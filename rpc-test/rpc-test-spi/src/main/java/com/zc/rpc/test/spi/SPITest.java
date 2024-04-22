package com.zc.rpc.test.spi;

import com.zc.rpc.spi.loader.ExtensionLoader;
import com.zc.rpc.test.spi.service.SPIService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-11
 */
@Slf4j
public class SPITest {

    @Test
    public void testSpiLoader(){
        SPIService spiService = ExtensionLoader.getExtension(SPIService.class, "spiService");
        String result = spiService.hello("zc");
        log.info("{}",result);
    }



}
