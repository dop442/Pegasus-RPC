package com.zc.rpc.admin.server.loadbalance.service;

import com.zc.rpc.admin.bean.DTO.LoadBalanceInfo;
import com.zc.rpc.admin.utils.resp.Result;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-17
 */
public interface LoadBalanceService {

    LoadBalanceInfo getServiceLoadBalanceInfo(String applicationName ,String serviceName, String group, String version);


    void removeLoadBalance(String applicationName ,String serviceName, String version, String group);

    void updateLoadBalance(String applicationName, String serviceName, String version, String group, String loadBalance);

    List<LoadBalanceInfo> getProviderListThroughConsumer(String applicationName);


}
