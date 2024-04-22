package com.zc.rpc.demo.spring.boot.consumer;

import com.zc.rpc.demo.spring.boot.consumer.service.ConsumerProductService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-04
 */

@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = {"com.zc.rpc"})
public class SpringBootConsumerDemoStarter2 {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootConsumerDemoStarter2.class, args);
        ConsumerProductService consumerProductService = context.getBean(ConsumerProductService.class);
        String productName = "教父";
        boolean flag = consumerProductService.buyProduct(productName);
        log.info("《{}》购买：{}", productName, flag);
    }
}
