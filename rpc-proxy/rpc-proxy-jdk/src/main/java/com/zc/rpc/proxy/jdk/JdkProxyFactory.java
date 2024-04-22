package com.zc.rpc.proxy.jdk;

import com.zc.rpc.proxy.api.BaseProxyFactory;
import com.zc.rpc.proxy.api.ProxyFactory;
import com.zc.rpc.proxy.api.consumer.Consumer;
import com.zc.rpc.proxy.api.object.ObjectProxy;

import java.lang.reflect.Proxy;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-05
 */
public class JdkProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<T> clazz){
        return ((T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, objectProxy));
    }

}
