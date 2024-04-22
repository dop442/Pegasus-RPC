package com.zc.rpc.spring.boot.consumer.starter;

import com.zc.rpc.consumer.RpcClient;
import com.zc.rpc.spring.boot.consumer.config.SpringBootConsumerConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@Configuration
@EnableConfigurationProperties
public class SpringBootConsumerAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "rpc.zc.consumer")
    public SpringBootConsumerConfig springBootConsumerConfig(){
        return new SpringBootConsumerConfig();
    }

    @Bean
    public RpcClient rpcClient(final SpringBootConsumerConfig springBootConsumerConfig){
        return new RpcClient(springBootConsumerConfig.getVersion(),
                springBootConsumerConfig.getGroup(),
                springBootConsumerConfig.getSerializationType(),
                springBootConsumerConfig.getTimeout(),
                springBootConsumerConfig.isAsync(),
                springBootConsumerConfig.isOneway(),
                springBootConsumerConfig.getRegistryAddress(),
                springBootConsumerConfig.getRegistryType(),
                springBootConsumerConfig.getLoadBalanceType(),
                springBootConsumerConfig.getHeartbeatInterval(),
                springBootConsumerConfig.getScanNotActiveChannelInterval(),
                springBootConsumerConfig.getRetryInterval(),
                springBootConsumerConfig.getRetryTimes(),
                springBootConsumerConfig.getProxy());
    }
}
