package com.zc.rpc.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.zc.rpc.common.exception.SerializerException;
import com.zc.rpc.serialization.api.Serialization;
import com.zc.rpc.spi.annotation.SPIClass;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-15
 */
@Slf4j
@SPIClass
public class KryoSerialization implements Serialization {
    @Override
    public <T> byte[] serialize(T obj) {
        log.info("execute kryo serialize....");
        if (obj == null) {
            throw new SerializerException("serialize object is null");
        }
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(obj.getClass(), new JavaSerializer());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, obj);
        output.flush();
        output.close();
        byte[] bytes = baos.toByteArray();
        try{
            baos.flush();
            baos.close();
        }catch (IOException e){
            throw new SerializerException(e.getMessage(), e);
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        log.info("execute kryo deserialize....");
        if (data == null){
            throw new SerializerException("deserialize data is null");
        }
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(cls, new JavaSerializer());
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Input input = new Input(bais);
        return (T) kryo.readClassAndObject(input);
    }
}
