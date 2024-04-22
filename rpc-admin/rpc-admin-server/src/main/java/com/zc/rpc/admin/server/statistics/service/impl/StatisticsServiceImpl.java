package com.zc.rpc.admin.server.statistics.service.impl;

import com.zc.rpc.admin.bean.DTO.StatisticsProviderConsumerDTO;
import com.zc.rpc.admin.bean.DTO.StatisticsSerialization;
import com.zc.rpc.admin.server.discovery.service.ServerDiscoveryService;
import com.zc.rpc.admin.server.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-04
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ServerDiscoveryService serverDiscoveryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public StatisticsProviderConsumerDTO getConsumerProvider() throws Exception {
        int providerSize = serverDiscoveryService.getAllService().size();
        int consumerSize = redisTemplate.keys("consumer:*").size();
        return new StatisticsProviderConsumerDTO(providerSize, consumerSize);
    }

    @Override
    public StatisticsSerialization getSerialization() {
        Set<String> keys = redisTemplate.keys("consumer:*");
        int jdk = 0, hessian2=0, fst=0, json=0, kryo=0, protobuf=0, sum=0;
        for (String key : keys) {
            String serialization = (String) redisTemplate.opsForHash().get(key, "serialization");
            switch (serialization){
                case "jdk":
                    jdk++; sum++;
                    break;
                case "hessian2":
                    hessian2++; sum++;
                    break;
                case "fst":
                    fst++; sum++;
                    break;
                case "json":
                    json++; sum++;
                    break;
                case "kryo":
                    kryo++; sum++;
                    break;
                case "protostuff":
                    protobuf++; sum++;
                    break;
            }
        }
        return new StatisticsSerialization(jdk, hessian2, fst, json, kryo, protobuf, sum);
    }
}
