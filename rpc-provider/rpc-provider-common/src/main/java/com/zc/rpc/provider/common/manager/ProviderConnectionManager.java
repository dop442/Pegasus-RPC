package com.zc.rpc.provider.common.manager;

import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.enumeration.RpcType;
import com.zc.rpc.protocol.header.RpcHeader;
import com.zc.rpc.protocol.header.RpcHeaderFactory;
import com.zc.rpc.protocol.response.RpcResponse;
import com.zc.rpc.provider.common.cache.ProviderChannelCache;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @Description 提供者的连接管理器，负责扫描清除空闲的Channel、向channel发送心跳请求
 * @Author Schrodinger's Cobra
 * @Date 2024-02-22
 */
@Slf4j
public class ProviderConnectionManager {

    /**
     * @Description 扫描清除空闲的Channel
     */
    public static void scanNotActiveChannel() {
        Set<Channel> channelCache = ProviderChannelCache.getChannelCache();
        if (channelCache == null || channelCache.isEmpty()) {
            return;
        }
        channelCache.forEach((channel) -> {
            if (!channel.isOpen() || !channel.isActive()) {
                channel.close();
                ProviderChannelCache.remove(channel);
            }
        });
    }

    /**
     * @Description 发送心跳请求
     */
    public static void broadcastPingMessageFromProvider() {
        Set<Channel> channelCache = ProviderChannelCache.getChannelCache();
        if (channelCache == null || channelCache.isEmpty()) {
            return;
        }
        RpcHeader header = RpcHeaderFactory.getRequestHeader(RpcConstants.SERIALIZATION_JDK, RpcType.HEARTBEAT_FROM_PROVIDER.getType());
        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
        RpcResponse response = new RpcResponse();
        response.setResult(RpcConstants.HEARTBEAT_PING);
        responseRpcProtocol.setHeader(header);
        responseRpcProtocol.setBody(response);
        channelCache.forEach((channel -> {
            if (channel.isOpen() || channel.isActive()) {
                log.info("提供者发送心跳请求给消费者，消费者是{}", channel.remoteAddress());
                channel.writeAndFlush(responseRpcProtocol);
            }
        }));

    }


}
