package com.zc.rpc.admin.server.discovery.service.impl;

import com.zc.rpc.admin.bean.DTO.PageDTO;
import com.zc.rpc.admin.bean.DTO.ServiceDetailInfoDTO;
import com.zc.rpc.admin.bean.POJO.Provider;
import com.zc.rpc.admin.bean.POJO.ServerInfo;
import com.zc.rpc.protocol.meta.ServiceInfo;
import com.zc.rpc.admin.server.discovery.service.ServerDiscoveryService;
import com.zc.rpc.admin.utils.page.PageUtil;
import com.zc.rpc.common.helper.RpcServerHelper;
import com.zc.rpc.protocol.meta.ServiceMeta;
import com.zc.rpc.registry.zookeeper.ZookeeperRegistryServiceImpl;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-06
 */
@Service
public class ServerDiscoveryServiceImpl implements ServerDiscoveryService {

    @Autowired
    private ZookeeperRegistryServiceImpl zookeeperRegistryService;


    @Value("${admin-server.discovery.zookeeperPath}")
    private String zookeeperPath;

    @Override
    public ServiceDetailInfoDTO getServiceDetail(String serviceName, String group, String version) throws Exception {
        ServiceDetailInfoDTO serviceDetailInfo = ServiceDetailInfoDTO
                .builder()
                .serviceName(serviceName)
                .serviceGroup(group)
                .serviceVersion(version).build();
        List<Provider> providerList = new ArrayList<>();
        String serviceKey = RpcServerHelper.buildServiceKey(serviceName, version, group);
        List<ServiceInfo> serviceInfoList = zookeeperRegistryService.discoverySimple(serviceKey);
        serviceInfoList.forEach(serviceInfo -> {
            ServiceMeta serviceMeta = serviceInfo.serviceMeta;
            Provider provider = new Provider(serviceMeta.getServiceAddr(), serviceMeta.getServicePort(), serviceMeta.getWeight());
            providerList.add(provider);
        });
        serviceDetailInfo.providerList = providerList;
        return serviceDetailInfo;
    }

    @Override
    public PageDTO<ServerInfo> getServiceListPage(String serviceName, int size, int page) throws Exception {
        List<ServerInfo> serverInfos = getServerInfos(serviceName);
        PageUtil<ServerInfo> pageUtil = new PageUtil<>();
        pageUtil.setPageNum(page);
        pageUtil.setPageSize(size);
        List<ServerInfo> data = pageUtil.setMyList(serverInfos).getData();
        int count = data.size();
        return new PageDTO<>(data, count);
    }


    @Override
    public List<ServiceInfo> getAllService() throws Exception {
        List<String> children = nextPath(zookeeperPath+"/service");
        List<ServiceInfo> serverList = new ArrayList<>();
        for (String child : children) {
            List<ServiceInfo> serviceMetaList = zookeeperRegistryService.discoverySimple(child);
            serverList.add(serviceMetaList.get(0));
        }
        return serverList;
    }

    @Override
    public void removeService(String serviceName, String group, String version, String id) throws Exception {
        String serviceKey = RpcServerHelper.buildServiceKey(serviceName, version, group);
        zookeeperRegistryService.unRegistrySimple(serviceKey, id);
    }

    private List<String> nextPath(String path) throws Exception {
        CuratorFramework client = zookeeperRegistryService.client;
        return client.getChildren().forPath(path);
    }


    private List<ServerInfo> getServerInfos(String serviceName) throws Exception {
        List<String> children = nextPath(zookeeperPath+"/service");
        List<ServerInfo> rowResult = children.stream().map(s -> {
            String[] info = s.split("#");
            return new ServerInfo(info[0], info[1], info[2]);
        }).collect(Collectors.toList());
        if (!serviceName.equals("*")){
            return rowResult.stream().filter(serverInfo -> serverInfo.getServiceName().contains(serviceName)).collect(Collectors.toList());
        }
        return rowResult;
    }


}
