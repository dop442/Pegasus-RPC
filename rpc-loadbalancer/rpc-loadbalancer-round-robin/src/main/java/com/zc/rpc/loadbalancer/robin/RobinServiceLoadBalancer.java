package com.zc.rpc.loadbalancer.robin;

import com.zc.rpc.loadbalancer.api.ServiceLoadBalancer;
import com.zc.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-16
 */
@SPIClass
@Slf4j
public class RobinServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {
    private volatile AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    public T select(List<T> servers, int hashCode, String sourceIp) {
        log.info("基于轮询算法的负载均衡策略....");
        if (servers == null||servers.isEmpty()) {
            return null;
        }
        int count = servers.size();
        int index = atomicInteger.incrementAndGet();
        if (index>=(Integer.MAX_VALUE-10000)) {
            atomicInteger.set(0);
        }
        return servers.get(index%count);

    }
}
