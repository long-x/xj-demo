package com.ecdata.cmp.common.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import lombok.AllArgsConstructor;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-03-19
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(TenantProperties.class)
public class MybatisPlusConfig {
    /**
     * 多租户配置类
     */
    private final TenantProperties tenantProperties;

    /**
     * 多租户属于 SQL 解析部分，依赖 MP 分页插件
     * @return PaginationInterceptor
     */
    @Bean
    @ConditionalOnMissingBean(PaginationInterceptor.class)
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandlerImpl(tenantProperties));

        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
        paginationInterceptor.setSqlParserFilter((metaObject) -> {
            MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
            return tenantProperties.getMapperFilter().contains(ms.getId());
        });
        return paginationInterceptor;
    }

    /**
     * 启用逻辑删除
     * @return LogicSqlInjector
     */
    @Bean
    public LogicSqlInjector logicSqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * 性能分析拦截器，不建议生产使用
     * 用来观察 SQL 执行情况及执行时长
     * @return PerformanceInterceptor
     */
    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

//    /**
//     * 乐观锁插件
//     * @return
//     */
//    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
//        return new OptimisticLockerInterceptor();
//    }

}
