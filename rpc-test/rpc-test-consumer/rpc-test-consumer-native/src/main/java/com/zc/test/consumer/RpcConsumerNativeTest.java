package com.zc.test.consumer;

import com.zc.rpc.consumer.RpcClient;
import com.zc.rpc.proxy.api.async.IAsyncObjectProxy;
import com.zc.rpc.proxy.api.future.RpcFuture;
import com.zc.rpc.registry.api.RegistryService;
import com.zc.rpc.registry.zookeeper.ZookeeperRegistryServiceImpl;
import com.zc.rpc.test.api.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-05
 */
@Slf4j
public class RpcConsumerNativeTest {
    private RpcClient rpcClient;

    @Before
    public void initRpcClient(){

        rpcClient = new RpcClient("1.0.0", "zc", "protostuff", 3000, false, false, "192.168.217.130:2181", "zookeeper", "random",30000,60000, 1000, 3);
    }

    @Test
    public void testInterfaceRpc(){
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("zczc");
        log.info("返回的数据结果为===>>>{}",result);
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        rpcClient.shutdown();
    }

    @Test
    public void testAsyncInterface() throws ExecutionException, InterruptedException {
        IAsyncObjectProxy asyncProxyService = rpcClient.createAsync(DemoService.class);
        RpcFuture future = asyncProxyService.call("hello", "zc");
        String result = future.get().toString();
        System.out.println(result);
        rpcClient.shutdown();
    }


//    @Test
//    public void testProxyConsumer(){
//        RpcClient rpcClient = new RpcClient("1.0.0", "zc", "jdk", 3000, false, false);
//        DemoService demoService = rpcClient.create(DemoService.class);
//        String result = demoService.hello("zc");
//        log.info("返回的结果是：{}", result);
//        rpcClient.shutdown();
//    }
//
//    @Test
//    public void testAsyncInterfaceProxyRpc() throws Exception{
//        RpcClient rpcClient = new RpcClient("1.0.0", "zc", "jdk", 3000, false, false);
//        IAsyncObjectProxy demoService = rpcClient.createAsync(DemoService.class);
//        RpcFuture future = demoService.call("hello", "zc");
//        log.info("返回的结果是：{}", future.get());
//        rpcClient.shutdown();
//    }


}
