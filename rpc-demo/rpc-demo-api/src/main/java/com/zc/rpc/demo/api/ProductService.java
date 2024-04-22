package com.zc.rpc.demo.api;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-04
 */
public interface ProductService {

    int queryStock(String productName);

    boolean buyProduct(String productName);

}
