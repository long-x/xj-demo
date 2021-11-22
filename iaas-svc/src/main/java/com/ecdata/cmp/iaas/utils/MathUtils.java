package com.ecdata.cmp.iaas.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * @title: utils
 * @Author: shig
 * @description: 加减乘除
 * @Date: 2019/11/19 8:34 下午
 */
public abstract class MathUtils {

    /***
     * 保留2为精度
     */
    private static final int DIV_SCALE_0 = 0;

    private static final int DIV_SCALE_2 = 2;


    /***
     * 加法
     * @param a1
     * @param a2
     * @return
     */
    public static String add(String a1, String a2) {
        return add(a1, a2, 0);
    }

    /***
     * 加法
     * @param a1
     * @param a2
     * @param scale 精度
     * @return
     */
    public static String add(String a1, String a2, int scale) {
        if (!StringUtils.hasText(a1)) {
            a1 = "0";
        }
        if (!StringUtils.hasText(a2)) {
            a2 = "0";
        }
        BigDecimal b1 = BigDecimal.valueOf(Double.parseDouble(a1));
        BigDecimal b2 = b1.add(BigDecimal.valueOf(Double.parseDouble(a2)));
        return getFormart(scale).format(b2);
    }

    public static String sub(String s1, String s2) {
        return sub(s1, s2, 0);
    }

    /**
     * 减法
     *
     * @param s1
     * @param s2
     * @param scale
     * @return
     */
    public static String sub(String s1, String s2, int scale) {
        if (!StringUtils.hasText(s1)) {
            s1 = "0";
        }
        if (!StringUtils.hasText(s2)) {
            s2 = "0";
        }
        BigDecimal b1 = new BigDecimal(Double.parseDouble(s1));
        BigDecimal b2 = new BigDecimal(Double.parseDouble(s2));
        return getFormart(scale).format(b1.subtract(b2).doubleValue());
    }


    /***
     * 除法，默认2位小数
     * @param v1
     * @param v2
     * @return
     */
    public static String div(String v1, String v2) {
        return div(v1, v2, 2);
    }

    /***
     * 除法，默认2位小数
     * @param v1
     * @param v2
     * @return
     */
    public static double divDouble(String v1, String v2) {
        return org.apache.commons.lang3.StringUtils.isBlank(div(v1, v2, 4)) ? 0 : Double.valueOf(div(v1, v2, 2));
    }

    /**
     * 除法
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static String div(String v1, String v2, int scale) {
        if (!StringUtils.hasText(v1)) {
            return getFormart(scale).format(0d);
        } else if (!StringUtils.hasText(v2)) {
            return "";
        }
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.parseDouble(v1));
//        b1 = b1.multiply(new BigDecimal(100d));
        BigDecimal b2 = new BigDecimal(Double.parseDouble(v2));
        if (b2.compareTo(new BigDecimal(0)) == 0) {
            return "";
        }
        return getFormart(scale).format(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP));
    }

    public static double divDouble(String v1, String v2, int scale) {
        return org.apache.commons.lang3.StringUtils.isBlank(div(v1, v2, scale)) ? 0 : Double.valueOf(div(v1, v2, scale));
    }

    /**
     * 乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String mul(String v1, String v2) {
        return mul(v1, v2, 0);
    }

    /**
     * 乘法
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static String mul(String v1, String v2, int scale) {
        if (!StringUtils.hasText(v1)) {
            v1 = "0";
        }
        if (!StringUtils.hasText(v2)) {
            v2 = "0";
        }
        BigDecimal b1 = new BigDecimal(Double.parseDouble(v1));
        BigDecimal b2 = new BigDecimal(Double.parseDouble(v2));
        return getFormart(scale).format(b1.multiply(b2));
    }

    public static DecimalFormat getFormart(int scale) {
        DecimalFormat format = new DecimalFormat("#0");
        if (scale > 0) {
            String f = String.format("%0" + scale + "d", 0);
            format = new DecimalFormat("#0." + f);
        }
        return format;
    }


    public static void main(String[] args) {
//        System.out.println(divDouble("3", "64"));
//        System.out.println(add("2", "780967", 2));
//        System.out.println(sub("2", "780967", 0));
        System.out.println(div("3", "64", 4));
//        System.out.println(mul("2", "2", 0));
    }

}
