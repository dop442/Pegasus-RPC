package com.zc.rpc.provider.common.cache;

import io.netty.channel.Channel;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Description 提供者的Channel连接缓存
 * @Author Schrodinger's Cobra
 * @Date 2024-02-22
 */
public class ProviderChannelCache {
    @Getter
    private static volatile Set<Channel> channelCache = new CopyOnWriteArraySet<>();

    public static void add(Channel channel){
        channelCache.add(channel);
    }

    public static void remove(Channel channel){
        channelCache.remove(channel);
    }


}
