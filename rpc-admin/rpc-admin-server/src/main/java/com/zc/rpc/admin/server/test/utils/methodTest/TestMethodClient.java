package com.zc.rpc.admin.server.test.utils.methodTest;

import com.zc.rpc.codec.RpcDecoder;
import com.zc.rpc.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Description 用于测试服务的Netty客户端
 * @Author Schrodinger's Cobra
 * @Date 2024-03-13
 */
public class TestMethodClient {

    private Bootstrap bootstrap;
    private String address;
    private int port;
    private TestMethodHandler testMethodHandler;


    public TestMethodClient(String address, int port, String serviceName, String group, String version, String methodName, String[] parametersStr, String[] parameterTypeStr) {
        this.bootstrap = new Bootstrap();
        this.address = address;
        this.port = port;
        testMethodHandler = new TestMethodHandler(serviceName, group, version, methodName, parametersStr, parameterTypeStr);
    }

    public Object testMethod() throws InterruptedException{
        Object result;
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(eventLoopGroup);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler());
                    ch.pipeline().addLast(new RpcEncoder());
                    ch.pipeline().addLast(new RpcDecoder());
                    ch.pipeline().addLast(testMethodHandler);
                }
            });
            Channel channel = bootstrap.connect(address, port).sync().channel();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            result = testMethodHandler.getResult();
            int count = 0;
            while (result==null&&count<10){
                Thread.sleep(2000);
                result = testMethodHandler.getResult();
                count++;
            }
            eventLoopGroup.shutdownGracefully();
        }
        return result;
    }
}
