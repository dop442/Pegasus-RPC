package com.zc.test.consumer;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-06
 */
public class TestProxy {

    @Test
    public void testDynamicProxy() {
        ProxyHandler proxyHandler = new ProxyHandler();
        Service proxy = (Service) proxyHandler.createProxy(Service.class);
        proxy.eat();
        System.out.println("------------------");
        proxy.drink();
    }
}

interface Service {
    void eat();

    void drink();
}

class ServiceImpl implements Service{

    @Override
    public void eat() {
        System.out.println("eat");
    }

    @Override
    public void drink() {
        System.out.println("drink");
    }
}

class ProxyHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName());
        return null;
    }

    public <T> T createProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, this);

    }
}
