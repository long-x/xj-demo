package com.ecdata.cmp.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskCandidateVO;
import com.ecdata.cmp.activiti.entity.WorkflowTaskCandidate;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020/4/17
 */
public interface IWorkflowTaskCandidateService extends IService<WorkflowTaskCandidate> {
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
     * @param workflowTaskCandidateList 工作流任务候选列表
     * @return List<WorkflowTaskVO>
     */
    List<WorkflowTaskCandidateVO> transform(List<WorkflowTaskCandidate> workflowTaskCandidateList);

    /**
     * 获取工作流任务候选列表
     *
     * @param workflowTaskId 工作流任务id
     * @param isApproved     是否已审批(0:未审批;1:已审批)
     * @return 工作流任务候选列表
     */
    List<WorkflowTaskCandidate> listCandidate(Long workflowTaskId, Integer isApproved);

    /**
     * 批量增加候选
     *
     * @param workflowTaskId  工作流任务id
     * @param candidateVOList 候选列表
     * @return true/false
     */
    boolean addCandidateBatch(Long workflowTaskId, List<WorkflowTaskCandidateVO> candidateVOList);

    /**
     * 批量更新候选
     *
     * @param workflowTaskId  工作流任务id
     * @param candidateVOList 候选列表
     * @return true/false
     */
    boolean updateCandidateBatch(Long workflowTaskId, List<WorkflowTaskCandidateVO> candidateVOList);

    /**
     * 获取子列表
     *
     * @param parentId 父id
     * @return 子列表
     */
    List<WorkflowTaskCandidateVO> getChildren(Long parentId);
}
