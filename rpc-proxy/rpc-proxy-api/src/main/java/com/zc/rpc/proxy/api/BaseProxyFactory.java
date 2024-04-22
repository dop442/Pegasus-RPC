package com.zc.rpc.proxy.api;

import com.zc.rpc.proxy.api.config.ProxyConfig;
import com.zc.rpc.proxy.api.object.ObjectProxy;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-06
 */
public abstract class BaseProxyFactory<T> implements ProxyFactory {

    protected ObjectProxy<T> objectProxy;

    @Override
    public <T> void init(ProxyConfig<T> proxyConfig) {
        this.objectProxy = new ObjectProxy(proxyConfig.getClazz(),
                proxyConfig.getServiceVersion(),
                proxyConfig.getServiceGroup(),
                proxyConfig.getTimeout(),
                proxyConfig.getConsumer(),
                proxyConfig.getSerializationType(),
                proxyConfig.isAsync(),
                proxyConfig.isOneway(),
                proxyConfig.getRegistryService());
    }
}
