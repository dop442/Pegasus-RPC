package com.zc.rpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.zc.rpc.common.cache.ConsumerChannelCache;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.consumer.common.context.RpcContext;
import com.zc.rpc.protocol.enumeration.RpcStatus;
import com.zc.rpc.protocol.enumeration.RpcType;
import com.zc.rpc.protocol.header.RpcHeaderFactory;
import com.zc.rpc.proxy.api.future.RpcFuture;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.header.RpcHeader;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.protocol.response.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Descriptionb
 * @Author Schrodinger's Cobra
 * @Date 2024-02-04
 */
@Slf4j
@Getter
public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private volatile Channel channel;

    private SocketAddress remotePeer;

    private Map<Long, RpcFuture> pendingRpc = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("消费者channel连接成功");
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
        ConsumerChannelCache.add(channel);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    /**
     * @param evt 触发的事件
     * @Description 如果触发的是读写超时的事件，那么就尝试给提供者发送一次心跳请求
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info("消费者发生读空闲，因此向提供者发送心跳请求");
            RpcHeader header = RpcHeaderFactory.getRequestHeader(RpcConstants.SERIALIZATION_JDK, RpcType.HEARTBEAT_FROM_CONSUMER.getType());
            RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
            RpcRequest request = new RpcRequest();
            request.setParameters(new Object[]{RpcConstants.HEARTBEAT_PING});
            requestRpcProtocol.setHeader(header);
            requestRpcProtocol.setBody(request);
            ctx.writeAndFlush(requestRpcProtocol);
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> protocol) throws Exception {
        log.info("消费者读取到数据...");
        if (protocol == null) {
            return;
        }
        handlerMessage(protocol);
    }

    /**
     * @param protocol 服务端返回的响应信息protocol
     * @Description 处理服务端返回的响应信息
     */
    private void handlerMessage(RpcProtocol<RpcResponse> protocol) {
        RpcHeader header = protocol.getHeader();
        if (header.getMsgType()==(byte) RpcType.HEARTBEAT_TO_CONSUMER.getType()) {
            handlerHeartbeatMessage(protocol, channel);
        } else if (header.getMsgType()==(byte) RpcType.RESPONSE.getType()) {
            handlerResponseMessage(protocol, header);
        } else if (header.getMsgType()==(byte) RpcType.HEARTBEAT_FROM_PROVIDER.getType()){
            handlerHeartbeatMessageFromProvider(protocol, channel);
        }
    }

    /**
     * @param protocol 提供者发送过来的“心跳请求”
     * @Description 向消费者返回“心跳请求响应” RpcProtocol(RpcRequest)
     */
    private void handlerHeartbeatMessageFromProvider(RpcProtocol<RpcResponse> protocol, Channel channel) {
        RpcHeader header = protocol.getHeader();
        header.setMsgType((byte) RpcType.HEARTBEAT_TO_PROVIDER.getType());
        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
        RpcRequest request = new RpcRequest();
        request.setParameters(new Object[]{RpcConstants.HEARTBEAT_PONG});
        header.setStatus((byte) RpcStatus.SUCCESS.getCode());
        requestRpcProtocol.setHeader(header);
        requestRpcProtocol.setBody(request);
        channel.writeAndFlush(requestRpcProtocol);
    }

    /**
     * @Description 接收到服务端返回“普通响应”后的相应处理
     */
    private void handlerResponseMessage(RpcProtocol<RpcResponse> protocol, RpcHeader header) {
        long requestId = header.getRequestId();
        RpcFuture rpcFuture = pendingRpc.remove(requestId);
        if (rpcFuture != null) {
            rpcFuture.done(protocol);
        }
    }

    /**
     * @Description 接收到服务端返回的“心跳响应”后的相应处理
     */
    private void handlerHeartbeatMessage(RpcProtocol<RpcResponse> protocol, Channel channel) {
        log.info("接收到服务端返回的“心跳响应”");
    }

    public RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, boolean async, boolean oneway){
        return oneway?this.sendRequestOneway(protocol) :async?sendRequestAsync(protocol):this.sendRequestSync(protocol);
    }

    // 同步调用
    public RpcFuture sendRequestSync(RpcProtocol<RpcRequest> protocol){
        log.info("消费者发送的数据===>>{}", JSONObject.toJSONString(protocol));
        RpcFuture rpcFuture=this.getRpcFuture(protocol);
        channel.writeAndFlush(protocol);
        return rpcFuture;
    }

    // 异步调用
    public RpcFuture sendRequestAsync(RpcProtocol<RpcRequest> protocol){
        RpcFuture rpcFuture = this.getRpcFuture(protocol);
        RpcContext.getContext().setRpcFuture(rpcFuture);
        channel.writeAndFlush(protocol);
        return null;
    }

    // 单向调用
    public RpcFuture sendRequestOneway(RpcProtocol<RpcRequest> protocol){
        channel.writeAndFlush(protocol);
        return null;
    }


    private RpcFuture getRpcFuture(RpcProtocol<RpcRequest> protocol){
        RpcFuture rpcFuture = new RpcFuture(protocol);
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        pendingRpc.put(requestId, rpcFuture);
        return rpcFuture;
    }

    public void close(){
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

}
