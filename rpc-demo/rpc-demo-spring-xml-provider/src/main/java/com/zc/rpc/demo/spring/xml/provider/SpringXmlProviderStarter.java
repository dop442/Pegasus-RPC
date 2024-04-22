package com.zc.rpc.demo.spring.xml.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-23
 */
public class SpringXmlProviderStarter {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("server-spring.xml");
    }
}
