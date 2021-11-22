package com.ecdata.cmp.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置
 *
 * @author xuxinsheng
 * @since 2019-03-19
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    /**
     * 多租户字段名称
     */
    private Integer defaultId;

    /**
     * 多租户字段名称
     */
    private String column = "tenant_id";

    /**
     * 过滤表
     */
    private List<String> tableFilter = new ArrayList<>();

    /**
     * 过滤的映射方法
     */
    private List<String> mapperFilter = new ArrayList<>();

}
