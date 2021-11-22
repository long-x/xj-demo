package com.ecdata.cmp.apigateway.error;

/**
 * @author honglei
 * @since 2019-08-29
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
