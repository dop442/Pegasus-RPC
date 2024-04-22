package com.zc.rpc.provider.spring;

import com.zc.rpc.annotation.RpcService;
import com.zc.rpc.common.helper.RpcServerHelper;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.protocol.meta.ServiceMeta;
import com.zc.rpc.protocol.meta.ServiceMethodMeta;
import com.zc.rpc.provider.common.server.base.BaseServer;
import com.zc.rpc.registry.api.config.RegistryConfig;
import com.zc.rpc.registry.zookeeper.ZookeeperRegistryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-23
 */
@Slf4j
public class RpcSpringServer extends BaseServer implements ApplicationContextAware, InitializingBean {

    public static final int BASE_SLEEP_TIME_MS = 1000;

    public static final int MAX_RETRIES = 3;

    private CuratorFramework client;

    private JsonInstanceSerializer<ServiceMethodMeta> serializer;
    public RpcSpringServer(String serverAddress, String registryAddress, String registryType, String registryLoadBalanceType, String reflectType, int heartbeatInterval, int scanNotActiveChannelInterval) {
        super(serverAddress, registryAddress, registryType, reflectType, registryLoadBalanceType, heartbeatInterval, scanNotActiveChannelInterval);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.startNettyServer();
    }

    /**
     * @Description 在当前bean（RpcSpringServer）被初始化的的时候执行
     * （通过xml扫包或者@ComponentScan扫包的时候把当前类RpcSpringServer扫入）
     * 在提供者端，通过Spring扫描所有被@RpcService标注的类
     * 1. 放到handlerMap中
     * 2. 将其信息注册到注册中心中
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        initMethodRegistryService();
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                ServiceMeta serviceMeta = new ServiceMeta(this.getServiceName(rpcService), rpcService.version(), rpcService.group(), host, port, getWeight(rpcService.weight()));
                handlerMap.put(RpcServerHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup()), serviceBean);
                List<Method> methods = filter(serviceBean);
                try {
                    registryService.registry(serviceMeta);
                    registryMethod(serviceMeta, methods);
                } catch (Exception e) {
                    log.error("RPC服务端初始化Spring异常：", e);
                }
            }
        }
    }

    /**
     * @Description 初始化服务注册（初始化与Zookeeper的连接）
     */
    private void initMethodRegistryService() {
        client = CuratorFrameworkFactory.newClient(super.registryAddress, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
        serializer = new JsonInstanceSerializer<>(ServiceMethodMeta.class);
    }

    /**
     * @Description 获取权重
     */
    private int getWeight(int weight) {
        if (weight < RpcConstants.SERVICE_WEIGHT_MIN) {
            weight = RpcConstants.SERVICE_WEIGHT_MIN;
        }
        if (weight > RpcConstants.SERVICE_WEIGHT_MAX) {
            weight = RpcConstants.SERVICE_WEIGHT_MAX;
        }
        return weight;
    }

    /**
     * @param rpcService @RpcService注解信息
     * @return 被@RpcService标注的服务类的类名
     * @Description 获取被@RpcService标注的服务类的类名
     */
    private String getServiceName(RpcService rpcService) {
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


    /**
     * @return 过滤后的方法——这个类真正的方法
     * @Description 获取一个类的方法（过滤掉Object方法）
     */
    private List<Method> filter(Object clazz){
        List<Method> methodList = new ArrayList<>();
        List<String> objectMethod = Arrays.stream(Object.class.getDeclaredMethods()).map(Method::getName).collect(Collectors.toList());
        Arrays.stream(clazz.getClass().getMethods()).forEach(method -> {
            if (!objectMethod.contains(method.getName())){
                methodList.add(method);
            }
        });
        return methodList;
    }

    /**
     * @param serviceMeta 服务信息
     * @param methods 方法列表
     * @Description 将方法注册到服务下的节点
     */
    private void registryMethod(ServiceMeta serviceMeta, List<Method> methods) throws Exception {
        String serviceKey = RpcServerHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup());
        ServiceDiscovery<ServiceMethodMeta> serviceMethodDiscovery = ServiceDiscoveryBuilder
                .builder(ServiceMethodMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath("/zc_rpc/method/")
                .build();
        serviceMethodDiscovery.start();

        for (Method method : methods) {
            List<String> parameterTypeStringList = new ArrayList<>();
            Class<?>[] parameterTypes = method.getParameterTypes();
            String returnTypeString = method.getReturnType().getName();
            Arrays.stream(parameterTypes).forEach(p-> parameterTypeStringList.add(p.getName()));
            ServiceMethodMeta serviceMethodMeta = new ServiceMethodMeta(method.getName(), method.getParameterCount(), parameterTypeStringList, returnTypeString);
            ServiceInstance<ServiceMethodMeta> serviceInstance = ServiceInstance.<ServiceMethodMeta>builder()
                    .port(serviceMeta.getServicePort())
                    .address(serviceMeta.getServiceAddr())
                    .name(serviceKey)
                    .payload(serviceMethodMeta).build();
            serviceMethodDiscovery.registerService(serviceInstance);
        }
    }

}
