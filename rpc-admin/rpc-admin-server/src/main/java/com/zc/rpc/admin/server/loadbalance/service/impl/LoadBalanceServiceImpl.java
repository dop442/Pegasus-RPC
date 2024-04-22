package com.zc.rpc.admin.server.loadbalance.service.impl;

import com.zc.rpc.admin.bean.DTO.LoadBalanceInfo;
import com.zc.rpc.admin.bean.POJO.Provider;
import com.zc.rpc.admin.server.loadbalance.service.LoadBalanceService;
import com.zc.rpc.admin.utils.resp.Result;
import com.zc.rpc.protocol.meta.ServiceMeta;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-17
 */
@Service
public class LoadBalanceServiceImpl implements LoadBalanceService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ServiceDiscovery<ServiceMeta> serviceDiscovery;

    @Override
    public LoadBalanceInfo getServiceLoadBalanceInfo(String applicationName ,String serviceName, String group, String version) {
        String serviceKey = getServiceKey(serviceName, group, version);
        String redisKey = getRedisKey(applicationName,serviceName, group, version);
        Map<String, String> propertiesMap = redisTemplate.opsForHash().entries(redisKey);
        List<Provider> providerList = new ArrayList<>();
        try {
            Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceKey);
            serviceInstances.forEach(serviceMetaServiceInstance -> {
                ServiceMeta payload = serviceMetaServiceInstance.getPayload();
                providerList.add(new Provider(payload.getServiceAddr(), payload.getServicePort(), payload.getWeight()));
            });
        } catch (Exception e) {
            return new LoadBalanceInfo(serviceName, group, version, null, null);
        }
        return new LoadBalanceInfo(serviceName, group, version, propertiesMap.get("loadbalance"), providerList);
    }

    @Override
    public void removeLoadBalance(String applicationName ,String serviceName, String version, String group) {
        String redisKey = getRedisKey(applicationName, serviceName, group, version);
        redisTemplate.opsForHash().put(redisKey, "loadbalance", "random");
    }

    @Override
    public void updateLoadBalance(String applicationName, String serviceName, String version, String group, String loadBalance) {
        String redisKey = getRedisKey(applicationName, serviceName, group, version);
        redisTemplate.opsForHash().put(redisKey, "loadbalance", loadBalance);
    }

    @Override
    public List<LoadBalanceInfo> getProviderListThroughConsumer(String applicationName) {
        Set<String> keys = redisTemplate.keys("*:" + applicationName + ":*");
        List<LoadBalanceInfo> loadBalanceInfoList = new ArrayList<>();
        for (String key : keys) {
            String[] split = key.split(":");
            String[] serviceNameVersionGroup = split[2].split("#");
            String serviceName = serviceNameVersionGroup[0];
            String version = serviceNameVersionGroup[1];
            String group = serviceNameVersionGroup[2];
            LoadBalanceInfo serviceLoadBalanceInfo = getServiceLoadBalanceInfo(applicationName, serviceName, group, version);
            loadBalanceInfoList.add(serviceLoadBalanceInfo);
        }
        return loadBalanceInfoList;
    }

//    @Override
//    public Map<String, String> getConsumer(String applicationName) {
//        Set<String> keys = redisTemplate.keys("*:" + applicationName + ":*");
//        List<Map<String, String>>
//        Map<String, String> map = new HashMap<>();
//        if (keys != null&& !keys.isEmpty()) {
//            keys.forEach(key->{
//                String[] split = key.split(":");
//                String[] serviceNameVersionGroup = split[2].split("#");
//                map.put("serviceName", serviceNameVersionGroup[0]);
//                map.put("version", serviceNameVersionGroup[1]);
//                map.put("group", serviceNameVersionGroup[2]);
//            });
//        }
//        return map;
//    }


    private String getRedisKey(String applicationName, String serviceName, String group, String version){
        return "consumer:" + applicationName + ":" +getServiceKey(serviceName, group, version);
    }

    private String getServiceKey(String serviceName, String group, String version){
        return serviceName + "#" + version + "#" + group;
    }
}
