package com.zc.rpc.spi.factory;

import com.zc.rpc.spi.annotation.SPIClass;
import com.zc.rpc.spi.annotation.SPI;
import com.zc.rpc.spi.loader.ExtensionLoader;

import java.util.Optional;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-10
 */
@SPIClass
public class SpiExtensionFactory implements ExtensionFactory{
    @Override
    public <T> T getExtension(final String key, final Class<T> clazz) {
        return Optional.ofNullable(clazz)
                .filter(Class::isInterface)
                .filter(cls -> cls.isAnnotationPresent(SPI.class))
                .map(ExtensionLoader::getExtensionLoader)
                .map(ExtensionLoader::getDefaultSpiClassInstance)
                .orElse(null);
    }
}
