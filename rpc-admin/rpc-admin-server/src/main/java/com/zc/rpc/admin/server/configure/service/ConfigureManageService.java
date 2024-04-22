package com.zc.rpc.admin.server.configure.service;

import com.zc.rpc.admin.bean.POJO.ConsumerInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-01
 */
public interface ConfigureManageService {
    void addConfigure(String configureName, String consumerName, String serviceName, String version, String group, String configure);

    List<ConsumerInfo> getAllConsumerInfo();

    void deleteConfigure(String configureName);

    Map<String, String> getConfigure(String configureName);

    void updateConfigure(String configureName, String configure);

    List<String> getAllConfigureName();

}
