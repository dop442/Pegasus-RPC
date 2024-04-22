package com.zc.rpc.protocol.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-08
 */

@Getter
@Setter
public class ServiceMeta implements Serializable {

    private static final long serialVersionUID = 6289735590272020366L;
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本号
     */
    private String serviceVersion;
    /**
     * 服务地址
     */
    private String serviceAddr;
    /**
     * 服务端口
     */
    private int servicePort;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 负载均衡节点权重
     */
    private int weight;

    public ServiceMeta() {
    }
    public ServiceMeta(String serviceName, String serviceVersion, String serviceGroup, String serviceAddr, int servicePort, int weight) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.serviceAddr = serviceAddr;
        this.servicePort = servicePort;
        this.serviceGroup = serviceGroup;
        this.weight = weight;
    }

}
