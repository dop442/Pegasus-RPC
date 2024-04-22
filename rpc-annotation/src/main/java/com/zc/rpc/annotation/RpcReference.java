package com.zc.rpc.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Schrodinger's Cobra
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {

    String version() default "1.0.0";

    String registryType() default "zookeeper";

    String registryAddress() default "127.0.0.1:2181";
    String loadBalanceType() default "zkconsistenthash";

    String serializationType() default "protostuff";

    long timeout() default 5000;

    boolean async() default false;

    boolean oneway() default false;

    String proxy() default "jdk";

    String group() default "";

    /**
     * 心跳间隔时间，默认30秒
     */
    int heartbeatInterval() default 30000;

    /**
     * 扫描空闲连接间隔时间，默认60秒
     */
    int scanNotActiveChannelInterval() default 60000;

    /**
     * 重试间隔时间
     */
    int retryInterval() default 1000;

    /**
     * 重试间隔时间
     */
    int retryTimes() default 3;

}
