package com.zc.rpc.demo.spring.boot.consumer.service.impl;

import com.zc.rpc.annotation.RpcReference;
import com.zc.rpc.demo.api.DemoService;
import com.zc.rpc.demo.api.ProductService;
import com.zc.rpc.demo.spring.boot.consumer.service.ConsumerProductService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-04
 */
@Service
public class ConsumerProductServiceImpl implements ConsumerProductService {

    @RpcReference(registryType = "zookeeper", registryAddress = "192.168.217.130:2181",
            loadBalanceType = "zkconsistenthash", version = "1.0.0", group = "johnson",
            serializationType = "jdk", proxy = "cglib", timeout = 30000,
            async = false, oneway = false)
    private ProductService productService;


    @RpcReference(registryType = "zookeeper", registryAddress = "192.168.217.130:2181",
            loadBalanceType = "zkconsistenthash", version = "1.0.0", group = "zc",
            serializationType = "jdk", proxy = "cglib", timeout = 30000,
            async = false, oneway = false)
    private DemoService demoService;

    @Override
    public int queryStock(String productName) {
        return productService.queryStock(productName);
    }

    @Override
    public boolean buyProduct(String productName) {
        String result = demoService.hello("hhhhhhhhhcccccc");
        System.out.println(result);
        return productService.buyProduct(productName);
    }

}
