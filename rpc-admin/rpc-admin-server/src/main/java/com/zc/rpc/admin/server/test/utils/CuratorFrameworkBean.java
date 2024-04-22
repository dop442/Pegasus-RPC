package com.zc.rpc.admin.server.test.utils;

import com.zc.rpc.protocol.meta.ServiceMeta;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-30
 */
@Configuration
public class CuratorFrameworkBean {

    @Value("${admin-server.discovery.registryAddr}")
    private String registryAddr;

    @Bean
    public ServiceDiscovery<ServiceMeta> serviceDiscovery(){
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(1000, 3));
        client.start();
        return ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(new JsonInstanceSerializer<>(ServiceMeta.class))
                .basePath("/zc_rpc/service")
                .build();
    }
}
