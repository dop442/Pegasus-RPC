package com.zc.rpc.spring.boot.provider.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@Getter
@Setter
public final class SpringBootProviderConfig {

    /**
     * 服务地址
     */
    private String serverAddress;
    /**
     * 注册到注册中心的地址
     */
    private String serverRegistryAddress;
    /**
     * 注册中心地址
     */
    private String registryAddress;
    /**
     * 注册类型
     */
    private String registryType;
    /**
     * 负载均衡类型
     */
    private String registryLoadBalanceType;
    /**
     * 反射类型
     */
    private String reflectType;

    /**
     * 心跳时间间隔
     */
    private int heartbeatInterval;

    /**
     * 扫描并清理不活跃连接的时间间隔
     */
    private int scanNotActiveChannelInterval;

    public SpringBootProviderConfig() {
    }

    public SpringBootProviderConfig(final String serverAddress, final String registryAddress, final String registryType, final String registryLoadBalanceType, final String reflectType, final int heartbeatInterval, int scanNotActiveChannelInterval, final String serverRegistryAddress) {
        this.serverAddress = serverAddress;
        this.registryAddress = registryAddress;
        this.registryType = registryType;
        this.registryLoadBalanceType = registryLoadBalanceType;
        this.reflectType = reflectType;
        if (heartbeatInterval > 0){
            this.heartbeatInterval = heartbeatInterval;
        }
        this.scanNotActiveChannelInterval = scanNotActiveChannelInterval;
        this.serverRegistryAddress = serverRegistryAddress;
    }


}
