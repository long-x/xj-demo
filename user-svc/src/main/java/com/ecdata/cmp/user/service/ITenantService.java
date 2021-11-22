package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.TenantVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.Tenant;

/**
 * @author xuxinsheng
 * @since 2019-03-21
 */
public interface ITenantService extends IService<Tenant> {
    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 查询租户信息
     *
     * @param page    分页参数
     * @param keyword 关键字
     * @return 租户信息
     */
    IPage<TenantVO> qryTenantInfo(Page page, String keyword);

    /**
     * 新增租户成功后，同步新增默认用户相关信息
     *
     * @param tenant 租户
     */
    void initUser(Tenant tenant);
}
