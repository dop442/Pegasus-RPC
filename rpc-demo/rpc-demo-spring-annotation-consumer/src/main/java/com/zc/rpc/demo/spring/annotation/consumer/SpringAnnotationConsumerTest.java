package com.zc.rpc.demo.spring.annotation.consumer;

import com.zc.rpc.demo.spring.annotation.consumer.config.SpringAnnotationConsumerConfig;
import com.zc.rpc.demo.spring.annotation.consumer.service.ConsumerDemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@Slf4j
public class SpringAnnotationConsumerTest {

    @Test
    public void testInterfaceRpc(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringAnnotationConsumerConfig.class);
        ConsumerDemoService consumerDemoService = context.getBean(ConsumerDemoService.class);
        String result = consumerDemoService.hello("zczczczczczc");
        log.info("返回的结果是：{}", result);
    }
}
