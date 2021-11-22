package com.ecdata.cmp.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.activiti.dto.vo.WorkflowVO;
import com.ecdata.cmp.activiti.entity.Workflow;
import com.ecdata.cmp.common.api.BaseResponse;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
public interface IWorkflowService extends IService<Workflow> {
    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 转换
     *
     * @param workflowList 工作流列表
     * @return List<WorkflowVO>
     */
    List<WorkflowVO> transform(List<Workflow> workflowList);

    /**
     * 获取工作流详情
     *
     * @param id 工作流id
     * @return WorkflowVO
     */
    WorkflowVO getDetail(Long id);

    /**
     * 添加工作流
     *
     * @param workflowVO 工作流vo
     * @return BaseResponse
     */
    BaseResponse addWorkflow(WorkflowVO workflowVO);

    /**
     * 修改工作流
     *
     * @param workflowVO 工作流vo
     * @return BaseResponse
     */
    BaseResponse updateWorkflow(WorkflowVO workflowVO);

    /**
     * 修改工作流禁用状态
     *
     * @param id         工作流id
     * @param isDisabled 是否已禁用(0:未禁用;1:已禁用)
     * @return true/false
     */
    boolean updateDisableState(Long id, Integer isDisabled);
}
