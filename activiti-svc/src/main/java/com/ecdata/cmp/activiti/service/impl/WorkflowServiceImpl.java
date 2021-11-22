package com.ecdata.cmp.activiti.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.activiti.dto.vo.WorkflowStepVO;
import com.ecdata.cmp.activiti.dto.vo.WorkflowVO;
import com.ecdata.cmp.activiti.entity.Workflow;
import com.ecdata.cmp.activiti.mapper.WorkflowMapper;
import com.ecdata.cmp.activiti.service.IWorkflowService;
import com.ecdata.cmp.activiti.service.IWorkflowStepRightService;
import com.ecdata.cmp.activiti.service.IWorkflowStepService;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Service
public class WorkflowServiceImpl extends ServiceImpl<WorkflowMapper, Workflow> implements IWorkflowService {
    /**
     * 工作流步骤服务
     */
    @Autowired
    private IWorkflowStepService workflowStepService;
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
    public List<WorkflowVO> transform(List<Workflow> workflowList) {
        List<WorkflowVO> workflowVOList = new ArrayList<>();
        if (workflowList != null && workflowList.size() > 0) {
            for (Workflow workflow : workflowList) {
                WorkflowVO workflowVO = new WorkflowVO();
                BeanUtils.copyProperties(workflow, workflowVO);
                workflowVOList.add(workflowVO);
            }
        }
        return workflowVOList;
    }

    @Override
    public WorkflowVO getDetail(Long id) {
        WorkflowVO workflowVO = new WorkflowVO();
        Workflow workflow = this.getById(id);
        BeanUtils.copyProperties(workflow, workflowVO);
        workflowVO.setWorkflowStepVOList(workflowStepService.listDetail(id));
        return workflowVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResponse addWorkflow(WorkflowVO workflowVO) {
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_FAIL);
        Long userId = Sign.getUserId();

        if (StringUtils.isEmpty(workflowVO.getWorkflowName())) {
            baseResponse.setMessage("缺少工作流名");
            return baseResponse;
        }
        Workflow workflow = new Workflow();
        BeanUtils.copyProperties(workflowVO, workflow);
        Long workflowId = SnowFlakeIdGenerator.getInstance().nextId();
        workflow.setId(workflowId).setCreateUser(userId).setCreateTime(DateUtil.getNow());

        List<WorkflowStepVO> workflowStepVOList = workflowVO.getWorkflowStepVOList();
        baseResponse = this.workflowStepService.addWorkflowSteps(workflowId, workflowStepVOList);
        if (!baseResponse.getCode().equals(ResultEnum.DEFAULT_SUCCESS.getCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return baseResponse;
        }

        if (this.save(workflow)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("新增工作流失败");
        }

        return baseResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResponse updateWorkflow(WorkflowVO workflowVO) {
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_FAIL);
        Long workflowId = workflowVO.getId();
        if (workflowId == null) {
            baseResponse.setMessage("缺少工作流id");
            return baseResponse;
        }
        if (StringUtils.isEmpty(workflowVO.getWorkflowName())) {
            baseResponse.setMessage("缺少工作流名");
            return baseResponse;
        }
        Long userId = Sign.getUserId();
        Workflow workflow = new Workflow();
        BeanUtils.copyProperties(workflowVO, workflow);
        workflow.setUpdateUser(userId).setUpdateTime(DateUtil.getNow());

        List<WorkflowStepVO> workflowStepVOList = workflowVO.getWorkflowStepVOList();
        baseResponse = this.workflowStepService.updateWorkflowSteps(workflowId, workflowStepVOList);
        if (!baseResponse.getCode().equals(ResultEnum.DEFAULT_SUCCESS.getCode())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return baseResponse;
        }

        if (this.updateById(workflow)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改工作流失败");
        }

        return baseResponse;
    }

    @Override
    public boolean updateDisableState(Long id, Integer isDisabled) {
        Workflow workflow = new Workflow();
        workflow.setId(id).setIsDisabled(isDisabled).setUpdateUser(Sign.getUserId()).setUpdateTime(DateUtil.getNow());
        return this.updateById(workflow);
    }
}
