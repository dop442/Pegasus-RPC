package com.zc.rpc.demo.spring.annotation.provider;

import com.zc.rpc.demo.spring.annotation.provider.config.SpringAnnotationProviderConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-23
 */
public class SpringAnnotationProviderStarter {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(SpringAnnotationProviderConfig.class);
    }
}
