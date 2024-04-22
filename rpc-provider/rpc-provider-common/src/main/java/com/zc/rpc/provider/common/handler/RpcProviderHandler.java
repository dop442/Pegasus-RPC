package com.zc.rpc.provider.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.zc.rpc.common.helper.RpcServerHelper;
import com.zc.rpc.common.threadpool.ServerThreadPool;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.enumeration.RpcStatus;
import com.zc.rpc.protocol.enumeration.RpcType;
import com.zc.rpc.protocol.header.RpcHeader;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.protocol.response.RpcResponse;
import com.zc.rpc.provider.common.cache.ProviderChannelCache;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import com.zc.rpc.constants.RpcConstants;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Description 请求的处理器，处理后返回响应
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
@Slf4j
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private final String reflectType;

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(String reflectType, Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
        this.reflectType = reflectType;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("与消费者的连接建立成功！！====={}", ctx.channel().toString());
        ProviderChannelCache.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel不活跃了");
        ProviderChannelCache.remove(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channel读取信息完毕");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("报错原因:", cause);
    }

    /**
     * @param ctx 上下文
     * @param evt 超时事件
     * @Description 触发读写超时事件时执行的逻辑
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            try {
                log.info("读写超时事件触发，关闭Channel：{}", channel);
                channel.close();
            } finally {
                channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {
        log.info("channel读取到数据，准备进行处理.....");
        ServerThreadPool.submit(() -> {
            RpcProtocol<RpcResponse> responseRpcProtocol = handlerMessage(protocol, ctx.channel());
            ctx.writeAndFlush(responseRpcProtocol).addListener(new ChannelFutureListener() {
                // 当写操作完成时，执行打印日志
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    log.info("对消费者id为{}的请求，服务端返回对应的相应", protocol.getHeader().getRequestId());
                }
            });
        });
    }


    /**
     * @param protocol 进入channel的protocol
     * @return 处理好的protocol
     * @Description 帮助channelRead0处理传入的protocol 根据protocol的类型做出不同的处理
     */
    private RpcProtocol<RpcResponse> handlerMessage(RpcProtocol<RpcRequest> protocol, Channel channel) {
        RpcProtocol<RpcResponse> responseRpcProtocol = null;
        RpcHeader header = protocol.getHeader();
        if (header.getMsgType() == (byte) RpcType.HEARTBEAT_FROM_CONSUMER.getType()) {
            log.info("接收的请求是“消费者的心跳请求”");
            responseRpcProtocol = handlerHeartbeatMessage(protocol, header);
        } else if (header.getMsgType() == (byte) RpcType.REQUEST.getType()) {
            log.info("接收的请求是“消费者的普通请求”");
            responseRpcProtocol = handlerRequestMessage(protocol, header);
        } else if (header.getMsgType() == (byte) RpcType.HEARTBEAT_TO_PROVIDER.getType()) {
            log.info("接收的响应是“消费者的心跳请求响应”");
            handlerHeartbeatMessageToProvider(protocol, channel);
        }
        return responseRpcProtocol;
    }

    private void handlerHeartbeatMessageToProvider(RpcProtocol<RpcRequest> protocol, Channel channel) {
        log.info("接收到来自消费者的心跳请求响应，消费者是：{}", channel.remoteAddress());
    }

    /**
     * @param protocol 进入channel的protocol
     * @return 返回组装好的protocol
     * @Description 如果进入channel的请求不是心跳请求，则由他处理
     */
    private RpcProtocol<RpcResponse> handlerRequestMessage(RpcProtocol<RpcRequest> protocol, RpcHeader header) {
        header.setMsgType((byte) RpcType.RESPONSE.getType());
        RpcRequest request = protocol.getBody();
        log.info("Receive request {}", header.getRequestId());
        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
        RpcResponse response = new RpcResponse();
        try {
            Object result = handle(request);
            response.setResult(result);
            response.setAsync(request.isAsync());
            response.setOneway(request.isOneway());
            header.setStatus((byte) RpcStatus.SUCCESS.getCode());
        } catch (Throwable t) {
            response.setError(t.toString());
            header.setStatus((byte) RpcStatus.FAIL.getCode());
            log.error("服务提供端处理请求异常", t);
        }
        responseRpcProtocol.setHeader(header);
        responseRpcProtocol.setBody(response);
        return responseRpcProtocol;
    }

    /**
     * @param protocol 进入channel的protocol
     * @return 返回组装好的protocol
     * @Description 如果进入channel的请求是心跳请求，则由他处理
     */
    private RpcProtocol<RpcResponse> handlerHeartbeatMessage(RpcProtocol<RpcRequest> protocol, RpcHeader header) {
        header.setMsgType((byte) RpcType.HEARTBEAT_TO_CONSUMER.getType());
        RpcRequest request = protocol.getBody();
        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
        RpcResponse response = new RpcResponse();
        response.setResult(RpcConstants.HEARTBEAT_PONG);
        response.setAsync(request.isAsync());
        response.setOneway(request.isOneway());
        header.setStatus((byte) RpcStatus.SUCCESS.getCode());
        responseRpcProtocol.setHeader(header);
        responseRpcProtocol.setBody(response);
        return responseRpcProtocol;
    }


    /**
     * @param request 从channel获取到的protocol中的RpcRequest
     * @return 处理后的结果
     * @Description 从channel中读取到protocol后，执行具体的处理逻辑（由代理增强去执行）
     */
    private Object handle(RpcRequest request) throws Throwable {
        String serviceKey = RpcServerHelper.buildServiceKey(request.getClassName(), request.getVersion(), request.getGroup());
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        log.debug("handle serviceClass name is {}", serviceClass.getName());
        log.debug("handle method is {}", methodName);
        if (parameterTypes != null) {
            for (Class<?> parameterType : parameterTypes) {
                log.debug("parameterType:{}", parameterType.getName());
            }
            for (Object parameter : parameters) {
                log.debug("parameter:{}", parameter);
            }
        }
        return invokeMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
    }


    private Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {

        switch (this.reflectType) {
            case RpcConstants.REFLECT_TYPE_JDK:
                return invokeJDKMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
            case RpcConstants.REFLECT_TYPE_CGLIB:
                return invokeCGLibMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
            default:
                throw new IllegalArgumentException("not support reflect type:"+reflectType);
        }

    }

    private Object invokeJDKMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        log.info("通过JDK的反射方式，构建具体实现业务逻辑的类，执行消费者调用的方法");
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }

    private Object invokeCGLibMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        log.info("通过CGLib的反射方式，构建具体实现业务逻辑的类，执行消费者调用的方法");
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

}
