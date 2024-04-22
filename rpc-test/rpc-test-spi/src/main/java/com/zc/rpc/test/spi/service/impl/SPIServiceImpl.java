package com.zc.rpc.test.spi.service.impl;

import com.zc.rpc.spi.annotation.SPIClass;
import com.zc.rpc.test.spi.service.SPIService;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-11
 */
@SPIClass
public class SPIServiceImpl implements SPIService {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
