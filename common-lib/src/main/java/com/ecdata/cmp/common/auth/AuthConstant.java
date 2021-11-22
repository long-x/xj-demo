package com.ecdata.cmp.common.auth;

/**
 * @author honglei
 * @since 2019-08-16
 */
public final class AuthConstant {
    /**
     *
     */
    public static final String COOKIE_NAME = "ocpmgp-gateway";
    /**
     * header set for internal user id
     */
    public static final String CURRENT_USER_HEADER = "gateway-current-user-id";
    /**
     * AUTHORIZATION_HEADER is the http request header
     * key used for accessing the internal authorization.
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";
    /**
     * AUTHORIZATION_ANONYMOUS_WEB is set as the Authorization header to denote that
     * a request is being made bu an unauthenticated web user
     */
    public static final String AUTHORIZATION_ANONYMOUS_WEB = "gateway-anonymous";
    /**
     * AUTHORIZATION_COMPANY_SERVICE is set as the Authorization header to denote
     * that a request is being made by the company service
     */
    public static final String AUTHORIZATION_COMPANY_SERVICE = "company-service";
    /**
     * AUTHORIZATION_BOT_SERVICE is set as the Authorization header to denote that
     * a request is being made by the bot microservice
     */
    public static final String AUTHORIZATION_BOT_SERVICE = "pool-service";
    /**
     * AUTHORIZATION_ACCOUNT_SERVICE is set as the Authorization header to denote that
     * a request is being made by the account service
     */
    public static final String AUTHORIZATION_ACCOUNT_SERVICE = "project-service";
    /**
     * AUTHORIZATION_SUPPORT_USER is set as the Authorization header to denote that
     * a request is being made by a Staffjoy team member
     */
    public static final String AUTHORIZATION_SUPPORT_USER = "gateway-support";
    /**
     * AUTHORIZATION_SUPERPOWERS_SERVICE is set as the Authorization header to
     * denote that a request is being made by the dev-only superpowers service
     */
    public static final String AUTHORIZATION_SUPERPOWERS_SERVICE = "superpowers-service";
    /**
     * AUTHORIZATION_WWW_SERVICE is set as the Authorization header to denote that
     * a request is being made by the www login / signup system
     */
    public static final String AUTHORIZATION_WWW_SERVICE = "web-service";
    /**
     * AUTH_WHOAMI_SERVICE is set as the Authorization heade to denote that
     * request is being made by the whoami microservice
     */
    public static final String AUTHORIZATION_WHOAMI_SERVICE = "whoami-service";
    /**
     * AUTHORIZATION_AUTHENTICATED_USER is set as the Authorization header to denote that
     * request is being made by an authenticated we6b user
     */
    public static final String AUTHORIZATION_AUTHENTICATED_USER = "gateway-authenticated";
    /**
     * AUTHORIZATION_ICAL_SERVICE is set as the Authorization header to denote that
     * a request is being made by the ical service
     */
    public static final String AUTHORIZATION_ICAL_SERVICE = "ical-service";
    /**
     * AUTH ERROR Messages
     */
    public static final String ERROR_MSG_DO_NOT_HAVE_ACCESS = "You do not have access to this service";
    /**
     * AUTH ERROR Messages
     */
    public static final String ERROR_MSG_MISSING_AUTH_HEADER = "Missing Authorization http header";

    /**
     * 工具类不需要一个公共的构造方法，如何做？1.private 2.UnsupportedOperationException
     */
    private AuthConstant() {
        //not final to allow subclassing
        throw new UnsupportedOperationException();
    }
}
