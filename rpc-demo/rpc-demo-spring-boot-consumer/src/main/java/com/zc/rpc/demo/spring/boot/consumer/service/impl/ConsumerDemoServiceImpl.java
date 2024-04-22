package com.zc.rpc.demo.spring.boot.consumer.service.impl;

import com.zc.rpc.annotation.RpcReference;
import com.zc.rpc.demo.api.DemoService;
import com.zc.rpc.demo.spring.boot.consumer.service.ConsumerDemoService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@Service
public class ConsumerDemoServiceImpl implements ConsumerDemoService {

    @RpcReference(registryType = "zookeeper", registryAddress = "192.168.217.130:2181",
            loadBalanceType = "zkconsistenthash", version = "1.0.0", group = "zc",
            serializationType = "jdk", proxy = "cglib", timeout = 30000,
            async = false, oneway = false)
    private DemoService demoService;
    @Override
    public String hello(String name) {
        return demoService.hello(name);
    }
    @Override
    public int plus(int a, int b, int c) {
        return demoService.plus(a, b, c);
    }
}


















