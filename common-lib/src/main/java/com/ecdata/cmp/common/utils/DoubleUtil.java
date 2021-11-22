package com.ecdata.cmp.common.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @author xuxinsheng
 * @since 2019-10-14
 */
public final class DoubleUtil {

    private DoubleUtil() {

    }

    /**
     * 除法保留精度
     */
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 两个Double数相加
     *
     * @param d1 被加数
     * @param d2 加数
     * @return Double   和
     */
    public static Double add(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * 两个Double数相减
     *
     * @param d1 被减数
     * @param d2 减数
     * @return Double   差
     */
    public static Double sub(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 两个Double数相乘
     *
     * @param d1 被乘数
     * @param d2 乘数
     * @return Double   积
     */
    public static Double mul(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 两个Double数相除
     *
     * @param d1 被除数
     * @param d2 除数
     * @return Double   商
     */
    public static Double div(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 两个Double数相除，并保留scale位小数
     *
     * @param d1    被除数
     * @param d2    除数
     * @param scale 保留小数位
     * @return Double   商
     */
    public static Double div(Double d1, Double d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 数字字符串转换为千位分隔符形式
     *
     * @param number 数字字符串
     * @return 千位分隔符形式数字字符串
     */
    public static String numberStrToThousandSeparator(String number) {
        String positive;
        String decimal = "";
        String[] num = number.split("\\.");
        if (num.length > 1) {
            positive = num[0];
            decimal = num[1];
        } else {
            positive = number;
        }
        String formatPositive = positive.replaceAll("(?<=\\d)(?=(?:\\d{3})+$)", ",");
        return StringUtils.isEmpty(decimal) ? formatPositive : formatPositive + "." + decimal;
    }

    /**
     * 两个Double数比较大小
     * @param d1    d1
     * @param d2    d2
     * @return -1:d1<d2; 0:d1==d2; 1:d1>d2;
     */
    public static int compareTo(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.compareTo(b2);
    }

}
