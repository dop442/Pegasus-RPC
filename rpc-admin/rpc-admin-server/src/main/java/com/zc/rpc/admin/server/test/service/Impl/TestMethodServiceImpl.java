package com.zc.rpc.admin.server.test.service.Impl;

import com.zc.rpc.admin.bean.DTO.TestResultDTO;
import com.zc.rpc.admin.bean.exception.ParametersCountException;
import com.zc.rpc.admin.server.test.service.TestMethodService;
import com.zc.rpc.admin.server.test.utils.ParameterUtil;
import com.zc.rpc.admin.server.test.utils.ZookeeperMethodDiscoveryService;
import com.zc.rpc.admin.server.test.utils.methodTest.TestMethodClient;
import com.zc.rpc.common.helper.RpcServerHelper;
import com.zc.rpc.protocol.meta.ServiceMethodMeta;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-07
 */
@Service
public class TestMethodServiceImpl implements TestMethodService {

    @Value("${admin-server.discovery.registryAddr}")
    private String registryAddr;


    /**
     * @param serviceName 服务名
     * @param group       组名
     * @param version     版本
     * @return 该服务的所有方法
     * @Description 获取该服务的所有方法信息
     */
    @Override
    public List<ServiceMethodMeta> getMethodList(String serviceName, String group, String version) throws Exception {
        ZookeeperMethodDiscoveryService methodDiscoveryService = new ZookeeperMethodDiscoveryService();
        String serviceKey = RpcServerHelper.buildServiceKey(serviceName, version, group);
        methodDiscoveryService.init(registryAddr, serviceKey);
        List<ServiceMethodMeta> list = methodDiscoveryService.getAllMethodMetaInService();
        return list.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public TestResultDTO testMethod(String serviceName, String group, String version, String[] parameters, String[] parametersType, String returnType, String methodName) throws Exception {
        if (parameters.length != parametersType.length) {
            throw new ParametersCountException("需提供" + parametersType.length + "个参数");
        }
        if (!parametersVerify(parameters, parametersType)){
            return new TestResultDTO(returnType, "无结果");
        }
        ZookeeperMethodDiscoveryService methodDiscoveryService = new ZookeeperMethodDiscoveryService();
        String serviceKey = RpcServerHelper.buildServiceKey(serviceName, version, group);
        methodDiscoveryService.init(registryAddr, serviceKey);
        ServiceInstance<ServiceMethodMeta> methodInfo = methodDiscoveryService.getAllMethodInfoInService().stream().filter(sm -> sm.getPayload().getMethodName().equals(methodName)).findFirst().get();
        TestMethodClient client = new TestMethodClient(methodInfo.getAddress(), methodInfo.getPort(), serviceName, group, version, methodName, parameters, parametersType);
        Object result = client.testMethod();
        if (result != null) return new TestResultDTO(returnType, result.toString());
        return new TestResultDTO(returnType, "无结果");
    }

    private boolean parametersVerify(String[] parameters, String[] parametersType) throws ClassNotFoundException {
        try {
            for (int i = 0; i < parametersType.length; i++) {
                ParameterUtil.stringMapParameter(parametersType[i], parameters[i]);
            }
            return true;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("参数错误，应当输入数字");
        } catch (ClassNotFoundException e){
            throw new ClassNotFoundException("未找到对应类");
        }


    }



}
