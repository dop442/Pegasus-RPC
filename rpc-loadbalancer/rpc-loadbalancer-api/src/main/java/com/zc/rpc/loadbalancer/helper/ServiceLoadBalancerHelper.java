package com.zc.rpc.loadbalancer.helper;

import com.zc.rpc.protocol.meta.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description 将List <ServiceInstance<ServiceMeta>>列表转换成List<ServiceMeta>列表
 * @Author Schrodinger's Cobra
 * @Date 2024-02-21
 */
public class ServiceLoadBalancerHelper {
    private static volatile List<ServiceMeta> cacheServiceMeta = new CopyOnWriteArrayList<>();

    public static List<ServiceMeta> getServiceMetaList(List<ServiceInstance<ServiceMeta>> serviceInstances) {
        if (serviceInstances == null || serviceInstances.isEmpty() || cacheServiceMeta.size() == serviceInstances.size()) {
            return cacheServiceMeta;
        }
        cacheServiceMeta.clear();
        serviceInstances.forEach((serviceMetaServiceInstance -> cacheServiceMeta.add(serviceMetaServiceInstance.getPayload())));
        return cacheServiceMeta;
    }
}
