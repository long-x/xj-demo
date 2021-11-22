package com.ecdata.cmp.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskVO;
import com.ecdata.cmp.activiti.entity.WorkflowTask;
import com.ecdata.cmp.common.api.BaseResponse;

import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2020/4/17
 */
public interface IWorkflowTaskService extends IService<WorkflowTask> {
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
     * @param workflowTaskList 工作流任务列表
     * @return List<WorkflowTaskVO>
     */
    List<WorkflowTaskVO> transform(List<WorkflowTask> workflowTaskList);

    /**
     * 添加工作流任务
     *
     * @param workflowTaskVO 工作流任务vo
     * @return BaseResponse
     */
    BaseResponse addWorkflowTask(WorkflowTaskVO workflowTaskVO);

    /**
     * 修改工作流任务
     *
     * @param workflowTaskVO 工作流任务vo
     * @return BaseResponse
     */
    BaseResponse updateWorkflowTask(WorkflowTaskVO workflowTaskVO);

    /**
     * 审核任务
     *
     * @param processInstanceId   流程实例id
     * @param processWorkflowStep 流程工作流步骤
     * @param taskId              任务id
     * @param outcome             结果
     * @param userId              用户id
     * @param userName            用户名
     * @param comment             意见
     * @return true:工作流任务审核完毕;false:工作流任务未审核完毕;
     */
    boolean approveTask(String processInstanceId, Integer processWorkflowStep, String taskId,
                        String outcome, Long userId, String userName, String comment);

    /**
     * 查询待办
     *
     * @param list 关联列表
     * @return List<Map < String ,   Object>>
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
