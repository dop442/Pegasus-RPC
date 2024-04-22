package com.zc.rpc.loadbalancer.hash;

import com.zc.rpc.loadbalancer.api.ServiceLoadBalancer;
import com.zc.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-16
 */
@SPIClass
@Slf4j
public class HashServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {
    @Override
    public T select(List<T> servers, int hashCode, String sourceIp) {
        log.info("基于Hash算法的负载均衡策略...");
        if (servers == null||servers.isEmpty()) {
            return null;
        }
        int index = Math.abs(hashCode) % servers.size();
        return servers.get(index);
    }
}
