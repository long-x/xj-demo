package com.ecdata.cmp.common.auth;

import com.ecdata.cmp.common.crypto.Sign;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author honglei
 * @since 2019-08-19
 */
public final class Sessions {
    /**
     * SHORT_SESSION
     */
    public static final long SHORT_SESSION = TimeUnit.HOURS.toMillis(12);
    /**
     * LONG_SESSION
     */
    public static final long LONG_SESSION = TimeUnit.HOURS.toMillis(30 * 24);

    /**
     * 根据条件种cookie
     *
     * @param userId        userId
     * @param support       角色
     * @param rememberMe    是否记住我
     * @param signingSecret secret
     * @param externalApex  根域名
     * @param response      http response
     */
    public static void loginUser(String userId,
                                 boolean support,
                                 boolean rememberMe,
                                 String signingSecret,
                                 String externalApex,
                                 HttpServletResponse response) {
        long duration;
        //最大过期时间，以秒为单位Sets the maximum age of the cookie in seconds
        int maxAge;
        //是否记住我
        if (rememberMe) {
            duration = LONG_SESSION;
        } else {
            duration = SHORT_SESSION;
        }
        final int n = 1000;
        maxAge = (int) (duration / n);

        String token = Sign.generateSessionToken(userId, signingSecret, support, duration);

        Cookie cookie = new Cookie(AuthConstant.COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setDomain(externalApex);
        cookie.setMaxAge(maxAge);
        //防止Cross Site Scripting即跨站脚本 攻击
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * @param request request
     * @return tokenCookie value
     */
    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> AuthConstant.COOKIE_NAME.equals(cookie.getName()))
                .findAny().orElse(null);
        if (tokenCookie == null) {
            return null;
        }
        return tokenCookie.getValue();
    }

    /**
     * @param externalApex externalApex
     * @param response     void
     */
    public static void logout(String externalApex, HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthConstant.COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setDomain(externalApex);
        response.addCookie(cookie);
    }

    /**
     * 工具类没有构造方法的必要
     */
    private Sessions() {
        throw new UnsupportedOperationException();
    }
}
