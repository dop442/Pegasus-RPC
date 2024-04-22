package com.zc.rpc.spring.boot.consumer.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@Getter
@Setter
public final class SpringBootConsumerConfig {
    /**
     * 缓存地址
     */
    private String redisAddress;
    /**
     * 注册地址
     */
    private String registryAddress;
    /**
     * 注册类型
     */
    private String registryType;
    /**
     * 负载均衡类型
     */
    private String loadBalanceType;
    /**
     * 代理
     */
    private String proxy;
    /**
     * 版本号
     */
    private String version;
    /**
     * 分组
     */
    private String group;
    /**
     * 序列化类型
     */
    private String serializationType;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 是否异步
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    /**
     * 心跳检测
     */
    private int heartbeatInterval;

    /**
     * 扫描并移除不活跃连接的时间间隔
     */
    private int scanNotActiveChannelInterval;

    //重试间隔时间
    private int retryInterval = 1000;

    //重试次数
    private int retryTimes = 3;

    public SpringBootConsumerConfig() {
    }

    public SpringBootConsumerConfig(final String redisAddress,final String registryAddress, final String registryType, final String loadBalanceType, final String proxy, final String version, final String group, final String serializationType, final int timeout, final boolean async, final boolean oneway, final int heartbeatInterval, final int scanNotActiveChannelInterval, final int retryInterval, final int retryTimes) {
        this.redisAddress = redisAddress;
        this.registryAddress = registryAddress;
        this.registryType = registryType;
        this.loadBalanceType = loadBalanceType;
        this.proxy = proxy;
        this.version = version;
        this.group = group;
        this.serializationType = serializationType;
        this.timeout = timeout;
        this.async = async;
        this.oneway = oneway;
        if (heartbeatInterval > 0){
            this.heartbeatInterval = heartbeatInterval;
        }
        this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        this.retryInterval = retryInterval;
        this.retryTimes = retryTimes;
    }
}
