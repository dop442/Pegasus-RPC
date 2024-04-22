package com.zc.rpc.consumer.common.initializer;

import com.zc.rpc.codec.RpcDecoder;
import com.zc.rpc.codec.RpcEncoder;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.consumer.common.handler.RpcConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-04
 */
public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {

    private int heartbeatInterval;

    public RpcConsumerInitializer(int heartbeatInterval) {
        if (heartbeatInterval > 0) {
            this.heartbeatInterval = heartbeatInterval;
        }
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler());
        pipeline.addLast(RpcConstants.CODEC_ENCODER, new RpcEncoder());
        pipeline.addLast(RpcConstants.CODEC_DECODER, new RpcDecoder());
        pipeline.addLast(RpcConstants.CODEC_CLIENT_IDLE_HANDLER, new IdleStateHandler(heartbeatInterval, 0, 0, TimeUnit.MILLISECONDS));
        pipeline.addLast(RpcConstants.CODEC_HANDLER, new RpcConsumerHandler());
    }
}
