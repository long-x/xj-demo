package com.ecdata.cmp.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.activiti.entity.Workflow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Mapper
@Repository
public interface WorkflowMapper extends BaseMapper<Workflow> {

    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

}
