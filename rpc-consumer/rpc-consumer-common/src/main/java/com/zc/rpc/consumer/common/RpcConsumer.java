package com.zc.rpc.consumer.common;

import com.zc.rpc.common.helper.RpcServerHelper;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.consumer.common.manager.ConsumerConnectionManager;
import com.zc.rpc.consumer.common.handler.RpcConsumerHandler;
import com.zc.rpc.consumer.common.helper.RpcConsumerHandlerHelper;
import com.zc.rpc.consumer.common.initializer.RpcConsumerInitializer;
import com.zc.rpc.consumer.common.ip.IpUtils;
import com.zc.rpc.protocol.meta.ServiceMeta;
import com.zc.rpc.proxy.api.threadpool.ClientThreadPool;
import com.zc.rpc.proxy.api.consumer.Consumer;
import com.zc.rpc.proxy.api.future.RpcFuture;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.registry.api.RegistryService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-04
 */
public class RpcConsumer implements Consumer {

    private static final Logger log = LoggerFactory.getLogger(RpcConsumer.class);
    private final String localIp;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();

    public static volatile RpcConsumer instance;

    /**
     * 心跳消息发送的间隔时间，默认30s
     */
    private int heartbeatInterval = 30000;

    /**
     * 扫描并移除空闲Channel连接的间隔时间，默认60s
     */
    private int scanNotActiveChannelInterval = 60000;

    /**
     * 定时执行心跳请求的线程池
     */
    private ScheduledExecutorService executorService;

    /**
     * 重试间隔时间
     */
    private int retryInterval = 1000;

    /**
     * 服务订阅和连接提供者的重试次数
     */
    private int retryTimes = 3;

    /**
     * 连接服务提供者的重试次数
     */
    private volatile int currentConnectRetryTimes = 0;


    private RpcConsumer(int heartbeatInterval, int scanNotActiveChannelInterval, int retryInterval, int retryTimes) {
        if (heartbeatInterval > 0) {
            this.heartbeatInterval = heartbeatInterval;
        }
        if (scanNotActiveChannelInterval > 0) {
            this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        }
        this.retryInterval = retryInterval <= 0 ? RpcConstants.DEFAULT_RETRY_INTERVAL : retryInterval;
        this.retryTimes = retryTimes <= 0 ? RpcConstants.DEFAULT_RETRY_TIMES : retryTimes;
        localIp = IpUtils.getLocalHostIp();
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcConsumerInitializer(heartbeatInterval));
        startHeartbeat();
    }

    public static RpcConsumer getInstance(int heartbeatInterval, int scanNotActiveChannelInterval, int retryInterval, int retryTimes) {
        if (instance == null) {
            synchronized (RpcConsumer.class) {
                if (instance == null) {
                    instance = new RpcConsumer(heartbeatInterval, scanNotActiveChannelInterval, retryInterval, retryTimes);
                }
            }
        }
        return instance;
    }

    private void startHeartbeat() {
        executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(() -> {
            log.info("-------扫描并清除所有不活跃的channel连接-------");
            ConsumerConnectionManager.scanNotActiveChannel();
        }, 10, scanNotActiveChannelInterval, TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(() -> {
            log.info("-----------消费者向服务端发送心跳请求----------");
            ConsumerConnectionManager.broadcastPingMessageFromConsumer();
        }, 1, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    public void close() {
        RpcConsumerHandlerHelper.closeRpcClientHandler();
        eventLoopGroup.shutdownGracefully();
        ClientThreadPool.shutdown();
    }

    private RpcConsumerHandler getRpcConsumerHandler(ServiceMeta serviceMeta) throws InterruptedException {
        String serviceAddress = serviceMeta.getServiceAddr();
        int port = serviceMeta.getServicePort();
        ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();
        channelFuture.addListener((ChannelFutureListener) listener -> {
            if (channelFuture.isSuccess()) {
                // 如果连接成功，将重试次数重置为0
                log.info("connect rpc server {} on port {} success", serviceAddress, port);
                currentConnectRetryTimes = 0;
            } else {
                log.error("connect rpc server {} on port {} fail", serviceAddress, port);
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
    }


    @Override
    public RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, RegistryService registryService) throws Exception {
        RpcRequest request = protocol.getBody();
        String serviceKey = RpcServerHelper.buildServiceKey(request.getClassName(), request.getVersion(), request.getGroup());
        Object[] params = request.getParameters();
        int invokerHashCode = (params == null || params.length <= 0) ? serviceKey.hashCode() : params[0].hashCode();
        ServiceMeta serviceMeta = getServiceMetaWithRetry(registryService, serviceKey, invokerHashCode);
        RpcConsumerHandler handler = null;
        if (serviceMeta != null) {
            handler = getRpcConsumerHandlerWithRetry(serviceMeta);
        }
        RpcFuture rpcFuture = null;
        if (handler!=null){
            rpcFuture = handler.sendRequest(protocol, request.isAsync(), request.isOneway());
        }
        return rpcFuture;
    }

    /**
     * @param registryService 注册中心服务
     * @param serviceKey 注册中心中的提供者服务的Key
     * @return 提供者的元数据
     * @Description 消费者向注册中心订阅服务（带有重试机制）
     */
    private ServiceMeta getServiceMetaWithRetry(RegistryService registryService, String serviceKey, int invokerHashCode) throws Exception{
        log.info("尝试从注册中心获取提供者元数据");
        ServiceMeta serviceMeta = registryService.discovery(serviceKey, invokerHashCode, localIp);
        if (serviceMeta==null){
            for (int i = 0; i < retryTimes; i++) {
                log.info("第【{}】次尝试从注册中心获取提供者元数据", i);
                serviceMeta = registryService.discovery(serviceKey, invokerHashCode, localIp);
                if (serviceMeta!=null){
                    break;
                }
                Thread.sleep(retryInterval);
            }
        }
        return serviceMeta;
    }

    /**
     * @param serviceMeta 服务提供者的元信息
     * @Description 向服务提供者获取服务（带有重试机制）
     */
    private RpcConsumerHandler getRpcConsumerHandlerWithRetry(ServiceMeta serviceMeta) throws InterruptedException{
        log.info("消费者正在连接提供者....");
        RpcConsumerHandler handler = null;
        try {
            handler = this.getRpcConsumerHandlerWithCache(serviceMeta);
        }catch (Exception e){
            if (e instanceof ConnectException){
                if (handler==null){
                    if (currentConnectRetryTimes<retryTimes){
                        currentConnectRetryTimes++;
                        log.info("服务消费者第【{}】次尝试重新连接服务提供者", currentConnectRetryTimes);
                        handler = getRpcConsumerHandlerWithRetry(serviceMeta);
                        Thread.sleep(retryInterval);
                    }
                }
            }
        }
        return handler;
    }

    /**
     * @Description 从缓存中获取RpcConsumerHandler
     */
    private RpcConsumerHandler getRpcConsumerHandlerWithCache(ServiceMeta serviceMeta) throws InterruptedException{
        RpcConsumerHandler handler = RpcConsumerHandlerHelper.get(serviceMeta);
        if (handler == null) {
            handler = getRpcConsumerHandler(serviceMeta);
            RpcConsumerHandlerHelper.put(serviceMeta, handler);
        } else if (!handler.getChannel().isActive()) {
            handler.close();
            handler = getRpcConsumerHandler(serviceMeta);
            RpcConsumerHandlerHelper.put(serviceMeta, handler);
        }
        return handler;
    }


}
