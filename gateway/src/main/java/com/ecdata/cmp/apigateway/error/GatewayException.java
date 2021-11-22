package com.ecdata.cmp.apigateway.error;

/**
 * @author honglei
 * @since 2019-08-29
 */
public class GatewayException extends RuntimeException {
    public GatewayException(String message) {
        super(message);
    }

    public GatewayException(String message, Throwable cause) {
        super(message, cause);
    }
}
