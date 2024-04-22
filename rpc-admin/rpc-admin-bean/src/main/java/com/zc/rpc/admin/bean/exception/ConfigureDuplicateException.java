package com.zc.rpc.admin.bean.exception;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-08
 */
public class ConfigureDuplicateException extends RuntimeException{
    private static final long serialVersionUID = 7805276743445169853L;

    public ConfigureDuplicateException(String message) {
        super(message);
    }
}
