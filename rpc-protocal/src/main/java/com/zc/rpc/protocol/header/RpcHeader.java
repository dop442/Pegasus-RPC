package com.zc.rpc.protocol.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */

@Getter
@Setter
@ToString
public class RpcHeader implements Serializable {

    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 报文类型 1byte | 状态 1byte | 消息 ID 8byte    |
    +---------------------------------------------------------------+
    |           序列化类型 16byte      | 数据长度 4byte    |
    +---------------------------------------------------------------+
    */

    private short magic;

    private byte msgType;

    private byte status;

    private long requestId;

    private String serializationType;

    private int msgLen;


}
