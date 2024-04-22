package com.zc.rpc.test.scanner.provider;


import com.zc.rpc.annotation.RpcService;
import com.zc.rpc.test.scanner.service.DemoService;

/**
 *@Description
 *@Author Schrodinger's Cobra
 *@Date 2024-01-17
 */
@RpcService(interfaceClass = DemoService.class,
        interfaceClassName = "com.zc.rpc.test.scanner.service.DemoService",
        group = "zc")
public class ProviderDemoServiceImpl implements DemoService {

}
