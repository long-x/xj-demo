package com.ecdata.cmp.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.activiti.entity.WorkflowStepRight;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.RightDTO;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
public interface IWorkflowStepRightService extends IService<WorkflowStepRight> {

    /**
     * 查询用户权限
     *
     * @param userId 用户id
     * @return 候选组列表
     */
    List<String> getUserRight(Long userId);

    /**
     * 修改权限
     *
     * @param rightDTO 权限
     * @return true/false
     */
    boolean changeRight(RightDTO rightDTO);

    /**
     * 生成流程步骤权限
     *
     * @param rightDTO 权限
     * @return List<WorkflowStepRight>
     */
    List<WorkflowStepRight> generateRight(RightDTO rightDTO);

    /**
     * 获取权限
     *
     * @param stepId 工作流步骤id
     * @return RightDTO
     */
    RightDTO getRight(Long stepId);


    /**
     * 获取工作流步骤权限
     *
     * @param stepId 工作流步骤id
     * @return List<WorkflowStepRight>
     */
    List<WorkflowStepRight> getWorkflowStepRight(Long stepId);

    /**
     * 添加流程步骤权限
     *
     * @param stepName 步骤名
     * @param rightDTO 权限
     * @return BaseResponse
     */
    BaseResponse addWorkflowStepRights(String stepName, RightDTO rightDTO);

    /**
     * 添加流程步骤权限
     *
     * @param stepName 步骤名
     * @param rightDTO 权限
     * @return BaseResponse
     */
    BaseResponse updateWorkflowStepRights(String stepName, RightDTO rightDTO);
}
