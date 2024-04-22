package com.zc.rpc.protocol.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
@Getter
@Setter
public class RpcMessage implements Serializable {

    private boolean oneway;

    private boolean async;

}
