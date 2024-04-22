package com.zc.rpc.serialization.api;

import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.spi.annotation.SPI;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
@SPI(RpcConstants.SERIALIZATION_JDK)
public interface Serialization {
    /**
     * @param obj
     * @param <T>
     * @return
     * @Description 序列化
     */
    <T> byte[] serialize(T obj);


    /**
     * @param data
     * @param cls
     * @param <T>
     * @return
     * @Description 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> cls);

}
