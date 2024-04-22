package com.zc.rpc.test.consumer.codec.init;

import com.zc.rpc.codec.RpcDecoder;
import com.zc.rpc.codec.RpcEncoder;
import com.zc.rpc.test.consumer.codec.handler.RpcTestConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Description 将测试的 RpcTestConsumerHandler 放入数据处理链中
 * @Author Schrodinger's Cobra
 * @Date 2024-01-18
 */
public class RpcTestConsumerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new LoggingHandler());
        cp.addLast(new RpcEncoder());
        cp.addLast(new RpcDecoder());
        cp.addLast(new RpcTestConsumerHandler());
    }
}
