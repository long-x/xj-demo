package com.ecdata.cmp.common.config;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.ecdata.cmp.common.crypto.Sign;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 租户信息处理器
 *
 * @author xuxinsheng
 * @since 2019-03-19
 */
@Slf4j
@AllArgsConstructor
public class TenantHandlerImpl implements TenantHandler {

    /**
     * 租户参数
     */
    private final TenantProperties tenantProperties;

    /**
     * 获取租户编号
     *
     * @return 租户编号
     */
    @Override
    public Expression getTenantId() {
        // 获取租户ID，通过解析器注入到SQL中。
        Long currentTenantId = null;
        try {
            currentTenantId = Sign.getTenantId();
        } catch (Exception e) {
            Sign.setCurrentTenantId(10000L);
            currentTenantId = Sign.getTenantId();
        } finally {
            Sign.removeCurrentTenantId();
        }

        if (null == currentTenantId) {
            throw new RuntimeException("getTenantId error");
        }
        return new LongValue(currentTenantId);
//        return new LongValue(10000);
    }

    /**
     * 获取租户字段名称
     *
     * @return 租户字段名称
     */
    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getColumn();
    }

    /**
     * 过滤租户表
     *
     * @param tableName 表名
     * @return 是否进行过滤
     */
    @Override
    public boolean doTableFilter(String tableName) {
        return tenantProperties.getTableFilter().size() > 0 && tenantProperties.getTableFilter().contains(tableName);
    }
}
