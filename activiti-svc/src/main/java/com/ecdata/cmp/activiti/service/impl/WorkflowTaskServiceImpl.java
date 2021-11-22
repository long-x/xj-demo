package com.ecdata.cmp.activiti.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskCandidateVO;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskVO;
import com.ecdata.cmp.activiti.entity.WorkflowTask;
import com.ecdata.cmp.activiti.entity.WorkflowTaskCandidate;
import com.ecdata.cmp.activiti.mapper.WorkflowTaskMapper;
import com.ecdata.cmp.activiti.service.IWorkflowTaskCandidateService;
import com.ecdata.cmp.activiti.service.IWorkflowTaskService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Service
public class WorkflowTaskServiceImpl
        extends ServiceImpl<WorkflowTaskMapper, WorkflowTask> implements IWorkflowTaskService {

    /**
     * 工作流任务候选服务
     */
    @Autowired
    private IWorkflowTaskCandidateService workflowTaskCandidateService;

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public List<WorkflowTaskVO> transform(List<WorkflowTask> workflowTaskList) {
        List<WorkflowTaskVO> workflowTaskVOList = new ArrayList<>();
        if (workflowTaskList != null && workflowTaskList.size() > 0) {
            for (WorkflowTask workflowTask : workflowTaskList) {
                WorkflowTaskVO workflowTaskVO = new WorkflowTaskVO();
                BeanUtils.copyProperties(workflowTask, workflowTaskVO);
                workflowTaskVOList.add(workflowTaskVO);
            }
        }
        return workflowTaskVOList;
    }

    @Transactional
    @Override
    public BaseResponse addWorkflowTask(WorkflowTaskVO workflowTaskVO) {
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_FAIL);

        if (workflowTaskVO.getWorkflowId() == null) {
            baseResponse.setMessage("缺少工作流id");
            return baseResponse;
        }
        Integer step = workflowTaskVO.getWorkflowStep();
        if (step == null) {
            baseResponse.setMessage("缺少工作流步骤");
            return baseResponse;
        }
        String processInstanceId = workflowTaskVO.getProcessInstanceId();
        if (StringUtils.isEmpty(processInstanceId)) {
            baseResponse.setMessage("缺少流程实例id");
            return baseResponse;
        }
        QueryWrapper<WorkflowTask> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.lambda()
                .eq(WorkflowTask::getProcessInstanceId, processInstanceId)
                .eq(WorkflowTask::getWorkflowStep, step);
        if (this.count(taskQueryWrapper) > 0) {
            baseResponse.setMessage("工作流任务已存在");
            return baseResponse;
        }
        List<WorkflowTaskCandidateVO> candidateList = workflowTaskVO.getCandidateList();
        if (candidateList == null) {
            baseResponse.setMessage("缺少候选");
            return baseResponse;
        }
        for (WorkflowTaskCandidateVO candidateVO : candidateList) {
            if (candidateVO.getRelateId() == null) {
                baseResponse.setMessage("缺少候选id");
                return baseResponse;
            }
        }
        WorkflowTask workflowTask = new WorkflowTask();
        BeanUtils.copyProperties(workflowTaskVO, workflowTask);
        Long workflowTaskId = SnowFlakeIdGenerator.getInstance().nextId();
        workflowTask.setId(workflowTaskId).setCreateUser(Sign.getUserId()).setCreateTime(DateUtil.getNow());

        if (this.save(workflowTask)) {
            if (this.workflowTaskCandidateService.addCandidateBatch(workflowTaskId, candidateList)) {
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("新增工作流任务成功");
            } else {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("新增工作流任务失败");
            }
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("新增工作流任务失败");
        }
        return baseResponse;
    }

    @Transactional
    @Override
    public BaseResponse updateWorkflowTask(WorkflowTaskVO workflowTaskVO) {
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_FAIL);
        Long workflowTaskId = workflowTaskVO.getId();
        if (workflowTaskId == null) {
            baseResponse.setMessage("缺少工作流任务id");
            return baseResponse;
        }
        if (workflowTaskVO.getWorkflowId() == null) {
            baseResponse.setMessage("缺少工作流id");
            return baseResponse;
        }
        Integer step = workflowTaskVO.getWorkflowStep();
        if (step == null) {
            baseResponse.setMessage("缺少工作流步骤");
            return baseResponse;
        }
        String processInstanceId = workflowTaskVO.getProcessInstanceId();
        if (StringUtils.isEmpty(processInstanceId)) {
            baseResponse.setMessage("缺少流程实例id");
            return baseResponse;
        }
        QueryWrapper<WorkflowTask> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.lambda()
                .ne(WorkflowTask::getId, workflowTaskId)
                .eq(WorkflowTask::getProcessInstanceId, processInstanceId)
                .eq(WorkflowTask::getWorkflowStep, step);
        if (this.count(taskQueryWrapper) > 0) {
            baseResponse.setMessage("工作流任务已存在");
            return baseResponse;
        }
        List<WorkflowTaskCandidateVO> candidateList = workflowTaskVO.getCandidateList();
        if (candidateList == null) {
            baseResponse.setMessage("缺少候选");
            return baseResponse;
        }
        for (WorkflowTaskCandidateVO candidateVO : candidateList) {
            if (candidateVO.getRelateId() == null) {
                baseResponse.setMessage("缺少候选id");
                return baseResponse;
            }
        }
        WorkflowTask workflowTask = new WorkflowTask();
        BeanUtils.copyProperties(workflowTaskVO, workflowTask);
        workflowTask.setUpdateUser(Sign.getUserId()).setUpdateTime(DateUtil.getNow());

        if (this.updateById(workflowTask)) {
            if (this.workflowTaskCandidateService.updateCandidateBatch(workflowTaskId, candidateList)) {
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("修改工作流任务成功");
            } else {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("修改工作流任务失败");
            }
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改工作流任务失败");
        }

        return baseResponse;
    }

    @Override
    public boolean approveTask(String processInstanceId, Integer processWorkflowStep, String taskId,
                               String outcome, Long userId, String userName, String comment) {
        boolean completeFlag = true;
        Date date = DateUtil.getNow();
        QueryWrapper<WorkflowTask> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.lambda()
                .eq(WorkflowTask::getProcessInstanceId, processInstanceId)
                .eq(WorkflowTask::getWorkflowStep, processWorkflowStep);
        List<WorkflowTask> taskList = this.list(taskQueryWrapper);
        if (taskList != null && taskList.size() > 0) {
            WorkflowTask workflowTask = taskList.get(0);
            if (workflowTask.getType() == 1) {
                // 单人可决定
                workflowTask.setAssigneeId(userId).setAssigneeName(userName).setOutcome(outcome).setComment(comment).setIsApproved(1);
                this.updateById(workflowTask);
            } else {
                // 一票否决
                List<WorkflowTaskCandidate> candidateList = this.workflowTaskCandidateService.listCandidate(workflowTask.getId(), 0);
                if (candidateList != null && candidateList.size() > 0) {
                    for (WorkflowTaskCandidate candidate : candidateList) {
                        if (userId.equals(candidate.getRelateId())) {
                            candidate.setOutcome(outcome).setComment(comment).setIsApproved(1).setUpdateUser(userId).setUpdateTime(date);
                            this.workflowTaskCandidateService.updateById(candidate);
                            if (candidateList.size() == 1) {
                                workflowTask.setIsApproved(1);
                            }
                        } else {
                            completeFlag = false;
                        }
                    }
                }
                if ("放弃".equals(outcome) || "拒绝".equals(outcome) || "驳回".equals(outcome)) {
                    completeFlag = true;
                }
            }
            workflowTask.setTaskId(taskId).setUpdateUser(userId).setUpdateTime(date);
            this.updateById(workflowTask);
        }
        return completeFlag;
    }

    @Override
    public List<Map<String, Object>> queryBacklog(List<String> list) {
        return baseMapper.queryBacklog(list);
    }

    @Override
    public List<String> queryApproved(Long userId) {
        return baseMapper.queryApproved(userId);
    }
}
