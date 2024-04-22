package com.zc.rpc.provider.common.server.base;

import com.zc.rpc.codec.RpcDecoder;
import com.zc.rpc.codec.RpcEncoder;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.provider.common.handler.RpcProviderHandler;
import com.zc.rpc.provider.common.manager.ProviderConnectionManager;
import com.zc.rpc.provider.common.server.api.Server;
import com.zc.rpc.registry.api.RegistryService;
import com.zc.rpc.registry.api.config.RegistryConfig;
import com.zc.rpc.registry.zookeeper.ZookeeperRegistryServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
@Slf4j
public class BaseServer implements Server {
    protected String host = "127.0.0.1";
    protected int port = 27110;
    private String reflectType;
    protected RegistryService registryService;
    protected Map<String, Object> handlerMap = new HashMap<>();
    /**
     * 执行定时心跳检测任务的线程池
     */
    private ScheduledExecutorService executorService;

    /**
     * 心跳检测间隔时间，默认30s
     */
    private int heartbeatInterval = 30000;

    /**
     * 扫描移除空闲Channel时间间隔，默认60s
     */
    private int scanNotActiveChannelInterval = 60000;

    protected String registryAddress;

    protected String registryType;

    protected String registryLoadBalanceType;

    public BaseServer(String serverAddress, String registryAddress, String registryType, String reflectType, String registryLoadBalanceType, int heartbeatInterval, int scanNotActiveChannelInterval) {
        if (heartbeatInterval > 0) {
            this.heartbeatInterval = heartbeatInterval;
        }
        if (scanNotActiveChannelInterval > 0) {
            this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        }
        if (!StringUtils.isEmpty(serverAddress)) {
            String[] serverArray = serverAddress.split(":");
            this.host = serverArray[0];
            this.port = Integer.parseInt(serverArray[1]);
        }
        this.reflectType = reflectType;
        this.registryAddress = registryAddress;
        this.registryType = registryType;
        this.registryLoadBalanceType = registryLoadBalanceType;
        this.registryService = this.getRegistryService(registryAddress, registryType, registryLoadBalanceType);
    }

    private RegistryService getRegistryService(String registryAddress, String registryType, String registryLoadBalanceType) {
        // TODO 后续拓展SPI
        RegistryService registryService = null;
        try {
            registryService = new ZookeeperRegistryServiceImpl();
            registryService.init(new RegistryConfig(registryAddress, registryType, registryLoadBalanceType));
        } catch (Exception e) {
            log.error("RPC Server init error", e);
        }
        return registryService;
    }

    @Override
    public void startNettyServer() {
        startHeartbeat();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new LoggingHandler())
                                    .addLast(RpcConstants.CODEC_DECODER, new RpcDecoder())
                                    .addLast(RpcConstants.CODEC_ENCODER, new RpcEncoder())
                                    .addLast(RpcConstants.CODEC_SERVER_IDLE_HANDLER, new IdleStateHandler(0, 0, heartbeatInterval, TimeUnit.MILLISECONDS))
                                    .addLast(RpcConstants.CODEC_HANDLER, new RpcProviderHandler(reflectType, handlerMap));
                        }
                    });
            ChannelFuture future = bootstrap.bind(host, port).sync();
            log.info("Server started on {}:{} ", host, port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("RPC Server start error", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * @Description 开启心跳请求
     */
    private void startHeartbeat() {
        executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(() -> {
            log.info("===========扫描不活跃的Channel连接==============");
            ProviderConnectionManager.scanNotActiveChannel();
        }, 10, scanNotActiveChannelInterval, TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(() -> {
            ProviderConnectionManager.broadcastPingMessageFromProvider();
        }, 3, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

}
