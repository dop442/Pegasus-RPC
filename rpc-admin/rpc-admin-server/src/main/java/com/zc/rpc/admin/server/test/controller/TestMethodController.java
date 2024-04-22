package com.zc.rpc.admin.server.test.controller;

import com.zc.rpc.admin.bean.DTO.MethodListDTO;
import com.zc.rpc.admin.bean.DTO.TestResultDTO;
import com.zc.rpc.admin.server.test.service.TestMethodService;
import com.zc.rpc.admin.utils.constants.HttpCode;
import com.zc.rpc.admin.utils.resp.Result;
import com.zc.rpc.protocol.meta.ServiceMethodMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-06
 */

@RestController
public class TestMethodController {

    @Autowired
    private TestMethodService testMethodService;

    @PostMapping("/method")
    public Result<MethodListDTO> getMethodList(
            @RequestParam("serviceName") String serviceName,
            @RequestParam("group") String group,
            @RequestParam("version")String version) throws Exception {
        List<ServiceMethodMeta> methodList = testMethodService.getMethodList(serviceName, group, version);
        return new Result<>(HttpCode.SUCCESS, "成功", new MethodListDTO(methodList, methodList.size()));
    }

    @PostMapping("/method/test")
    public Result<TestResultDTO> testMethod(
            @RequestParam("serviceName") String serviceName,
            @RequestParam("group") String group,
            @RequestParam("version")String version,
            @RequestParam("parameters")String parameters,
            @RequestParam("parametersType")String[] parametersType,
            @RequestParam("returnType")String returnType,
            @RequestParam("methodName")String methodName
    ) throws Exception {
        String[] parametersSplit = parameters.split("#");
        TestResultDTO testResultDTO = testMethodService.testMethod(serviceName, group, version, parametersSplit, parametersType, returnType,methodName);
        if (testResultDTO==null) return new Result<>(HttpCode.FAILURE, "参数数量错误", null);
        return new Result<>(HttpCode.SUCCESS, "方法调用成功", testResultDTO);
    }





}
