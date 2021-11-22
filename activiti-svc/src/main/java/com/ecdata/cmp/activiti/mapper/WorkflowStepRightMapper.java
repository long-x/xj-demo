package com.ecdata.cmp.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.activiti.entity.WorkflowStepRight;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Mapper
@Repository
public interface WorkflowStepRightMapper extends BaseMapper<WorkflowStepRight> {

}
