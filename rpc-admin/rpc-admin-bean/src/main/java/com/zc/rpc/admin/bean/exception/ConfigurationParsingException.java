package com.zc.rpc.admin.bean.exception;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-01
 */
public class ConfigurationParsingException extends RuntimeException{
    private static final long serialVersionUID = -8368289846789102671L;

    public ConfigurationParsingException(String message) {
        super(message);
    }
}
