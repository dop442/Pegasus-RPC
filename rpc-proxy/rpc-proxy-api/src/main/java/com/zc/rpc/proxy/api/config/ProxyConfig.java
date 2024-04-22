package com.zc.rpc.proxy.api.config;

import com.zc.rpc.proxy.api.consumer.Consumer;
import com.zc.rpc.registry.api.RegistryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-06
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProxyConfig<T> implements Serializable {
    private static final long serialVersionId = 6648940252795742398L;
    /**
     * 接口的Class实例
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
     * 超时时间
     */
    private long timeout;
    /**
     * 消费者接口
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
     * 注册服务的提供类
     */
    private RegistryService registryService;
}
