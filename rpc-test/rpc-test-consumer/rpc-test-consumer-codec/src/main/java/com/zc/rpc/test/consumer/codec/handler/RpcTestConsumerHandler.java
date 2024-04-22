package com.zc.rpc.test.consumer.codec.handler;

import com.alibaba.fastjson.JSONObject;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.header.RpcHeaderFactory;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 一个测试的消费者 Handler
 * @Author Schrodinger's Cobra
 * @Date 2024-01-18
 */
public class RpcTestConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private static final Logger log = LoggerFactory.getLogger(RpcTestConsumerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("数据发送开始");
        // 模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("com.zc.rpc.test.api.DemoService");
        request.setGroup("zc");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"hello zc!!!!!"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneway(false);
        protocol.setBody(request);
        log.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
        ctx.writeAndFlush(protocol).addListener((ChannelFutureListener) channelFuture -> {
            log.info("消息发送成功....");
            channelFuture.isSuccess();
        });
        log.info("数据发送完毕");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> protocol) throws Exception {
        log.info("服务消费者接收到的数据===>>>{}", JSONObject.toJSONString(protocol));
    }
}
