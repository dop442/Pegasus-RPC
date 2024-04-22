package com.zc.rpc.admin.server.test.service;

import com.zc.rpc.admin.bean.DTO.TestResultDTO;
import com.zc.rpc.admin.bean.POJO.MethodInfo;
import com.zc.rpc.protocol.meta.ServiceMethodMeta;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-07
 */
public interface TestMethodService {

    List<ServiceMethodMeta> getMethodList(String serviceName, String group, String version) throws Exception;


    TestResultDTO testMethod(String serviceName, String group, String version,
                             String[] parameters, String[] parametersType,
                             String returnType, String methodName) throws Exception;
}
