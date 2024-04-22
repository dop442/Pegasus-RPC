package com.zc.rpc.consumer.spring;

import com.zc.rpc.annotation.RpcReference;
import com.zc.rpc.constants.RpcConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description 解析@RpcReference注解
 * @Author Schrodinger's Cobra
 * @Date 2024-02-23
 */
@Slf4j
@Component
public class RpcConsumerPostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {


    private ApplicationContext context;

    private ClassLoader classLoader;

    private String applicationName;

    private String serverAddr;

    private String serverPort;

    private final Map<String, BeanDefinition> rpcRefBeanDefinitions = new LinkedHashMap<>();

    private String redisAddress;

    /**
     * @Description 获取Spring的上下文 获取Environment的数据
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        Environment environment = applicationContext.getEnvironment();
        applicationName = environment.getProperty("spring.application.name");
        serverPort = environment.getProperty("server.port");
        try {
            serverAddr = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        redisAddress = environment.getProperty("rpc.zc.consumer.redis-address");
    }

    /**
     * @param classLoader 获取Spring容器中，bean的类加载器
     */
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * @param beanFactory bean工厂
     * @Description 从bean工厂中获取所有bean的信息，如果bean被@RpcReference标注，就注册到Spring的IOC容器中
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                if (beanClassName.equals("springBootConsumerConfig")){
                    MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
                    redisAddress = propertyValues.getPropertyValue("redisAddress").getValue().toString();
                }
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, classLoader);
                ReflectionUtils.doWithFields(clazz, this::parseRpcReference);
            }
        }
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        rpcRefBeanDefinitions.forEach((beanName, beanDefinition)->{
            if (context.containsBean(beanName)) {
                throw new IllegalArgumentException("Spring 容器中已经存在一个相同名字的bean——{}"+beanName);
            }
            registry.registerBeanDefinition(beanName, rpcRefBeanDefinitions.get(beanName));
            log.info("被@RpcReference标注的类:{} 在spring中作为bean，成功注册到Spring的IOC容器中", beanName);
        });
    }

    /**
     * @param field 工厂bean中，某个类的所有属性
     * @Description 如果该类的所有属性中包括@RpcReference注解（被@RpcReference标记），就放入Map--rpcRefBeanDefinitions中
     */
    private void parseRpcReference(Field field) {
        RpcReference annotation = AnnotationUtils.getAnnotation(field, RpcReference.class);
        if (annotation != null) {
            String[] split = redisAddress.split(":");
            String address = split[0];
            int port = Integer.parseInt(split[1]);
            Jedis jedis = new Jedis(address, port);
            String key = getRedisKey(annotation, field);
            Map<String, String> propertiesMap = jedis.hgetAll(key);
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            builder.setInitMethodName(RpcConstants.INIT_METHOD_NAME);
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("version", annotation.version());
            builder.addPropertyValue("registryType", annotation.registryType());
            builder.addPropertyValue("registryAddress", annotation.registryAddress());
            builder.addPropertyValue("group", annotation.group());
            builder.addPropertyValue("oneway", annotation.oneway());
            builder.addPropertyValue("scanNotActiveChannelInterval", annotation.scanNotActiveChannelInterval());
            builder.addPropertyValue("heartbeatInterval", annotation.heartbeatInterval());
            builder.addPropertyValue("retryInterval", annotation.retryInterval());
            builder.addPropertyValue("retryTimes", annotation.retryTimes());
            builder.addPropertyValue("timeout", annotation.timeout());
            builder.addPropertyValue("async", annotation.async());
            if (propertiesMap!=null && propertiesMap.size()>0){
                builder.addPropertyValue("loadBalanceType", propertiesMap.get("loadbalance"));
                builder.addPropertyValue("serializationType", propertiesMap.get("serialization"));
                builder.addPropertyValue("proxy", propertiesMap.get("proxy"));
            }else {
                builder.addPropertyValue("loadBalanceType", annotation.loadBalanceType());
                builder.addPropertyValue("serializationType", annotation.serializationType());
                builder.addPropertyValue("proxy", annotation.proxy());
            }

            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcRefBeanDefinitions.put(field.getName(), beanDefinition);
            putConsumerInfoToRedis(annotation, field);
        }
    }

    /**
     * @param annotation @RpcReference注解
     * @param field 被@RpcReference标注的服务接口
     * @Description 把消费者的信息放到Redis中
     */
    private void putConsumerInfoToRedis(RpcReference annotation, Field field){
        Map<String, String> map = new HashMap<>();
        String[] split = redisAddress.split(":");
        String address = split[0];
        int port = Integer.parseInt(split[1]);
        Jedis jedis = new Jedis(address, port);
        String key = getRedisKey(annotation, field);
        map.put("consumerAddr", serverAddr);
        map.put("consumerPort", serverPort);
        map.put("consumerApplicationName", applicationName);
        map.put("providerInterfaceClassName", field.getType().toString().replace("interface ", ""));
        map.put("registryAddress", annotation.registryAddress());
        map.put("heartBeatInterval", String.valueOf(annotation.heartbeatInterval()));
        map.put("proxy", annotation.proxy());
        map.put("loadbalance", annotation.loadBalanceType());
        map.put("async", String.valueOf(annotation.async()));
        map.put("serialization",annotation.serializationType());
        jedis.hset(key, map);
    }

    private String getRedisKey(RpcReference annotation, Field field){
        return "consumer:"+applicationName+":"+field.getType().toString().replace("interface ", "")+"#"+annotation.version()+"#"+annotation.group();
    }
}
