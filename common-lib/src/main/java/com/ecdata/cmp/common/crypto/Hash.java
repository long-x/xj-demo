package com.ecdata.cmp.common.crypto;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author honglei
 * @since 2019-08-14
 */
public final class Hash {
    /**
     * 如果构造方法是protected的，则建议在构造方法里面抛出异常，以防止子类被实例化
     */
    private Hash() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * 默认key
     */
    private static String DEFAULT_KEY = "cci-default-key-8f1a57a0-a437-4352-88c1-753a8ed0765c";
    /**
     * 默认 mac
     */
    private static Mac DEFAULT_MAC = null;

    /**
     * @param key  key
     * @param data data
     * @return hex
     * @throws Exception exception
     */
    public static String encode(String key, String data) throws Exception {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256HMAC.init(secretKey);

        return Hex.toHexString(sha256HMAC.doFinal(data.getBytes("UTF-8")));
    }

    /**
     * 使用默认key加密
     * @param data  数据
     * @return 加密后字符串
     * @throws Exception    exception
     */
    public static String encode(String data) throws Exception {

        if (DEFAULT_MAC == null) {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(DEFAULT_KEY.getBytes("UTF-8"), "HmacSHA256");
            sha256HMAC.init(secretKey);
            DEFAULT_MAC = sha256HMAC;
        }

        return Hex.toHexString(DEFAULT_MAC.doFinal(data.getBytes("UTF-8")));
    }

}
