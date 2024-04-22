package com.zc.rpc.admin.server.test.utils;

import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-13
 */
@Component
public class ParameterUtil {
    public static Object stringMapParameter(String classType, String parameter) throws ClassNotFoundException, NumberFormatException {

        switch (classType) {
            case "java.lang.String":
                return parameter;
            case "int":
                return Integer.valueOf(parameter);
            case "byte":
                return Byte.valueOf(parameter);
            case "short":
                return Short.valueOf(parameter);
            case "long":
                return Long.valueOf(parameter);
            case "float":
                return Float.valueOf(parameter);
            case "double":
                return Double.valueOf(parameter);
            case "char":
                return parameter.charAt(0);
            case "boolean":
                return Boolean.valueOf(parameter);
            default:
                throw new ClassNotFoundException("找不到类：" + classType);
        }

    }

    public static Class stringMapParameterType(String type) throws ClassNotFoundException {
        switch (type) {
            case "java.lang.String":
                return String.class;
            case "int":
                return int.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "char":
                return char.class;
            case "boolean":
                return boolean.class;
            default:
                throw new ClassNotFoundException("找不到类：" + type);
        }
    }


}
