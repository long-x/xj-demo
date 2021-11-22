package com.ecdata.cmp.activiti.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.activiti.dto.vo.WorkflowStepVO;
import com.ecdata.cmp.activiti.entity.WorkflowStep;
import com.ecdata.cmp.activiti.mapper.WorkflowStepMapper;
import com.ecdata.cmp.activiti.service.IWorkflowStepRightService;
import com.ecdata.cmp.activiti.service.IWorkflowStepService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.constant.Constants;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.RightDTO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Service
public class WorkflowStepServiceImpl extends ServiceImpl<WorkflowStepMapper, WorkflowStep> implements IWorkflowStepService {

    /**
     * 工作流步骤权限服务
     */
    @Autowired
    private IWorkflowStepRightService workflowStepRightService;

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public List<WorkflowStepVO> listDetail(Long workflowId) {
        List<WorkflowStepVO> retList = new ArrayList<>();
        QueryWrapper<WorkflowStep> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WorkflowStep::getWorkflowId, workflowId).orderByAsc(WorkflowStep::getSort);
        List<WorkflowStep> stepList = this.list(queryWrapper);
        if (stepList != null && stepList.size() > 0) {
            for (WorkflowStep step : stepList) {
                WorkflowStepVO stepVO = new WorkflowStepVO();
                BeanUtils.copyProperties(step, stepVO);
                stepVO.setRightDTO(workflowStepRightService.getRight(step.getId()));
                retList.add(stepVO);
            }
        }
        return retList;
    }

    @Override
    public WorkflowStep getBackWorkflowStep(Long workflowId, Integer currentSort) {
        QueryWrapper<WorkflowStep> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WorkflowStep::getWorkflowId, workflowId)
                .lt(WorkflowStep::getSort, currentSort)
                .orderByDesc(WorkflowStep::getSort);
        List<WorkflowStep> stepList = this.list(queryWrapper);
        if (stepList != null && stepList.size() > 0) {
            return stepList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public WorkflowStep getNextWorkflowStep(Long workflowId, Integer currentSort) {
        QueryWrapper<WorkflowStep> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WorkflowStep::getWorkflowId, workflowId)
                .gt(WorkflowStep::getSort, currentSort)
                .orderByAsc(WorkflowStep::getSort);
        List<WorkflowStep> stepList = this.list(queryWrapper);
        if (stepList != null && stepList.size() > 0) {
            return stepList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public WorkflowStep getWorkflowStep(Long workflowId, Integer sort) {
        QueryWrapper<WorkflowStep> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(WorkflowStep::getWorkflowId, workflowId)
                .eq(WorkflowStep::getSort, sort)
                .orderByDesc(WorkflowStep::getCreateTime);
        List<WorkflowStep> stepList = this.list(queryWrapper);
        if (stepList != null && stepList.size() > 0) {
            return stepList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public BaseResponse addWorkflowSteps(Long workflowId, List<WorkflowStepVO> workflowStepVOList) {
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_FAIL);
        if (workflowStepVOList == null || workflowStepVOList.size() == 0) {
            baseResponse.setMessage("缺少工作流步骤");
            return baseResponse;
        }
        int size = workflowStepVOList.size();
        if (size == 1) {
            baseResponse.setMessage("缺少审批步骤");
            return baseResponse;
        }
        Long userId = Sign.getUserId();
        List<WorkflowStep> stepList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            WorkflowStepVO workflowStepVO = workflowStepVOList.get(i);
            Integer sort = i + 1;
            WorkflowStep workflowStep = new WorkflowStep();
            BeanUtils.copyProperties(workflowStepVO, workflowStep);
            Long stepId = SnowFlakeIdGenerator.getInstance().nextId();
            workflowStep.setId(stepId).setWorkflowId(workflowId).setSort(sort).setCreateUser(userId).setCreateTime(DateUtil.getNow());
            if (StringUtils.isEmpty(workflowStep.getStepName())) {
                workflowStep.setStepName("步骤" + sort);
            }
            this.setOption(workflowStep, sort == size);

            // 步骤1默认处理人为自己，没有审核人
            if (sort != 1) {
                RightDTO rightDTO = workflowStepVO.getRightDTO();
                rightDTO.setId(stepId);
                baseResponse = workflowStepRightService.addWorkflowStepRights(workflowStep.getStepName(), rightDTO);

                if (!baseResponse.getCode().equals(ResultEnum.DEFAULT_SUCCESS.getCode())) {
                    return baseResponse;
                }
            }
            stepList.add(workflowStep);
        }

        if (this.saveBatch(stepList)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("新增工作流步骤失败");
        }

        return baseResponse;
    }

    private void setOption(WorkflowStep workflowStep, boolean lastFlag) {
        Integer sort = workflowStep.getSort();
        if (sort == null) {
            return;
        }
        if (sort == 1) {
            workflowStep.setOptionOne("提交");
            workflowStep.setOptionTwo("放弃");
            workflowStep.setOptionThree("");
        } else if (sort == Constants.TWO) {
            if (lastFlag) {
                workflowStep.setOptionOne("结束");
                workflowStep.setOptionTwo("拒绝");
                workflowStep.setOptionThree("");
            } else {
                workflowStep.setOptionOne("同意");
                workflowStep.setOptionTwo("拒绝");
                workflowStep.setOptionThree("");
            }
        } else if (lastFlag) {
            workflowStep.setOptionOne("结束");
            workflowStep.setOptionTwo("拒绝");
            workflowStep.setOptionThree("驳回");
        } else {
            workflowStep.setOptionOne("同意");
            workflowStep.setOptionTwo("拒绝");
            workflowStep.setOptionThree("驳回");
        }
    }

    @Override
    public BaseResponse updateWorkflowSteps(Long workflowId, List<WorkflowStepVO> workflowStepVOList) {
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_FAIL);
        if (workflowStepVOList == null || workflowStepVOList.size() == 0) {
            baseResponse.setMessage("缺少工作流步骤");
            return baseResponse;
        }
        int size = workflowStepVOList.size();
        if (size == 1) {
            baseResponse.setMessage("缺少审批步骤");
            return baseResponse;
        }

        List<WorkflowStep> addList = new ArrayList<>();
        List<WorkflowStep> updateList = new ArrayList<>();
        List<WorkflowStep> deleteList = new ArrayList<>();

        QueryWrapper<WorkflowStep> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WorkflowStep::getWorkflowId, workflowId);
        List<WorkflowStep> oldList = this.list(queryWrapper);
        if (oldList != null && oldList.size() > 0) {
            deleteList.addAll(oldList);
        }
        Long userId = Sign.getUserId();
        for (int i = 0; i < size; i++) {
            WorkflowStepVO workflowStepVO = workflowStepVOList.get(i);
            Integer sort = i + 1;
            WorkflowStep workflowStep = new WorkflowStep();
            BeanUtils.copyProperties(workflowStepVO, workflowStep);
            Long stepId = workflowStep.getId();
            if (stepId == null) {
                stepId = SnowFlakeIdGenerator.getInstance().nextId();
                workflowStep.setId(stepId).setCreateUser(userId).setCreateTime(DateUtil.getNow());
                addList.add(workflowStep);
            } else {
                for (int j = 0; j < deleteList.size(); j++) {
                    if (stepId.equals(deleteList.get(j).getId())) {
                        deleteList.remove(j);
                        break;
                    }
                }
                workflowStep.setUpdateUser(userId).setUpdateTime(DateUtil.getNow());
                updateList.add(workflowStep);
            }
            workflowStep.setWorkflowId(workflowId).setSort(sort);
            if (StringUtils.isEmpty(workflowStep.getStepName())) {
                workflowStep.setStepName("步骤" + sort);
            }
            this.setOption(workflowStep, sort == size);

            // 步骤1默认处理人为自己，没有审核人
            if (i != 0) {
                RightDTO rightDTO = workflowStepVO.getRightDTO();
                rightDTO.setId(stepId);
                String stepName = workflowStep.getStepName();
                baseResponse = workflowStepRightService.addWorkflowStepRights(stepName, rightDTO);
                if (!baseResponse.getCode().equals(ResultEnum.DEFAULT_SUCCESS.getCode())) {
                    return baseResponse;
                }
            }
        }

        if (addList.size() > 0 && !this.saveBatch(addList)) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改工作流步骤失败");
            return baseResponse;
        }
        if (updateList.size() > 0 && !this.updateBatchById(updateList)) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改工作流步骤失败");
            return baseResponse;
        }
        if (deleteList.size() > 0) {
            List<Long> ids = new ArrayList<>();
            for (WorkflowStep step : deleteList) {
                ids.add(step.getId());
            }
            if (!this.removeByIds(ids)) {
                baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                baseResponse.setMessage("修改工作流步骤失败");
                return baseResponse;
            }
        }

        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        return baseResponse;
    }
}
