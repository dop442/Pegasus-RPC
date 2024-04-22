package com.zc.rpc.test.provider.service.impl;

import com.zc.rpc.annotation.RpcService;
import com.zc.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
@RpcService(interfaceClass = DemoService.class,
        interfaceClassName = "com.zc.rpc.test.api.DemoService",
        group = "zc")
public class ProviderDemoServiceImpl implements DemoService {

    private final Logger LOGGER = LoggerFactory.getLogger(ProviderDemoServiceImpl.class);

    @Override
    public String hello(String name) {
        LOGGER.info("调用hello方法传入的参数为===>>>{}", name);
        return "hello " + name;
    }
}
