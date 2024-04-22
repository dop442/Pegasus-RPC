package com.zc.rpc.serialization.hessian2;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.zc.rpc.common.exception.SerializerException;
import com.zc.rpc.serialization.api.Serialization;
import com.zc.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-11
 */
@SPIClass
public class Hessian2Serialization implements Serialization {

    private static final Logger log = LoggerFactory.getLogger(Hessian2Serialization.class);


    @Override
    public <T> byte[] serialize(T obj) {
        log.info("execute hessian2 serialize");
        if (obj == null) {
            throw new SerializerException("serialize object is null");
        }
        byte[] result = new byte[0];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        try {
            hessian2Output.startMessage();
            hessian2Output.writeObject(obj);
            hessian2Output.flush();
            hessian2Output.completeMessage();
            result = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            try {
                if (hessian2Output != null) {
                    hessian2Output.close();
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                throw new SerializerException(e.getMessage(), e);
            }
        }
        return result;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        log.info("execute hessian2 deserialize..");
        if (data == null) {
            throw new SerializerException("deserialize data is null");
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
        T object = null;
        try {
            hessian2Input.startMessage();
            object = (T) hessian2Input.readObject();
            hessian2Input.completeMessage();
        } catch (IOException e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            try {
                if (hessian2Input != null) {
                    hessian2Input.close();
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                throw new SerializerException(e.getMessage(), e);
            }
            return object;
        }
    }
}

