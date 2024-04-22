package com.zc.rpc.proxy.api.callback;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-05
 */
public interface AsyncRpcCallback {
    /**
     * 成功后的回调方法
     */
    void onSuccess(Object result);
    /**
     * 异常的回调方法
     */
    void onException(Exception e);
}
