package com.zc.rpc.protocol;

import com.zc.rpc.protocol.header.RpcHeader;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Description RPC的数据传输体（ header和 body ）
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
@Getter
@Setter
public class RpcProtocol<T> implements Serializable {

    private static final long serialVersionUID = 292789485166173277L;

    private RpcHeader header;

    private T body;

}
