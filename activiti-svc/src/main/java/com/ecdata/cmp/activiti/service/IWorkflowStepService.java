package com.ecdata.cmp.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.activiti.dto.vo.WorkflowStepVO;
import com.ecdata.cmp.activiti.entity.WorkflowStep;
import com.ecdata.cmp.common.api.BaseResponse;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
public interface IWorkflowStepService extends IService<WorkflowStep> {
    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 获取工作流的步骤详情
     *
     * @param workflowId 工作流id
     * @return List<WorkflowStepVO>
     */
    List<WorkflowStepVO> listDetail(Long workflowId);

    /**
     * 获取上个序号
     *
     * @param workflowId  工作流id
     * @param currentSort 当前序号
     * @return 上一步骤
     */
    WorkflowStep getBackWorkflowStep(Long workflowId, Integer currentSort);

    /**
     * 获取下个序号
     *
     * @param workflowId  工作流id
     * @param currentSort 当前序号
     * @return 下一步骤
     */
    WorkflowStep getNextWorkflowStep(Long workflowId, Integer currentSort);

    /**
     * 获取工作流步骤
     *
     * @param workflowId 工作流id
     * @param sort       序号
     * @return WorkflowStep
     */
    WorkflowStep getWorkflowStep(Long workflowId, Integer sort);

    /**
     * 添加工作流步骤
     *
     * @param workflowId         工作流id
     * @param workflowStepVOList 工作流步骤vo列表
     * @return BaseResponse
     */
    BaseResponse addWorkflowSteps(Long workflowId, List<WorkflowStepVO> workflowStepVOList);

    /**
     * 更新工作流步骤
     *
     * @param workflowId         工作流id
     * @param workflowStepVOList 工作流步骤vo列表
     * @return BaseResponse
     */
    BaseResponse updateWorkflowSteps(Long workflowId, List<WorkflowStepVO> workflowStepVOList);
}
