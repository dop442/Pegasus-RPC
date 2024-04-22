package com.zc.rpc.consumer.common.exception;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-09
 */
public class RegistryException extends RuntimeException{

    /**
     * Instantiates a new Serializer exception.
     *
     * @param e the e
     */
    public RegistryException(final Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new Serializer exception.
     *
     * @param message the message
     */
    public RegistryException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Serializer exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public RegistryException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
