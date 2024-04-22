package com.zc.rpc.demo.spring.boot.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@SpringBootApplication
@ComponentScan(value = {"com.zc.rpc"})
public class SpringBootProviderDemoStarter {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootProviderDemoStarter.class, args);
    }
}
