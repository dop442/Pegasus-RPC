package com.zc.rpc.registry.api;

import com.zc.rpc.protocol.meta.ServiceInfo;
import com.zc.rpc.protocol.meta.ServiceMeta;
import com.zc.rpc.registry.api.config.RegistryConfig;

import java.io.IOException;
import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-08
 */
public interface RegistryService {

    /** 服务注册
     * @param serviceMeta 服务元数据
     * @throws Exception 抛出异常
     */
    void registry(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务取消注册
     * @param serviceMeta 服务元数据
     * @throws Exception 抛出异常
     */
    void unRegistry(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务发现
     * @param serviceName 服务名称
     * @param invokerHashCode HashCode值
     * @return 服务元数据
     * @throws Exception 抛出异常
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode, String sourceIp) throws Exception;

    /**
     * 服务销毁
     * @throws IOException 抛出异常
     */
    void destroy() throws IOException;

    /**
     * 默认初始化方法
     */
    default void init(RegistryConfig registryConfig) throws Exception{};


    /**
     * @param serviceName 服务名
     * @Description Admin--获取该服务名的所有节点信息
     */
    List<ServiceInfo> discoverySimple(String serviceName) throws Exception;

    void unRegistrySimple(String serviceKey, String id) throws Exception;

    void updateNodeWeight(String serviceKey, String id, int weight) throws Exception;


}
