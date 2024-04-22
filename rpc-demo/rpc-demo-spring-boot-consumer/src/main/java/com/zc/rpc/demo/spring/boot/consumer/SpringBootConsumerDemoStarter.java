package com.zc.rpc.demo.spring.boot.consumer;

import com.zc.rpc.demo.spring.boot.consumer.service.ConsumerDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.zc.rpc"})
public class SpringBootConsumerDemoStarter {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootConsumerDemoStarter.class, args);
        ConsumerDemoService consumerDemoService = context.getBean(ConsumerDemoService.class);
        String result = consumerDemoService.hello("zc");
        log.info("调用hello的结果：{}", result);
        int plus = consumerDemoService.plus(2, 4, 6);
        log.info("调用plus的结果：{}", plus);
    }
}
