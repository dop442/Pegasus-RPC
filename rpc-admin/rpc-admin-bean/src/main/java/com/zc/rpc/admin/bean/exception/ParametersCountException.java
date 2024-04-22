package com.zc.rpc.admin.bean.exception;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-09
 */
public class ParametersCountException extends RuntimeException{

    public ParametersCountException(String message) {
        super(message);
    }
}
