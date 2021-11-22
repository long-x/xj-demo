package com.ecdata.cmp.common.auth;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 上下文助手类 A context holder class for holding the current userId and authz info
 *
 * @author honglei
 * @since 2019-08-16
 */
public final class AuthContext {

    private AuthContext() {

    }

    private static String getRequetHeader(String headerName) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String value = request.getHeader(headerName);
            System.out.println("上下文助手类：headerName：" + headerName + "value:" + value);
            return value;
        }
        return null;
    }

//    public static String getUserId() {
//        return getRequetHeader(AuthConstant.CURRENT_USER_HEADER);
//    }

    public static String getAuthz() {
        return getRequetHeader(AuthConstant.AUTHORIZATION_HEADER);
    }

}
