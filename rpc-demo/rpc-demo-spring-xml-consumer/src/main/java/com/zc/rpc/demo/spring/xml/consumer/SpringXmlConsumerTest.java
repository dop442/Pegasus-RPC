package com.zc.rpc.demo.spring.xml.consumer;

import com.zc.rpc.consumer.RpcClient;
import com.zc.rpc.demo.api.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:client-spring.xml")
@Slf4j
public class SpringXmlConsumerTest {

    @Autowired
    private RpcClient rpcClient;

    @Test
    public void testInterfaceRpc() throws InterruptedException{
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("zczczc");
        log.info("返回的结果为：{}", result);
        while (true){
            Thread.sleep(1000);
        }
    }
}
