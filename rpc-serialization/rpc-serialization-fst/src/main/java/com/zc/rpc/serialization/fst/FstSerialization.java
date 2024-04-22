package com.zc.rpc.serialization.fst;

import com.zc.rpc.common.exception.SerializerException;
import com.zc.rpc.serialization.api.Serialization;
import com.zc.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;
import org.nustaq.serialization.FSTConfiguration;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-13
 */
@SPIClass
@Slf4j
public class FstSerialization implements Serialization {

    @Override
    public <T> byte[] serialize(T obj) {
        log.info("execute fst serialize...");
        if (obj == null) {
            throw new SerializerException("serialize obj is null");
        }
        FSTConfiguration conf = FSTConfiguration.getDefaultConfiguration();
        return conf.asByteArray(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        log.info("execute fst deserialize...");
        if (data == null) {
            throw new SerializerException("deserialize data is null");
        }
        FSTConfiguration conf = FSTConfiguration.getDefaultConfiguration();
        return (T)conf.asObject(data);
    }
}
