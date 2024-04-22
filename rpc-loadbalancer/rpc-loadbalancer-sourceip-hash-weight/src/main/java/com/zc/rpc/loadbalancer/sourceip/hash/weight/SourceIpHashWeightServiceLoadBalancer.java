package com.zc.rpc.loadbalancer.sourceip.hash.weight;

import com.zc.rpc.loadbalancer.api.ServiceLoadBalancer;
import com.zc.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-20
 */
@Slf4j
@SPIClass
public class SourceIpHashWeightServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {

    @Override
    public T select(List<T> servers, int hashCode, String sourceIp) {
        if (servers==null||servers.isEmpty()) {
            return null;
        }
        if (StringUtils.isEmpty(sourceIp)) {
            return servers.get(0);
        }
        int count = Math.abs(hashCode) % servers.size();
        if (count==0) {
            count=servers.size();
        }
        int resultHashCode = Math.abs(sourceIp.hashCode() + hashCode);
        return servers.get(resultHashCode%count);
    }
}
