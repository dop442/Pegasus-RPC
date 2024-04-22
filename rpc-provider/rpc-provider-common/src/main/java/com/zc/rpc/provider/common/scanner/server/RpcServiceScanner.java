package com.zc.rpc.provider.common.scanner.server;

import com.zc.rpc.annotation.RpcService;
import com.zc.rpc.common.helper.RpcServerHelper;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.protocol.meta.ServiceMeta;
import com.zc.rpc.provider.common.scanner.ClassScanner;
import com.zc.rpc.registry.api.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Schrodinger's Cobra
 * @Description @RpcService注解扫描类
 */
public class RpcServiceScanner extends ClassScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceScanner.class);

    public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService(
            String host, int port, String scanPackage, RegistryService registryService
    ) throws Exception {
        Map<String, Object> handlerMap = new HashMap<>();
        List<String> classNameList = getClassNameList(scanPackage);
        if (classNameList == null || classNameList.isEmpty()) {
            return handlerMap;
        }
        classNameList.stream().forEach((className) -> {
            try {
                Class<?> clazz = Class.forName(className);
                RpcService rpcService = clazz.getAnnotation(RpcService.class);
                if (rpcService != null) {
                    //优先使用interfaceClass, interfaceClass的name为空，再使用interfaceClassName
                    ServiceMeta serviceMeta = new ServiceMeta(getServiceName(rpcService), rpcService.version(), rpcService.group(), host, port, getWeight(rpcService.weight()));
                    //将服务的元数据注册到注册中心
                    registryService.registry(serviceMeta);
                    handlerMap.put(RpcServerHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup()), clazz.newInstance());
                }
            } catch (Exception e) {
                LOGGER.error("scan classes throws exception: {}", e.toString());
            }
        });
        return handlerMap;
    }


    private static String getServiceName(RpcService rpcService) {
        Class<?> clazz = rpcService.interfaceClass();
        if (clazz == void.class) {
            return rpcService.interfaceClassName();
        }
        String serviceName = clazz.getName();
        if (serviceName == null || serviceName.trim().isEmpty()) {
            serviceName = rpcService.interfaceClassName();
        }
        return serviceName;
    }

    private static int getWeight(int weight){
        if (weight< RpcConstants.SERVICE_WEIGHT_MIN) {
            weight = RpcConstants.SERVICE_WEIGHT_MIN;
        }
        if (weight> RpcConstants.SERVICE_WEIGHT_MAX) {
            weight = RpcConstants.SERVICE_WEIGHT_MAX;
        }
        return weight;
    }



}
