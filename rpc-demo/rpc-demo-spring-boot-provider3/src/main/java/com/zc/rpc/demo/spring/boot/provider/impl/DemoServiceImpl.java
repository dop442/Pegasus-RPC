package com.zc.rpc.demo.spring.boot.provider.impl;

import com.zc.rpc.annotation.RpcService;
import com.zc.rpc.demo.api.DemoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-09
 */
@Slf4j
@RpcService(interfaceClass = DemoService.class,
        interfaceClassName = "com.zc.rpc.demo.api.DemoService",
        group = "zc", weight = 30)
public class DemoServiceImpl implements DemoService {
    @Override
    public String hello(String name) {
        log.info("调用hello方法传入的参数为：{}", name);
        return "hello " + name;
    }

    @Override
    public int plus(int a, int b, int c) {
        return a+b+c;
    }
}
