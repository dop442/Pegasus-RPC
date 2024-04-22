package com.zc.rpc.loadbalancer.base;

import com.zc.rpc.loadbalancer.api.ServiceLoadBalancer;
import com.zc.rpc.protocol.meta.ServiceMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-21
 */
public abstract class BaseEnhancedServiceLoadBalancer implements ServiceLoadBalancer<ServiceMeta> {

    /**
     * @param servers 服务节点列表
     * @return 权重列表
     * @Description 生成权重列表，比如a的权重是2，b的权重是5，生成的列表是{a,a,b,b,b,b,b}
     */
    protected List<ServiceMeta> getWeightServiceMetaList(List<ServiceMeta> servers){
        if (servers==null ||servers.isEmpty()) {
            return null;
        }
        List<ServiceMeta> serviceMetaList = new ArrayList<>();
        servers.stream().forEach((server)->
                IntStream.range(0, server.getWeight()).forEach((i)->
                        serviceMetaList.add(server)));
        return serviceMetaList;
    }
}
