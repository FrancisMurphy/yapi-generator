package com.frank.generator.yapi.common.exception;

import lombok.Data;

/**
 * An {@link Exception} which is thrown by a abandon.
 * @author frank
 */
@Data
public class YapiAbandonException extends RuntimeException
{

    private static final long serialVersionUID = -1464830400709348473L;

    /**
     * abandon code
     */
    private String code;

    /**
     * Creates a new instance.
     */
    public YapiAbandonException() {
    }

    /**
     * Creates a new instance.
     */
    public YapiAbandonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     */
    public YapiAbandonException(String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     */
    public YapiAbandonException(Throwable cause) {
        super(cause);
    }

}
