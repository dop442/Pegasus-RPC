package com.zc.rpc.test.spi.service;

import com.zc.rpc.spi.annotation.SPI;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-11
 */
@SPI("spiService")
public interface SPIService {
    String hello(String name);
}
