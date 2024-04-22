package com.zc.rpc.admin.server.discovery.service;

import com.zc.rpc.admin.bean.DTO.PageDTO;
import com.zc.rpc.admin.bean.DTO.ServiceDetailInfoDTO;
import com.zc.rpc.admin.bean.POJO.ServerInfo;
import com.zc.rpc.protocol.meta.ServiceInfo;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-06
 */
public interface ServerDiscoveryService {
    ServiceDetailInfoDTO getServiceDetail(String serviceName, String group, String version) throws Exception;

    PageDTO<ServerInfo> getServiceListPage(String serviceName, int size, int page) throws Exception;

    List<ServiceInfo> getAllService() throws Exception;

    void removeService(String serviceName, String group, String version, String id) throws Exception;
}
