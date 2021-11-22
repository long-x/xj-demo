package com.ecdata.cmp.activiti.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.activiti.entity.WorkflowStepRight;
import com.ecdata.cmp.activiti.mapper.WorkflowStepRightMapper;
import com.ecdata.cmp.activiti.service.IActivitiService;
import com.ecdata.cmp.activiti.service.IWorkflowStepRightService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.constant.Constants;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.RightDTO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Service
public class WorkflowStepRightServiceImpl extends ServiceImpl<WorkflowStepRightMapper, WorkflowStepRight> implements IWorkflowStepRightService {

    /**
     * 用户客户端
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserClient userClient;

    private void addToGroups(List<String> groups, List<Long> idList, Integer type) {
        if (idList != null && idList.size() > 0) {
            for (Long id : idList) {
                groups.add(type + IActivitiService.SEPARATOR + id);
            }
        }
    }

    @Override
    public List<String> getUserRight(Long userId) {
        List<String> groups = new ArrayList<>();
        String token = AuthContext.getAuthz();
        RightDTO rightDTO = userClient.getUserRight(token, userId).getData();
        this.addToGroups(groups, rightDTO.getUserIdList(), Constants.ONE);
        this.addToGroups(groups, rightDTO.getRoleIdList(), Constants.TWO);
        this.addToGroups(groups, rightDTO.getDepartmentIdList(), Constants.THREE);
        this.addToGroups(groups, rightDTO.getProjectIdList(), Constants.FOUR);
        this.addToGroups(groups, rightDTO.getBusinessGroupIdList(), Constants.FIVE);
        return groups;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeRight(RightDTO rightDTO) {
        boolean flag = true;
        Long stepId = rightDTO.getId();
        QueryWrapper<WorkflowStepRight> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WorkflowStepRight::getStepId, stepId);
        this.remove(queryWrapper);
        List<WorkflowStepRight> rightList = new ArrayList<>();
        this.generateRight(rightDTO);
        if (rightList.size() > 0) {
            flag = this.saveBatch(rightList);
        }
        return flag;
    }

    @Override
    public List<WorkflowStepRight> generateRight(RightDTO rightDTO) {
        List<WorkflowStepRight> rightList = new ArrayList<>();
        Long stepId = rightDTO.getId();
        this.generateRight(rightList, stepId, rightDTO.getUserIdList(), Constants.ONE);
        this.generateRight(rightList, stepId, rightDTO.getRoleIdList(), Constants.TWO);
        this.generateRight(rightList, stepId, rightDTO.getDepartmentIdList(), Constants.THREE);
        this.generateRight(rightList, stepId, rightDTO.getProjectIdList(), Constants.FOUR);
        this.generateRight(rightList, stepId, rightDTO.getBusinessGroupIdList(), Constants.FIVE);
        return rightList;
    }

    private void generateRight(List<WorkflowStepRight> rightList, Long stepId, List<Long> idList, Integer type) {
        if (idList != null && idList.size() > 0) {
            Long userId = Sign.getUserId();
            for (Long id : idList) {
                WorkflowStepRight right = new WorkflowStepRight();
                right.setId(SnowFlakeIdGenerator.getInstance().nextId()).setStepId(stepId).setRelateId(id).setType(type)
                        .setCreateUser(userId).setCreateTime(DateUtil.getNow());
                rightList.add(right);
            }
        }
    }

    @Override
    public RightDTO getRight(Long stepId) {
        RightDTO rightDTO = new RightDTO();
        List<Long> uidList = new ArrayList<>();
        List<Long> ridList = new ArrayList<>();
        List<Long> didList = new ArrayList<>();
        List<Long> pidList = new ArrayList<>();
        List<Long> bgidList = new ArrayList<>();
        List<WorkflowStepRight> rightList = this.getWorkflowStepRight(stepId);
        if (rightList != null && rightList.size() > 0) {
            for (WorkflowStepRight right : rightList) {
                Integer type = right.getType();
                if (type == null) {
                    continue;
                }
                Long rid = right.getRelateId();
                switch (type) {
                    case Constants.ONE:
                        uidList.add(rid);
                        break;
                    case Constants.TWO:
                        ridList.add(rid);
                        break;
                    case Constants.THREE:
                        didList.add(rid);
                        break;
                    case Constants.FOUR:
                        pidList.add(rid);
                        break;
                    case Constants.FIVE:
                        bgidList.add(rid);
                        break;
                    default:
                        break;
                }
            }
        }
        rightDTO.setUserIdList(uidList);
        rightDTO.setRoleIdList(ridList);
        rightDTO.setDepartmentIdList(didList);
        rightDTO.setProjectIdList(pidList);
        rightDTO.setBusinessGroupIdList(bgidList);
        return rightDTO;
    }

    @Override
    public List<WorkflowStepRight> getWorkflowStepRight(Long stepId) {
        QueryWrapper<WorkflowStepRight> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WorkflowStepRight::getStepId, stepId);
        return this.list(queryWrapper);
    }

    @Override
    public BaseResponse addWorkflowStepRights(String stepName, RightDTO rightDTO) {
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_FAIL);
        Long stepId = rightDTO.getId();
        if (stepId == null) {
            baseResponse.setMessage(stepName + "未分配id");
            return baseResponse;
        }
        List<WorkflowStepRight> rights = this.generateRight(rightDTO);
        if (rights == null || rights.size() == 0) {
            baseResponse.setMessage(stepName + "未分配审批者");
            return baseResponse;
        }

        QueryWrapper<WorkflowStepRight> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WorkflowStepRight::getStepId, stepId);
        this.remove(queryWrapper);
        if (this.saveBatch(rights)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        } else {
            baseResponse.setMessage(stepName + "分配审批者失败");
        }

        return baseResponse;
    }

    @Override
    public BaseResponse updateWorkflowStepRights(String stepName, RightDTO rightDTO) {
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_FAIL);
        List<WorkflowStepRight> rights = this.generateRight(rightDTO);
        if (rights == null || rights.size() == 0) {
            baseResponse.setMessage(stepName + "未分配审批者");
            return baseResponse;
        }
        QueryWrapper<WorkflowStepRight> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WorkflowStepRight::getStepId, rightDTO.getId());
        this.remove(queryWrapper);

        if (this.saveBatch(rights)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        } else {
            baseResponse.setMessage(stepName + "分配审批者失败");
        }
        return baseResponse;
    }
}
