package com.zc.rpc.protocol.enumeration;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public enum RpcType {

    /**
     * 请求消息
     */
    REQUEST(1),

    /**
     * 响应消息
     */
    RESPONSE(2),

    /**
     * 来自消费者的心跳消息
     */
    HEARTBEAT_FROM_CONSUMER(3),

    /**
     * 回应消费者的心跳消息
     */
    HEARTBEAT_TO_CONSUMER(4),

    /**
     * 来自提供者的心跳消息
     */
    HEARTBEAT_FROM_PROVIDER(5),

    /**
     * 回应提供者的心跳消息
     */
    HEARTBEAT_TO_PROVIDER(6);



    private final int type;

    RpcType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static RpcType findByType(int type){
        for (RpcType rpcType : RpcType.values()) {
            if (rpcType.getType() == type) {
                return rpcType;
            }
        }
        return null;
    }
}
