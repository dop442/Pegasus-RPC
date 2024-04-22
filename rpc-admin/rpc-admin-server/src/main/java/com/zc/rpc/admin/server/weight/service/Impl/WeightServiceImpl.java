package com.zc.rpc.admin.server.weight.service.Impl;

import com.zc.rpc.admin.bean.POJO.ProviderWeight;
import com.zc.rpc.admin.server.weight.service.WeightService;
import com.zc.rpc.common.helper.RpcServerHelper;
import com.zc.rpc.protocol.meta.ServiceInfo;
import com.zc.rpc.protocol.meta.ServiceMeta;
import com.zc.rpc.registry.zookeeper.ZookeeperRegistryServiceImpl;
import javassist.scopedpool.ScopedClassPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-31
 */
@Service
public class WeightServiceImpl implements WeightService {

    @Autowired
    private ZookeeperRegistryServiceImpl zookeeperRegistryService;

    @Override
    public void updateWeight(String serviceName, String group, String version, String id, int weight) throws Exception {
        String serviceKey = RpcServerHelper.buildServiceKey(serviceName, version, group);
        zookeeperRegistryService.updateNodeWeight(serviceKey, id, weight);
    }

    @Override
    public List<ProviderWeight> queryWeight(String serviceName, String group, String version) throws Exception {
        List<ServiceInfo> serviceInfoList = zookeeperRegistryService.discoverySimple(RpcServerHelper.buildServiceKey(serviceName, version, group));
        return serviceInfoList.stream().map(serviceInfo -> {
            ServiceMeta serviceMeta = serviceInfo.getServiceMeta();
            return new ProviderWeight(serviceInfo.id, serviceMeta.getServiceAddr(), serviceMeta.getServicePort(), serviceMeta.getWeight());
        }).collect(Collectors.toList());
    }
}
