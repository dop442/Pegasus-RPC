package com.zc.rpc.common.cache;

import io.netty.channel.Channel;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Description 在消费者端的Channel连接缓存（使用心跳机制维护）
 * @Author Schrodinger's Cobra
 * @Date 2024-02-21
 */
public class ConsumerChannelCache {

    @Getter
    private static volatile Set<Channel> channelCache = new CopyOnWriteArraySet<>();

    public static void add(Channel channel){
        channelCache.add(channel);
    }

    public static void remove(Channel channel){
        channelCache.remove(channel);
    }


}
