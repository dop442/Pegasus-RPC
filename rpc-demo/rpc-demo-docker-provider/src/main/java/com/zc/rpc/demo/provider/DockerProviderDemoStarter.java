package com.zc.rpc.demo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-25
 */
@SpringBootApplication
@ComponentScan(value = "com.zc.rpc")
public class DockerProviderDemoStarter {
    public static void main(String[] args) {
        SpringApplication.run(DockerProviderDemoStarter.class, args);
    }
}
