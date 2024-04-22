package com.zc.rpc.proxy.api.object;

import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.enumeration.RpcType;
import com.zc.rpc.protocol.header.RpcHeaderFactory;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.proxy.api.async.IAsyncObjectProxy;
import com.zc.rpc.proxy.api.consumer.Consumer;
import com.zc.rpc.proxy.api.future.RpcFuture;
import com.zc.rpc.registry.api.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-05
 */
public class ObjectProxy<T> implements InvocationHandler, IAsyncObjectProxy {

    private static final Logger log = LoggerFactory.getLogger(ObjectProxy.class);
    /**
     * 接口的Class对象
     */
    private Class<T> clazz;
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
     * 服务消费者
     */
    private Consumer consumer;
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
     * 注册中心的服务提供类
     */
    private RegistryService registryService;

    public ObjectProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ObjectProxy(Class<T> clazz, String serviceVersion, String serviceGroup, long timeout, Consumer consumer, String serializationType, boolean async, boolean oneway, RegistryService registryService) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                StringBuilder builder = new StringBuilder();
                builder.append(proxy.getClass().getName())
                        .append("@")
                        .append(Integer.toHexString(System.identityHashCode(proxy)))
                        .append(", with InvocationHandler")
                        .append(this);
                return builder.toString();
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }
        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
        requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType, RpcType.REQUEST.getType()));
        RpcRequest request = new RpcRequest();
        request.setVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setGroup(this.serviceGroup);
        request.setParameters(args);
        request.setAsync(async);
        request.setOneway(oneway);
        requestRpcProtocol.setBody(request);
        log.debug("method DeclaringClass name: {}", method.getDeclaringClass().getName());
        log.debug("method name: {}", method.getName());

        if (method.getParameterTypes() != null && method.getParameterTypes().length > 0) {
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                log.debug("method ParameterTypes[{}] is {}", i, method.getParameterTypes()[i].getName());
            }
        }

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                log.debug("arg[{}] is {}:", i, args[i].toString());
            }
        }

        RpcFuture rpcFuture = this.consumer.sendRequest(requestRpcProtocol, registryService);
        return rpcFuture == null ? null : timeout > 0 ? rpcFuture.get(timeout, TimeUnit.MILLISECONDS) : rpcFuture.get();

    }

    @Override
    public RpcFuture call(String funcName, Object... args) {
        RpcProtocol<RpcRequest> request = createRequest(this.clazz.getName(), funcName, args);
        RpcFuture rpcFuture = null;
        try {
            rpcFuture = this.consumer.sendRequest(request, registryService);
        } catch (Exception e) {
            log.error("async all throws exception:{}", e.toString());
        }
        return rpcFuture;
    }

    private RpcProtocol<RpcRequest> createRequest(String className, String methodName, Object[] args) {
        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
        requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType, RpcType.REQUEST.getType()));
        RpcRequest request = new RpcRequest();
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameters(args);
        request.setVersion(this.serviceVersion);
        request.setGroup(this.serviceGroup);
        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassTypes(args[i]);
        }
        request.setParameterTypes(parameterTypes);
        requestRpcProtocol.setBody(request);
        log.debug("className:{}", className);
        log.debug("methodName:{}", methodName);
        for (int i = 0; i < parameterTypes.length; i++) {
            log.debug("parameterTypes No.{}: {}", i, parameterTypes[i].getName());
        }
        for (int i = 0; i < args.length; i++) {
            log.debug("parameter No.{}: {}", i, args[i]);
        }
        return requestRpcProtocol;
    }

    private Class<?> getClassTypes(Object object) {
        Class<?> classType = object.getClass();
        String typeName = classType.getName();
        switch (typeName) {
            case "java.lang.Integer":
                return Integer.TYPE;
            case "java.lang.Long":
                return Long.TYPE;
            case "java.lang.Float":
                return Float.TYPE;
            case "java.lang.Double":
                return Double.TYPE;
            case "java.lang.Character":
                return Character.TYPE;
            case "java.lang.Boolean":
                return Boolean.TYPE;
            case "java.lang.Short":
                return Short.TYPE;
            case "java.lang.Byte":
                return Byte.TYPE;
        }
        return classType;

    }


}
