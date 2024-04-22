package com.zc.rpc.registry.zookeeper;

import com.zc.rpc.common.helper.RpcServerHelper;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.loadbalancer.api.ServiceLoadBalancer;
import com.zc.rpc.loadbalancer.helper.ServiceLoadBalancerHelper;
import com.zc.rpc.protocol.meta.ServiceInfo;
import com.zc.rpc.protocol.meta.ServiceMeta;
import com.zc.rpc.registry.api.RegistryService;
import com.zc.rpc.registry.api.config.RegistryConfig;
import com.zc.rpc.spi.loader.ExtensionLoader;
import lombok.NoArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-08
 */
@NoArgsConstructor
public class ZookeeperRegistryServiceImpl implements RegistryService {

    public CuratorFramework client;
    private static final Logger log = LoggerFactory.getLogger(ZookeeperRegistryServiceImpl.class);
    public static final int BASE_SLEEP_TIME_MS = 1000;

    public static final int MAX_RETRIES = 3;
    public static final String ZK_BASE_PATH = "/zc_rpc/service";
    private ServiceDiscovery<ServiceMeta> serviceDiscovery;
    private ServiceLoadBalancer<ServiceInstance<ServiceMeta>> serviceLoadBalancer;
    private ServiceLoadBalancer<ServiceMeta> serviceEnhancedLoadBalancer;

    @Override
    public void registry(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance.<ServiceMeta>builder()
                .name(RpcServerHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup()))
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceInstance);
        log.info("服务 {} 注册成功，ip：{}， port：{}", serviceMeta.getServiceName(), serviceMeta.getServiceAddr(), serviceMeta.getServicePort());
    }

    @Override
    public void unRegistry(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance.<ServiceMeta>builder()
                .name(serviceMeta.getServiceName())
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
        log.info("服务 {} 注销", serviceMeta.getServiceName());
    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode, String sourceIp) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        ServiceMeta meta;
        if (serviceLoadBalancer != null) {

            meta = getServiceMetaInstance((List<ServiceInstance<ServiceMeta>>) serviceInstances, invokerHashCode, sourceIp);
        }else {
            List<ServiceMeta> servers = ServiceLoadBalancerHelper.getServiceMetaList((List<ServiceInstance<ServiceMeta>>) serviceInstances);
            List<ServiceMeta> serviceMetaList = new ArrayList<>();
            servers.stream().forEach((server)->
                    IntStream.range(0, server.getWeight()).forEach((i)->
                            serviceMetaList.add(server)));
            meta = serviceEnhancedLoadBalancer.select(serviceMetaList,invokerHashCode,sourceIp);
        }
        if (meta != null) {
            log.info("基于负载均衡策略选择了节点====>>服务名：{}，ip：{}，port：{}", serviceName, meta.getServiceAddr(), meta.getServicePort());
            return meta;
        }

        return null;
    }

    /**
     * @param serviceInstances 节点集合
     * @return 节点对应的ServiceMeta信息
     * @Description 通过普通的负载均衡策略选择节点 获取节点对应的ServiceMeta信息
     */
    private ServiceMeta getServiceMetaInstance(List<ServiceInstance<ServiceMeta>> serviceInstances, int invokerHashCode, String sourceIp) {

        List<ServiceInstance<ServiceMeta>> servers = new ArrayList<>();
        serviceInstances.forEach(serviceMetaServiceInstance -> {
            IntStream.range(0, serviceMetaServiceInstance.getPayload().getWeight()).forEach(i->
                    servers.add(serviceMetaServiceInstance));
        });

        ServiceInstance<ServiceMeta> instance = serviceLoadBalancer.select(servers, invokerHashCode, sourceIp);
        if (instance != null) {
            return instance.getPayload();
        }
        return null;
    }

    @Override
    public void destroy() throws IOException {
        serviceDiscovery.close();
    }

    @Override
    public void init(RegistryConfig registryConfig) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryConfig.getRegistryAddr(), new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
        this.client = client;
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
        this.serviceDiscovery.start();
        if (registryConfig.getRegistryLoadBalanceType().toLowerCase().contains(RpcConstants.SERVICE_ENHANCED_LOAD_BALANCER_PREFIX)) {
            this.serviceEnhancedLoadBalancer = ExtensionLoader.getExtension(ServiceLoadBalancer.class, registryConfig.getRegistryLoadBalanceType());
        } else {
            this.serviceLoadBalancer = ExtensionLoader.getExtension(ServiceLoadBalancer.class, registryConfig.getRegistryLoadBalanceType());
        }
    }

    /**
     * @param serviceName 服务名
     * @Description Admin--获取该服务名的所有节点信息
     */
    @Override
    public List<ServiceInfo> discoverySimple(String serviceName) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        List<ServiceInfo> serviceInfoList = new ArrayList<>();
        for (ServiceInstance<ServiceMeta> serviceInstance : serviceInstances) {
            ServiceInfo serviceInfo = new ServiceInfo();
            ServiceMeta serviceMeta = new ServiceMeta();
            BeanUtils.copyProperties(serviceInstance.getPayload(), serviceMeta);
            serviceInfo.id = serviceInstance.getId();
            serviceInfo.serviceMeta = serviceMeta;
            serviceInfoList.add(serviceInfo);
        }
        return serviceInfoList;
    }

    @Override
    public void unRegistrySimple(String serviceKey, String id) throws Exception {
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        String serviceBasePath = "/zc_rpc/service/"+serviceKey;
        String methodBasePath = "/zc_rpc/method";
        List<String> servicePathList = client.getChildren().forPath(serviceBasePath);
        List<String> methodPathList = client.getChildren().forPath(methodBasePath);
        for (String path : servicePathList) {
            String servicePath = serviceBasePath+"/"+ path;
            ServiceInstance<ServiceMeta> serviceInstance = serializer.deserialize(client.getData().forPath(servicePath));
            if (serviceInstance.getId().equals(id)){
                client.delete().forPath(servicePath);
            }
        }
    }

    @Override
    public void updateNodeWeight(String serviceKey, String id, int weight) throws Exception {
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        String serviceBasePath = "/zc_rpc/service/"+serviceKey;
        List<String> servicePathList = client.getChildren().forPath(serviceBasePath);
        for (String path : servicePathList) {
            String servicePath = serviceBasePath+"/"+ path;
            ServiceInstance<ServiceMeta> serviceInstance = serializer.deserialize(client.getData().forPath(servicePath));
            if (serviceInstance.getId().equals(id)){
                ServiceMeta serviceMeta = serviceInstance.getPayload();
                serviceMeta.setWeight(weight);
                client.setData().forPath(servicePath, serializer.serialize(serviceInstance));
            }
        }

    }


}
