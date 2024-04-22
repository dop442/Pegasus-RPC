package com.zc.rpc.loadbalancer.api;

import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.spi.annotation.SPI;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-10
 */
@SPI(RpcConstants.SERVICE_LOAD_BALANCER_RANDOM)
public interface ServiceLoadBalancer<T> {

    /**
     * 以负载均衡的方式选取一个服务节点
     * @param servers 服务列表
     * @param hashCode Hash值
     * @return 可用的服务节点
     */
    T select(List<T> servers, int hashCode, String sourceIp);

}
