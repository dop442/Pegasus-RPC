package com.zc.rpc.demo.spring.xml.provider.impl;

import com.zc.rpc.annotation.RpcService;
import com.zc.rpc.demo.api.DemoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-23
 */
@Slf4j
@RpcService(interfaceClass = DemoService.class, interfaceClassName = "com.zc.rpc.test.api.DemoService",version = "1.0.0",group = "zc", weight = 2)
public class ProviderDemoServiceImpl implements DemoService {
    @Override
    public String hello(String name) {
        log.info("调用hello方法传入的参数为===>>>{}", name);
        return "hello "+name;
    }
}
