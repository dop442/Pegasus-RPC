package com.zc.rpc.consumer;

import com.zc.rpc.consumer.common.RpcConsumer;
import com.zc.rpc.consumer.common.exception.RegistryException;
import com.zc.rpc.proxy.api.async.IAsyncObjectProxy;
import com.zc.rpc.proxy.api.config.ProxyConfig;
import com.zc.rpc.proxy.api.object.ObjectProxy;
import com.zc.rpc.proxy.jdk.JdkProxyFactory;
import com.zc.rpc.registry.api.RegistryService;
import com.zc.rpc.registry.api.config.RegistryConfig;
import com.zc.rpc.registry.zookeeper.ZookeeperRegistryServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-05
 */
public class RpcClient {

    private static final Logger log = LoggerFactory.getLogger(RpcClient.class);
    /**
     * 服务版本号
     */
    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 超时时间，默认15s
     */
    private long timeout = 15000;
    /**
     * 序列化类型
     */
    private String serializationType;
    /**
     * 是否异步调用
     */
    private boolean async;
    /**
     * 是否单向调用
     */
    private boolean oneway;
    /**
     * 注册服务的提供类
     */
    private RegistryService registryService;

    /**
     * 心跳消息的间隔时间，默认30s
     */
    private int heartbeatInterval;

    /**
     * 扫描并清除空闲连接的间隔时间，默认60s
     */
    private int scanNotActiveChannelInterval;

    /**
     * 重连尝试时间间隔
     */
    private int retryInterval = 1000;

    /**
     * 重连尝试次数
     */
    private int retryTimes = 3;

    private String proxy;

    public RpcClient(String serviceVersion, String serviceGroup, String serializationType, long timeout, boolean async, boolean oneway, String registryAddress, String registryType, String registryLoadBalanceType, int heartbeatInterval, int scanNotActiveChannelInterval, int retryInterval, int retryTimes, String proxy) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.serviceGroup = serviceGroup;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.registryService = this.getRegistryService(registryAddress, registryType, registryLoadBalanceType);
        this.heartbeatInterval = heartbeatInterval;
        this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        this.retryInterval = retryInterval;
        this.retryTimes = retryTimes;
        this.proxy = proxy;
    }

    public <T> T create(Class<T> interfaceClass) {
        JdkProxyFactory<T> proxyFactory = new JdkProxyFactory<>();
        proxyFactory.init(
                new ProxyConfig<>(interfaceClass, serviceVersion, serviceGroup,
                        timeout, RpcConsumer.getInstance(heartbeatInterval, scanNotActiveChannelInterval, retryInterval, retryTimes), serializationType, async, oneway, registryService)
        );
        return proxyFactory.getProxy(interfaceClass);
    }

    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass, serviceVersion, serviceGroup,
                timeout, RpcConsumer.getInstance(heartbeatInterval, scanNotActiveChannelInterval, retryInterval, retryTimes), serializationType, async, oneway, registryService);
    }

    private RegistryService getRegistryService(String registryAddress, String registryType, String registryLoadBalanceType) {
        if (StringUtils.isEmpty(registryType)) {
            throw new IllegalArgumentException("registry type is null");
        }
        // TODO 后续 SPI 拓展
        registryService = new ZookeeperRegistryServiceImpl();
        try {
            registryService.init(new RegistryConfig(registryAddress, registryType, registryLoadBalanceType));
        } catch (Exception e) {
            log.error("RpcClient init registry service throws exception:", e);
            throw new RegistryException(e.getMessage(), e);
        }
        return registryService;
    }


    public void shutdown() {
        RpcConsumer.getInstance(heartbeatInterval, scanNotActiveChannelInterval, retryInterval, retryTimes).close();
    }


}
