package com.zc.rpc.serialization.jdk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zc.rpc.common.exception.SerializerException;
import com.zc.rpc.serialization.api.Serialization;
import com.zc.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;

import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
@SPIClass
@Slf4j
public class JdkSerialization implements Serialization {

    @Override
    public <T> byte[] serialize(T obj) {
        log.info("execute jdk serialize....");
        if (obj == null) {
            throw new SerializerException("serialize object is null");
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        log.info("execute jdk deserialize....");
        if (data == null) {
            throw new SerializerException("deserialize data is null");
        }
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(is);
            return (T) in.readObject();
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }
}
