package com.zc.rpc.test.scanner.consumer.service.impl;

import com.zc.rpc.annotation.RpcReference;
import com.zc.rpc.test.scanner.consumer.service.ConsumerBusinessService;
import com.zc.rpc.test.scanner.service.DemoService;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public class ConsumerBusinessServiceImpl implements ConsumerBusinessService {
    @RpcReference(group = "zc")
    private DemoService demoService;
}
