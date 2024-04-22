package com.zc.rpc.protocol.header;

import com.zc.rpc.common.id.IdFactory;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.protocol.enumeration.RpcType;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public class RpcHeaderFactory {

    public static RpcHeader getRequestHeader(String serializationType, int messageType){
        RpcHeader header = new RpcHeader();
        long requestId = IdFactory.getId();
        header.setMagic(RpcConstants.MAGIC);
        header.setRequestId(requestId);
        header.setMsgType((byte) messageType);
        header.setStatus((byte) 0x1);
        header.setSerializationType(serializationType);
        return header;
    }

}
