package com.ecdata.cmp.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author xuxinsheng
 * @since 2019-08-08
 */
@Slf4j
public final class NetworkUtil {

    private NetworkUtil() {
    }

    /**
     * ip正则
     */
    static final String IP_REXP = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    /**
     * 检查ip正确性
     *
     * @param ip ip地址
     * @return true正确  false错误
     */
    public static boolean checkIp(String ip) {
        return StringUtils.isNotEmpty(ip) && Pattern.compile(IP_REXP).matcher(ip).matches();
    }

    /**
     * 检查网段正确性(xx.xx.xx.0/24)
     *
     * @param segment 网段
     * @return true正确  false错误
     */
    public static boolean checkSegment(String segment) {
        if (StringUtils.isEmpty(segment)) {
            return false;
        }
        String[] arr = segment.split("/");
        final int arrLen = 2;
        if (arr.length != arrLen || !Pattern.compile(IP_REXP).matcher(arr[0]).matches()) {
            return false;
        }
        try {
            final int max = 32;
            int num = Integer.valueOf(arr[1]);
            if (num < 0 || num > max) {
                return false;
            }
        } catch (NumberFormatException ex) {
            log.error("网段格式错误", ex);
            return false;
        }

        return true;
    }

    /**
     * 获取ip数值
     *
     * @param ipStr ip字符串
     * @return ip数值
     */
    public static long ipConvertValue(String ipStr) {
        String[] ipInArray = ipStr.split("\\.");
        long value = 0;
        final int moveBits = 8;
        for (String b : ipInArray) {
            value = (value << moveBits) + Integer.parseInt(b);
        }
        return value;
    }

    /**
     * 获取ip字符串
     *
     * @param ipValue ip数值
     * @return ip字符串
     */
    public static String valueConvertIp(long ipValue) {
        final int firstMoveBits = 24;
        final int secondMoveBits = 16;
        final int thirdMoveBits = 8;
        final int mask = 0xFF;
        return String.valueOf((ipValue >> firstMoveBits) & mask) + "."
                + ((ipValue >> secondMoveBits) & mask) + "."
                + ((ipValue >> thirdMoveBits) & mask) + "."
                + (ipValue & mask);
    }

    /**
     * 获取网段开始ip值
     *
     * @param ip      网段或网段内ip
     * @param netmask 子网掩码
     * @return 网段开始ip值
     */
    public static long getStartIpValue(String ip, String netmask) {
        return (ipConvertValue(ip) & ipConvertValue(netmask)) + 1;
    }

    /**
     * 获取网段结束ip值
     *
     * @param ip      网段或网段内ip
     * @param netmask 子网掩码
     * @return 网段结束ip值
     */
    public static long getEndIpValue(String ip, String netmask) {
        long netmaskValue = ipConvertValue(netmask);
        final long max = 0xFFFFFFFFL;
        return (ipConvertValue(ip) & netmaskValue) + (max - netmaskValue) - 1;
    }

    /**
     * 根据掩码位数计算掩码
     *
     * @param maskIndex 掩码位
     * @return 子网掩码
     */
    public static String getNetmask(int maskIndex) {
        long ipValue = 0;
        final int maxMaskIndex = 32;
        if (maskIndex > maxMaskIndex || maskIndex < 0) {
            return null;
        }

        for (int i = 0; i < maskIndex; i++) {
            ipValue = (ipValue << 1) + 1;
        }
        final int moveBits = 32;
        ipValue = ipValue << moveBits - maskIndex;

        return valueConvertIp(ipValue);

    }

    /**
     * ip字符串转ipv4
     *
     * @param ipStr ip字符串
     * @return ipv4
     */
    public static byte[] textToNumericFormatV4(String ipStr) {
        byte[] returnIpBytes = new byte[4];
        long temIpValue = 0L;
        int site = 0;
        boolean dotFlag = true;
        int length = ipStr.length();
        if (length != 0 && length <= 15) {
            for (int i = 0; i < length; ++i) {
                char c = ipStr.charAt(i);
                if (c == '.') {
                    if (dotFlag || temIpValue < 0L || temIpValue > 255L || site == 3) {
                        return null;
                    }

                    returnIpBytes[site++] = (byte) ((int) (temIpValue & 255L));
                    temIpValue = 0L;
                    dotFlag = true;
                } else {
                    int value = Character.digit(c, 10);
                    if (value < 0) {
                        return null;
                    }

                    temIpValue *= 10L;
                    temIpValue += (long) value;
                    dotFlag = false;
                }
            }

            if (!dotFlag && temIpValue >= 0L && temIpValue < 1L << (4 - site) * 8) {
                switch (site) {
                    case 0:
                        returnIpBytes[0] = (byte) ((int) (temIpValue >> 24 & 255L));
                    case 1:
                        returnIpBytes[1] = (byte) ((int) (temIpValue >> 16 & 255L));
                    case 2:
                        returnIpBytes[2] = (byte) ((int) (temIpValue >> 8 & 255L));
                    case 3:
                        returnIpBytes[3] = (byte) ((int) (temIpValue >> 0 & 255L));
                    default:
                        return returnIpBytes;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        //        31.16.1.254  255.255.255.0
        String ip = "192.168.199.1";
        String netmask = "255.255.255.0";
        String segment = "31.15.10.4/23";
       System.out.println(ipConvertValue(ip));
//        System.out.println(valueConvertIp(4294967295L));
        System.out.println(valueConvertIp(getStartIpValue(ip, netmask)));
        System.out.println(valueConvertIp(getEndIpValue(ip, netmask)));
//        System.out.println(checkIp(ip));
//        System.out.println(checkSegment(segment));


    }
}
