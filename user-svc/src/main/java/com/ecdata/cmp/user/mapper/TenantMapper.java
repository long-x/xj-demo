package com.ecdata.cmp.user.mapper;

import com.ecdata.cmp.user.dto.TenantVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author xuxinsheng
 * @since 2019-03-21
 */
@Mapper
@Repository
public interface TenantMapper extends BaseMapper<Tenant> {
    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 查询租户信息
     * @param page      分页参数
     * @param keyword   关键字
     * @return  租户信息
     */
    IPage<TenantVO> qryTenantInfo(Page page, @Param("keyword") String keyword);

}
