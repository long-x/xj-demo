package com.ecdata.cmp.common.env;

/**
 * @author honglei
 * @since 2019-08-14
 */
public final class EnvConstant {

    private EnvConstant() {

    }

    /**
     * 开发环境
     */
    public static final String ENV_DEV = "dev";
    /**
     * 测试环境:功能测试环境、
     */
    public static final String ENV_TEST = "test";
    /**
     * similar to staging
     * 准生产环境（客户接收环境）
     */
    public static final String ENV_UAT = "uat";
    /**
     * 生产环境
     */
    public static final String ENV_PROD = "prod";
}
