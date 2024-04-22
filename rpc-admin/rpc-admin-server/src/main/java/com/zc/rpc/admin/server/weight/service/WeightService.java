package com.zc.rpc.admin.server.weight.service;

import com.zc.rpc.admin.bean.POJO.ProviderWeight;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-31
 */
public interface WeightService {


    void updateWeight(String serviceName, String group, String version, String id, int weight) throws Exception;

    List<ProviderWeight> queryWeight(String serviceName, String group, String version) throws Exception;

}
