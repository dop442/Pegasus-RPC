package com.zc.rpc.proxy.api;

import com.zc.rpc.proxy.api.config.ProxyConfig;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-06
 */
public interface ProxyFactory {

    <T> T getProxy(Class<T> clazz);

    default <T> void init(ProxyConfig<T> proxyConfig){};

}
