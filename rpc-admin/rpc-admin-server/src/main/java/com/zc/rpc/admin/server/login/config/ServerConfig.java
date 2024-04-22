package com.zc.rpc.admin.server.login.config;

import com.zc.rpc.registry.api.RegistryService;
import com.zc.rpc.registry.api.config.RegistryConfig;
import com.zc.rpc.registry.zookeeper.ZookeeperRegistryServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-06
 */
@Configuration
public class ServerConfig {

    @Value("${admin-server.discovery.registryAddr}")
    private String registryAddr;

    @Value("${admin-server.discovery.registryType}")
    private String registryType;

    @Value("${admin-server.discovery.registryLoadBalance}")
    private String registryLoadBalance;

    @Bean
    public ZookeeperRegistryServiceImpl zookeeperRegistryService(){
        ZookeeperRegistryServiceImpl zookeeperRegistryService = new ZookeeperRegistryServiceImpl();
        try {
            zookeeperRegistryService.init(new RegistryConfig(registryAddr, registryType, registryLoadBalance));
        } catch (Exception e) {
            throw new RuntimeException("连接注册中心失败");
        }
        return zookeeperRegistryService;
    }

}
