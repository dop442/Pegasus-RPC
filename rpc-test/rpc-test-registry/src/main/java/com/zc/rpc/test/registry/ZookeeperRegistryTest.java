package com.zc.rpc.test.registry;

import com.zc.rpc.protocol.meta.ServiceMeta;
import com.zc.rpc.registry.api.RegistryService;
import com.zc.rpc.registry.api.config.RegistryConfig;
import com.zc.rpc.registry.zookeeper.ZookeeperRegistryServiceImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-09
 */
public class ZookeeperRegistryTest {
    private RegistryService registryService;
    private ServiceMeta serviceMeta;

    @Before
    public void init() throws Exception{
        RegistryConfig registryConfig = new RegistryConfig("192.168.217.130:2181", "zookeeper");
        this.registryService = new ZookeeperRegistryServiceImpl();
        this.registryService.init(registryConfig);
        this.serviceMeta = new ServiceMeta(ZookeeperRegistryTest.class.getName(), "1.0.0", "zc", "127.0.0.1", 8000);
    }

    @Test
    public void testRegister() throws Exception{
        this.registryService.registry(serviceMeta);
    }

    @Test
    public void testUnRegister() throws Exception{
        this.registryService.unRegistry(serviceMeta);
    }

    @Test
    public void testDiscovery() throws Exception{
        this.registryService.discovery(RegistryService.class.getName(), "zc".hashCode(), "127.0.0.1");
    }

    @Test
    public void testDestroy() throws Exception{
        this.registryService.destroy();
    }


}
