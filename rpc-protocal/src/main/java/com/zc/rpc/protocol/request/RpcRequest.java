package com.zc.rpc.protocol.request;

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
public class RpcRequest extends RpcMessage {

    private static final long serialVersionUID = 5555776886650396129L;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

    private boolean async;

    private boolean oneway;

    private String version;

    private String group;

}
