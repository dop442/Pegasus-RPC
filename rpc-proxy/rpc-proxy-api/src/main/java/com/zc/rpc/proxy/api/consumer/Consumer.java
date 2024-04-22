package com.zc.rpc.proxy.api.consumer;

import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.proxy.api.future.RpcFuture;
import com.zc.rpc.registry.api.RegistryService;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-05
 */
public interface Consumer {


    /**
     * @param protocol 发送的具体内容（protocol协议）
     * @param registryService 注册中心服务
     * @return RpcFuture 返回发送的结果
     * @Description
     */
    RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, RegistryService registryService) throws Exception;

}
