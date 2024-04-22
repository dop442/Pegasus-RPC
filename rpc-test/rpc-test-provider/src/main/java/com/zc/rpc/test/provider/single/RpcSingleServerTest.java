package com.zc.rpc.test.provider.single;

import com.zc.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public class RpcSingleServerTest {
    @Test
    public void startRpcSingleServer(){
        RpcSingleServer singleServer = new RpcSingleServer("127.0.0.1:27880", "192.168.217.130:2181","zookeeper","com.zc.rpc.test", "jdk", "random", 30000, 60000);
        singleServer.startNettyServer();
    }
}
