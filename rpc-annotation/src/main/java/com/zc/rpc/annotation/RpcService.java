package com.zc.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Schrodinger's Cobra
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    Class<?> interfaceClass() default void.class;
    String interfaceClassName() default "";

    String version() default "1.0.0";

    String group() default "";

    int weight() default 0;
}
