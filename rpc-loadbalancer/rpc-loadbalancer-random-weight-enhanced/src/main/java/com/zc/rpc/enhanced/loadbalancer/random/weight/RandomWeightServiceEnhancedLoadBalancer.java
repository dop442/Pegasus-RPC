package com.zc.rpc.enhanced.loadbalancer.random.weight;

import com.zc.rpc.loadbalancer.base.BaseEnhancedServiceLoadBalancer;
import com.zc.rpc.protocol.meta.ServiceMeta;
import com.zc.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * @Description 获取“权重列表”，从权重列表中随机挑选一个节点
 * @Author Schrodinger's Cobra
 * @Date 2024-02-21
 */
@Slf4j
@SPIClass
public class RandomWeightServiceEnhancedLoadBalancer extends BaseEnhancedServiceLoadBalancer {
    @Override
    public ServiceMeta select(List<ServiceMeta> servers, int hashCode, String sourceIp) {
        log.info("基于增强型加权随机算法的负载均衡策略...");
        servers = getWeightServiceMetaList(servers);
        if (servers==null|| servers.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
