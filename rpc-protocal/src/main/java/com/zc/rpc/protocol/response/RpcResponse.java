package com.zc.rpc.protocol.response;

import com.zc.rpc.protocol.base.RpcMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
@Getter
@Setter
@ToString
public class RpcResponse extends RpcMessage {
    private static final long serialVersionUID = 425335064405584525L;
    private String error;
    private Object result;
    public boolean isError(){
        return error!=null;
    }
}
