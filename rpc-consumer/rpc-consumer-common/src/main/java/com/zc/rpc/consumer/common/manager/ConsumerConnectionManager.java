package com.zc.rpc.consumer.common.manager;

import com.zc.rpc.common.cache.ConsumerChannelCache;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.enumeration.RpcType;
import com.zc.rpc.protocol.header.RpcHeader;
import com.zc.rpc.protocol.header.RpcHeaderFactory;
import com.zc.rpc.protocol.request.RpcRequest;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @Description 在消费者端管理连接的管理器
 * @Author Schrodinger's Cobra
 * @Date 2024-02-21
 */
public class ConsumerConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(ConsumerConnectionManager.class);

    /**
     * @Description 扫描并移除不活跃的Channel连接
     */
    public static void scanNotActiveChannel(){
        Set<Channel> channelCache = ConsumerChannelCache.getChannelCache();
        if (channelCache == null || channelCache.isEmpty()) {
            return;
        }
        channelCache.forEach((channel -> {
            if (!channel.isOpen()||!channel.isActive()){
                channel.close();
                ConsumerChannelCache.remove(channel);
            }
        }));
    }

    /**
     * @Description 发送心跳请求 ping
     */
    public static void broadcastPingMessageFromConsumer(){
        Set<Channel> channelCache = ConsumerChannelCache.getChannelCache();
        if (channelCache == null||channelCache.isEmpty()) {
            return;
        }
        RpcHeader header = RpcHeaderFactory.getRequestHeader(RpcConstants.SERIALIZATION_JDK, RpcType.HEARTBEAT_FROM_CONSUMER.getType());
        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setParameters(new Object[]{RpcConstants.HEARTBEAT_PING});
        requestRpcProtocol.setHeader(header);
        requestRpcProtocol.setBody(rpcRequest);
        channelCache.forEach(channel -> {
            if (channel.isOpen()&&channel.isActive()){
                log.info("消费者发送心跳请求给提供者。提供者是：{}", channel.remoteAddress());
                channel.writeAndFlush(requestRpcProtocol);
            }
        });
    }

}
