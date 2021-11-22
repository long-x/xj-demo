package com.ecdata.cmp.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.activiti.entity.WorkflowTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2020-04-15
 */
@Mapper
@Repository
public interface WorkflowTaskMapper extends BaseMapper<WorkflowTask> {

    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 查询待办
     *
     * @param list 关联列表
     * @return map
     */
    List<Map<String, Object>> queryBacklog(List<String> list);

    /**
     * 查询已办(一票否决情况)
     *
     * @param userId 用户id
     * @return 任务id列表
     */
    List<String> queryApproved(Long userId);
}
