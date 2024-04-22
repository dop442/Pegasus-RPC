package com.zc.rpc.consumer.common.ip;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-20
 */
@Slf4j
public class IpUtils {

    public static InetAddress getLocalInetAddress(){
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("get local ip address throws exception:{}",e);
        }
        return null;
    }

    public static String getLocalAddress(){
        return getLocalInetAddress().toString();
    }

    public static String getLocalHostName(){
        return getLocalInetAddress().getHostName();
    }

    public static String getLocalHostIp(){
        return getLocalInetAddress().getHostAddress();
    }
}
