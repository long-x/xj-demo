package com.ecdata.cmp.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger配置
 *
 * @author xuxinsheng
 * @since 2019-06-18
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    /**
     * 基础包地址
     */
    private String basePackage = "";
    /**
     * 标题
     */
    private String title = "";
    /**
     * 描述
     */
    private String description = "";
    /**
     * 服务条款网址
     */
    private String termsOfServiceUrl = "";
    /**
     * 创建者
     */
    private String name = "";
    /**
     * 链接
     */
    private String url = "";
    /**
     * 地址邮箱
     */
    private String email = "";
    /**
     * 版本
     */
    private String version = "";

}
