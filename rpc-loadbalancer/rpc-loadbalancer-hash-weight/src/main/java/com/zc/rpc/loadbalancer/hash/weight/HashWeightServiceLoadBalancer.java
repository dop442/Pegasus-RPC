package com.zc.rpc.loadbalancer.hash.weight;

import com.zc.rpc.loadbalancer.api.ServiceLoadBalancer;
import com.zc.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-16
 */
@Slf4j
@SPIClass
public class HashWeightServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {
    @Override
    public T select(List<T> servers, int hashCode, String sourceIp) {
        log.info("基于加权Hash算法的负载均衡策略....");
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        hashCode = Math.abs(hashCode);
        int count = hashCode % servers.size();
        if (count<=0) {
            count = servers.size();
        }
        int index = hashCode % count;
        return servers.get(index);
    }
}
