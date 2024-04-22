package com.zc.rpc.demo.spring.boot.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-09
 */
@SpringBootApplication
@ComponentScan(value = {"com.zc.rpc"})
public class SpringBootProviderDemoStarter3 {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootProviderDemoStarter3.class, args);
    }
}
