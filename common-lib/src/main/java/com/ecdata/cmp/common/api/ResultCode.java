package com.ecdata.cmp.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author honglei
 * @since 2019-08-14
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    /**
     * Status code (200) indicating the request succeeded normally
     */
    SUCCESS(HttpServletResponse.SC_OK, "Operation is Successful"),
    /**
     * Status code (400) indicating the request sent by the client was
     * syntactically incorrect.
     */
    FAILURE(HttpServletResponse.SC_BAD_REQUEST, "Biz Exception"),
    /**
     * Status code (401) indicating that the request requires HTTP
     * authentication.
     */
    UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "Request Unauthorized"),
    /**
     * Status code (404) indicating that the requested resource is not
     * available.
     */
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "404 Not Found"),
    /**
     * Status code (400) indicating the request sent by the client was
     * syntactically incorrect.
     */
    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "Message Can't be Read"),

    /**
     * Status code (405) indicating that the method specified in the
     * <code><em>Request-Line</em></code> is not allowed for the resource
     * identified by the <code><em>Request-URI</em></code>.
     */
    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Supported"),
    /**
     * Status code (415) indicating that the server is refusing to service the
     * request because the entity of the request is in a format not supported by
     * the requested resource for the requested method.
     */
    MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Media Type Not Supported"),
    /**
     * Status code (403) indicating the server understood the request but
     * refused to fulfill it.
     */
    REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "Request Rejected"),
    /**
     * Status code (500) indicating an error inside the HTTP server which
     * prevented it from fulfilling the request.
     */
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),
    /**
     * Status code (400) indicating the request sent by the client was
     * syntactically incorrect.
     */
    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "Missing Required Parameter"),
    /**
     * Status code (400) indicating the request sent by the client was
     * syntactically incorrect.
     */
    PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Type Mismatch"),
    /**
     * Status code (400) indicating the request sent by the client was
     * syntactically incorrect.
     */
    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Binding Error"),
    /**
     * Status code (400) indicating the request sent by the client was
     * syntactically incorrect.
     */
    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Validation Error");
    /**
     * code
     */
    private final int code;
    /**
     * msg
     */
    private final String msg;
}
