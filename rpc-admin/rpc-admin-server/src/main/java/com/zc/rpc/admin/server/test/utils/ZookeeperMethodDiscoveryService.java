package com.zc.rpc.admin.server.test.utils;

import com.zc.rpc.protocol.meta.ServiceMethodMeta;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
  *@Description 获取”方法发现“的Zookeeper服务
  *@Author Schrodinger's Cobra
  *@Date 2024-03-07
*/
public class ZookeeperMethodDiscoveryService{

    private static final int BASE_SLEEP_TIME_MS = 1000;

    private static final int MAX_RETRIES = 3;

    private CuratorFramework client;

    private ServiceDiscovery<ServiceMethodMeta> methodDiscovery;

    private String serviceKey;

    private String path = "/zc_rpc/method";


    public void init(String registryAddr, String serviceKey){
        this.serviceKey = serviceKey;
        client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
        JsonInstanceSerializer<ServiceMethodMeta> serializer = new JsonInstanceSerializer<>(ServiceMethodMeta.class);
        methodDiscovery = ServiceDiscoveryBuilder.builder(ServiceMethodMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(path)
                .build();
    }

    /**
     * @Description 获取该服务的所有方法列表的Meta信息
     */
    public List<ServiceMethodMeta> getAllMethodMetaInService() throws Exception {
        List<ServiceInstance<ServiceMethodMeta>> allMethodInfoInService = getAllMethodInfoInService();
        return allMethodInfoInService.stream().map(ServiceInstance::getPayload).collect(Collectors.toList());
    }

    public List<ServiceInstance<ServiceMethodMeta>> getAllMethodInfoInService() throws Exception{
        Collection<ServiceInstance<ServiceMethodMeta>> serviceInstances = methodDiscovery.queryForInstances(serviceKey);
        return new ArrayList<>(serviceInstances);
    }





}
