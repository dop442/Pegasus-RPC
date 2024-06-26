package com.zc.rpc.common.utils;

import java.util.stream.IntStream;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public class SerializationUtils {

    private static final String PADDING_STRING = "0";

    /**
     * 序列化类型最大长度为16
     */
    public static final int MAX_SERIALIZATION_TYPE_COUNT = 16;


    /**
     * @param str 原始字符串
     * @return 补0后的字符串
     * @Description 为长度不足16的字符串后面补0
     */
    public static String paddingString(String str) {
        str = transNullToEmpty(str);
        if (str.length() >= MAX_SERIALIZATION_TYPE_COUNT) {
            return str;
        }
        int paddingCount = MAX_SERIALIZATION_TYPE_COUNT - str.length();
        StringBuilder paddingString = new StringBuilder(str);
        IntStream.range(0, paddingCount).forEach((i)->{
            paddingString.append(PADDING_STRING);
        });
        return paddingString.toString();
    }

    /**
     * @param str 原始字符串
     * @return 去0后的字符串
     * @Description 字符串去0操作
     */
    public static String subString(String str){
        str = transNullToEmpty(str);
        return str.replace(PADDING_STRING, "");
    }


    public static String transNullToEmpty(String str) {
        return str == null ? "" : str;
    }

}
