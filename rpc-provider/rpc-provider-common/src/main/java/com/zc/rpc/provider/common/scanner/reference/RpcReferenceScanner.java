package com.zc.rpc.provider.common.scanner.reference;

import com.zc.rpc.annotation.RpcReference;
import com.zc.rpc.provider.common.scanner.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public class RpcReferenceScanner extends ClassScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferenceScanner.class);

    public static Map<String, Object> doScannerWithRpcReferenceAnnotationFilter(
            String scanPackage
    ) throws Exception{
        Map<String, Object> handlerMap = new HashMap<>();
        List<String> classNameList = getClassNameList(scanPackage);
        if (classNameList==null || classNameList.isEmpty()) {
            return handlerMap;
        }
        classNameList.stream().forEach((className)->{
            try{
                Class<?> clazz = Class.forName(className);
                Field[] declaredFields = clazz.getDeclaredFields();
                Stream.of(declaredFields).forEach((field -> {
                    RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                    if (rpcReference!=null){
                        LOGGER.info("当前标注了@RpcReference注解的字段名称===>>>{}", clazz.getName());
                        LOGGER.info("@RpcReference注解上标注的属性信息如下：" +
                                        "version===>>>{}，" +
                                        "group===>>>{}，" +
                                        "registryType===>>>{}，" +
                                        "registryAddress===>>>{}，",
                                rpcReference.version(),
                                rpcReference.group(),
                                rpcReference.registryType(),
                                rpcReference.registryAddress());
                    }
                }));

            }catch (Exception e){
                LOGGER.error("scan classes throws exception:{}", e);
            }
        });
        return handlerMap;
    }


}
