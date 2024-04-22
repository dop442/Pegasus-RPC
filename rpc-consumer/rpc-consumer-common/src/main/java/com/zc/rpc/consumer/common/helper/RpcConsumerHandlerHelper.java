package com.zc.rpc.consumer.common.helper;

import com.zc.rpc.consumer.common.handler.RpcConsumerHandler;
import com.zc.rpc.protocol.meta.ServiceMeta;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 消费者的帮助类，缓存消费者处理器:RpcConsumerHandler
 * @Author Schrodinger's Cobra
 * @Date 2024-02-09
 */
public class RpcConsumerHandlerHelper {

    private static Map<String, RpcConsumerHandler> rpcConsumerHandlerMap;

    static {
        rpcConsumerHandlerMap = new ConcurrentHashMap<>();
    }

    private static String getKey(ServiceMeta key){
        return key.getServiceAddr().concat("_").concat(String.valueOf(key.getServicePort()));
    }

    public static void put(ServiceMeta key, RpcConsumerHandler value){
        rpcConsumerHandlerMap.put(getKey(key), value);
    }

    public static RpcConsumerHandler get(ServiceMeta key){
        return rpcConsumerHandlerMap.get(getKey(key));
    }

    public static Map<String, RpcConsumerHandler> getRpcConsumerHandlerMap() {
        return rpcConsumerHandlerMap;
    }

    public static void closeRpcClientHandler(){
        Collection<RpcConsumerHandler> rpcConsumerHandlers = rpcConsumerHandlerMap.values();
        if (rpcConsumerHandlers != null) {
            rpcConsumerHandlers.forEach((RpcConsumerHandler::close));
        }
        rpcConsumerHandlerMap.clear();
    }
}
