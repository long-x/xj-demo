package com.ecdata.cmp.common.utils;

/**
 * @author honglei
 * @since 2019-08-14
 */
public final class Helper {

    private Helper() {

    }

    /**
     * @param email email
     * @return hash
     */
    public static String generateGravatarUrl(String email) {
        String hash = MD5Util.md5Hex(email);
        return String.format("https://www.gravatar.com/avatar/%s.jpg?s=400&d=identicon", hash);
    }
}
