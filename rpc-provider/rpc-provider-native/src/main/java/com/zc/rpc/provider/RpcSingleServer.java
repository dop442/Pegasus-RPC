package com.zc.rpc.provider;

import com.zc.rpc.provider.common.scanner.server.RpcServiceScanner;
import com.zc.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public class RpcSingleServer extends BaseServer {
    private static final Logger log = LoggerFactory.getLogger(RpcSingleServer.class);

    public RpcSingleServer(String serverAddress, String registryAddress, String registryType, String scanPackage, String reflectType, String registryLoadBalanceType, int heartbeatInterval, int scanNotActiveChannelInterval) {
        super(serverAddress, registryAddress, registryType, reflectType, registryLoadBalanceType, heartbeatInterval, scanNotActiveChannelInterval);
        try {
            this.handlerMap = RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(this.host, this.port, scanPackage, registryService);
        } catch (Exception e) {
            log.error("RPC Server init error :{}", e.toString());
        }
    }
}
