package com.zc.rpc.demo.spring.annotation.consumer.service.impl;

import com.zc.rpc.annotation.RpcReference;
import com.zc.rpc.demo.api.DemoService;
import com.zc.rpc.demo.spring.annotation.consumer.service.ConsumerDemoService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@Service
public class ConsumerDemoServiceImpl implements ConsumerDemoService {

    @RpcReference(registryAddress = "192.168.217.130:2181", loadBalanceType = "random", group = "ManchesterCity", timeout = 30000)
    private DemoService demoService;

    @Override
    public String hello(String name) {
        return demoService.hello(name);
    }
}
