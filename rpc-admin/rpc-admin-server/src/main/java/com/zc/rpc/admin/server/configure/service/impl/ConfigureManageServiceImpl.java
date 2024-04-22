package com.zc.rpc.admin.server.configure.service.impl;

import com.zc.rpc.admin.bean.POJO.ConsumerInfo;
import com.zc.rpc.admin.bean.exception.ConfigurationParsingException;
import com.zc.rpc.admin.bean.exception.ConfigureDuplicateException;
import com.zc.rpc.admin.server.configure.service.ConfigureManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-01
 */
@Service
public class ConfigureManageServiceImpl implements ConfigureManageService {

    private Map<String, String> defaultConfigure = new HashMap<>();

    {
        defaultConfigure.put("proxy", "cglib");
        defaultConfigure.put("loadbalance", "random");
        defaultConfigure.put("serialization", "jdk");
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void addConfigure(String configureName, String consumerName, String serviceName, String version, String group, String configure) {
        String consumerRedisKey = getConsumerRedisKey(consumerName, serviceName, group, version);
        List<String> configureNameList = getAllConfigureName();
        for (String s : configureNameList) {
            String consumerKey = (String) redisTemplate.opsForHash().get(s, "consumerKey");
            String serviceInfo = serviceName+"#"+version+"#"+group;
            if (consumerKey.equals(consumerRedisKey))
                throw new ConfigureDuplicateException(consumerName+"对应的"+serviceInfo+"已有配置："+s);
        }
        Map<String, String> consumerInfo = redisTemplate.opsForHash().entries(consumerRedisKey);
        Map<String, String> configureMap = parseString2Map(configure);
        configureMap.put("consumerKey", consumerRedisKey);
        String configureRedisKey = getConfigureRedisKey(configureName);
        redisTemplate.opsForHash().putAll(configureRedisKey, configureMap);
        configureMap.forEach((key, value) -> {
            if (consumerInfo.containsKey(key)) {
                key = key.replace("\r", "");
                redisTemplate.opsForHash().put(consumerRedisKey, key, value);
            }
        });
    }

    @Override
    public List<ConsumerInfo> getAllConsumerInfo() {
        Set<String> keys = redisTemplate.keys("consumer:*");
        List<ConsumerInfo> result = new ArrayList<>();
        // 根据key中的消费者名分类构成Map——key为消费者名，value为提供者信息（提供者名、版本、组名）
        Map<String, List<String>> keysSortByConsumerName = keys.stream().collect(Collectors.groupingBy(s -> {
            String[] keyInfo = s.split(":");
            return keyInfo[1];
        }, Collectors.mapping(s -> {
            String[] keyInfo = s.split(":");
            return keyInfo[2];
        }, Collectors.toList())));

        keysSortByConsumerName.forEach((consumerName, providerInfos) -> {
            ConsumerInfo consumerInfo = new ConsumerInfo(consumerName, providerInfos);
            result.add(consumerInfo);
        });
        return result;

    }

    @Override
    public void deleteConfigure(String configureName) {
        String configureRedisKey = getConfigureRedisKey(configureName);
        if (!redisTemplate.hasKey(configureRedisKey)) return;
        String consumerKey = (String) redisTemplate.opsForHash().get(configureRedisKey, "consumerKey");
        redisTemplate.delete(configureRedisKey);
        defaultConfigure.forEach((key, value)->{
            redisTemplate.opsForHash().put(consumerKey, key, value);
        });
    }

    @Override
    public Map<String, String> getConfigure(String configureName) {
        String configureRedisKey = getConfigureRedisKey(configureName);
        Map<String, String> configureMap;
        if (redisTemplate.hasKey(configureRedisKey)) {
            configureMap = redisTemplate.opsForHash().entries(configureRedisKey);
            configureMap.forEach((key, value)->{
                value = value.replace("\r", "");
                configureMap.put(key, value);
            });
        } else {
            configureMap = null;
        }
        return configureMap;
    }

    @Override
    public void updateConfigure(String configureName, String configure) {
        String configureRedisKey = getConfigureRedisKey(configureName);
        String consumerKey = (String) redisTemplate.opsForHash().get(configureRedisKey, "consumerKey");
        Map<String, String> configureMap = parseString2Map(configure);

        Map<String, String> configureMapInRedis = redisTemplate.opsForHash().entries(configureRedisKey);
        configureMapInRedis.forEach((k, v)->{
            if (!k.equals("consumerKey")){
                redisTemplate.opsForHash().delete(configureRedisKey, k);
            }
        });
        configureMap.forEach((key,value)-> {
            redisTemplate.opsForHash().put(configureRedisKey, key, value);
            if (redisTemplate.opsForHash().hasKey(consumerKey, key)){
                value = value.replace("\r", "");
                redisTemplate.opsForHash().put(consumerKey, key, value);
            }
        });
    }

    @Override
    public List<String> getAllConfigureName() {
        Set<String> keys = redisTemplate.keys("configure:*");
        return new ArrayList<>(keys);
    }

    private Map<String, String> parseString2Map(String configure) {
        try {
            Map<String, String> map = new HashMap<>();
            String[] configureLines = configure.split("\n");
            for (String configureLine : configureLines) {
                String[] parts = configureLine.split(":");
                map.put(parts[0], parts[1]);
            }
            return map;
        } catch (Exception e) {
            throw new ConfigurationParsingException("配置解析异常，请检查配置填写格式");
        }
    }

    /**
     * @param configureName
     * @Description 获取“用户创建的配置”的key
     */
    private String getConfigureRedisKey(String configureName) {
        return "configure:" + configureName;
    }


    /**
     * @Description 获取消费者的key
     */
    private String getConsumerRedisKey(String applicationName, String serviceName, String group, String version) {
        return "consumer:" + applicationName + ":" + serviceName + "#" + version + "#" + group;
    }


}
