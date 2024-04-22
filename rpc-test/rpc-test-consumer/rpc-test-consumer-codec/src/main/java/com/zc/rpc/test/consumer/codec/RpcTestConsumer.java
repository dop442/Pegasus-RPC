package com.zc.rpc.test.consumer.codec;

import com.zc.rpc.test.consumer.codec.init.RpcTestConsumerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 用于测试的消费者
 * @Author Schrodinger's Cobra
 * @Date 2024-01-18
 */
@Slf4j
public class RpcTestConsumer {

    /**
     * @Description 启动消费者 连接提供者
     * 触发 RpcTestConsumerHandler的 channelActive()方法 向提供者发送数据
     */
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcTestConsumerInitializer());
            Channel channel = bootstrap.connect("127.0.0.1", 27880).sync().channel();
            log.info("与提供者的建立成功！！===={}",channel.toString());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            Thread.sleep(2000);
            eventLoopGroup.shutdownGracefully();
        }
    }

}
