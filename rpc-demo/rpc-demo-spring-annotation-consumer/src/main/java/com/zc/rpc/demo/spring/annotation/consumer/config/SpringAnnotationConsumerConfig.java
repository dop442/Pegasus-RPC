package com.zc.rpc.demo.spring.annotation.consumer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@Configuration
@ComponentScan(value = {"com.zc.rpc.*"})
public class SpringAnnotationConsumerConfig {
}
