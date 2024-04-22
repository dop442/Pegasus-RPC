package com.zc.rpc.codec;

import com.zc.rpc.serialization.api.Serialization;
import com.zc.rpc.serialization.jdk.JdkSerialization;
import com.zc.rpc.spi.loader.ExtensionLoader;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public interface RpcCodec {
    /**
     * 根据serializationType通过SPI获取序列化句柄
     * @param serializationType 序列化方式
     * @return Serialization对象
     */
    default Serialization getJdkSerialization(String serializationType){
        return ExtensionLoader.getExtension(Serialization.class, serializationType);
    }
}
