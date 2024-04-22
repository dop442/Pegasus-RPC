package com.zc.rpc.demo.spring.boot.consumer.service;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-04
 */
public interface ConsumerProductService {

    int queryStock(String productName);

    boolean buyProduct(String productName);


}
