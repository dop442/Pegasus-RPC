package com.zc.rpc.demo.spring.boot.consumer.service;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
public interface ConsumerDemoService {
    String hello(String name);

    int plus(int a, int b, int c);
}
