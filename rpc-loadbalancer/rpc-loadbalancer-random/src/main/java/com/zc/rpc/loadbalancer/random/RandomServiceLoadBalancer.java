package com.zc.rpc.loadbalancer.random;

import com.zc.rpc.loadbalancer.api.ServiceLoadBalancer;
import com.zc.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-10
 */
@Slf4j
@SPIClass
public class RandomServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {

    @Override
    public T select(List<T> servers, int hashCode, String sourceIp) {
        log.info("基于“随机选择”的负载均衡策略");
        if (servers == null||servers.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
