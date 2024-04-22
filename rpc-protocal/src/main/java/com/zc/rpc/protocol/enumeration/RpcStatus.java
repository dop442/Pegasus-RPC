package com.zc.rpc.protocol.enumeration;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-04
 */
public enum RpcStatus {

    // 成功
    SUCCESS(0),

    // 失败
    FAIL(1);


    private final int code;
    RpcStatus(int code){
        this.code = code;
    }
    public int getCode(){
        return code;
    }


}
