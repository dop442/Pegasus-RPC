package com.zc.rpc.admin.bean.exception;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-11
 */
public class TokenAuthExpiredException extends RuntimeException{
    public TokenAuthExpiredException(String message) {
        super(message);
    }
}
