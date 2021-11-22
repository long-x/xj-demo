package com.ecdata.cmp.common.utils;

import com.ecdata.cmp.common.constant.Constants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author honglei
 * @since 2019-08-14
 */
public final class MD5Util {

    private MD5Util() {

    }

    public static String hex(byte[] array) {
        final int mask1 = 0xFF;
        final int mask2 = 0x100;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & mask1) | mask2).substring(1, Constants.THREE));
        }
        return sb.toString();
    }

    public static String md5Hex(String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}
