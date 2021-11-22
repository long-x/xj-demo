package com.ecdata.cmp.activiti.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskCandidateVO;
import com.ecdata.cmp.activiti.entity.WorkflowTaskCandidate;
import com.ecdata.cmp.activiti.mapper.WorkflowTaskCandidateMapper;
import com.ecdata.cmp.activiti.service.IActivitiService;
import com.ecdata.cmp.activiti.service.IWorkflowTaskCandidateService;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020/4/17
 */
@Service
public class WorkflowTaskCandidateServiceImpl
        extends ServiceImpl<WorkflowTaskCandidateMapper, WorkflowTaskCandidate> implements IWorkflowTaskCandidateService {

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public List<WorkflowTaskCandidateVO> transform(List<WorkflowTaskCandidate> workflowTaskCandidateList) {
        List<WorkflowTaskCandidateVO> workflowTaskCandidateVOList = new ArrayList<>();
        if (workflowTaskCandidateList != null && workflowTaskCandidateList.size() > 0) {
            for (WorkflowTaskCandidate workflowTaskCandidate : workflowTaskCandidateList) {
                WorkflowTaskCandidateVO workflowTaskCandidateVO = new WorkflowTaskCandidateVO();
                BeanUtils.copyProperties(workflowTaskCandidate, workflowTaskCandidateVO);
                workflowTaskCandidateVOList.add(workflowTaskCandidateVO);
            }
        }
        return workflowTaskCandidateVOList;
    }

    @Override
    public List<WorkflowTaskCandidate> listCandidate(Long workflowTaskId, Integer isApproved) {
        QueryWrapper<WorkflowTaskCandidate> queryWrapper = new QueryWrapper<>();
        if (workflowTaskId != null) {
            queryWrapper.lambda().eq(WorkflowTaskCandidate::getWorkflowTaskId, workflowTaskId);
        }
        if (isApproved != null) {
            queryWrapper.lambda().eq(WorkflowTaskCandidate::getIsApproved, isApproved);
        }
        return this.list(queryWrapper);
    }

    @Override
    public boolean addCandidateBatch(Long workflowTaskId, List<WorkflowTaskCandidateVO> candidateVOList) {
        if (candidateVOList != null && candidateVOList.size() > 0) {
            List<WorkflowTaskCandidate> candidateList = new ArrayList<>();
            Long userId = Sign.getUserId();
            Date date = DateUtil.getNow();
            for (WorkflowTaskCandidateVO candidateVO : candidateVOList) {
                WorkflowTaskCandidate candidate = new WorkflowTaskCandidate();
                BeanUtils.copyProperties(candidateVO, candidate);
                candidate.setId(SnowFlakeIdGenerator.getInstance().nextId()).setWorkflowTaskId(workflowTaskId)
                        .setRelatedParty(candidate.getType() + IActivitiService.SEPARATOR + candidate.getRelateId())
                        .setCreateUser(userId).setCreateTime(date);
                candidateList.add(candidate);
            }
            return this.saveBatch(candidateList);
        } else {
            return true;
        }
    }

    @Override
    public boolean updateCandidateBatch(Long workflowTaskId, List<WorkflowTaskCandidateVO> candidateVOList) {
        QueryWrapper<WorkflowTaskCandidate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WorkflowTaskCandidate::getWorkflowTaskId, workflowTaskId);
        if (candidateVOList != null && candidateVOList.size() > 0) {
            List<WorkflowTaskCandidate> addList = new ArrayList<>();
            List<WorkflowTaskCandidate> updateList = new ArrayList<>();
            List<WorkflowTaskCandidate> deleteList = new ArrayList<>();

            List<WorkflowTaskCandidate> oldList = this.list(queryWrapper);
            if (oldList != null && oldList.size() > 0) {
                deleteList.addAll(oldList);
            }
            Long userId = Sign.getUserId();
            Date date = DateUtil.getNow();
            for (WorkflowTaskCandidateVO workflowTaskCandidateVO : candidateVOList) {
                WorkflowTaskCandidate workflowTaskCandidate = new WorkflowTaskCandidate();
                BeanUtils.copyProperties(workflowTaskCandidateVO, workflowTaskCandidate);
                Long id = workflowTaskCandidate.getId();
                if (id == null) {
                    id = SnowFlakeIdGenerator.getInstance().nextId();
                    workflowTaskCandidate.setId(id).setCreateUser(userId).setCreateTime(date);
                    addList.add(workflowTaskCandidate);
                } else {
                    for (int j = 0; j < deleteList.size(); j++) {
                        if (id.equals(deleteList.get(j).getId())) {
                            deleteList.remove(j);
                            break;
                        }
                    }
                    workflowTaskCandidate.setUpdateUser(userId).setUpdateTime(DateUtil.getNow());
                    updateList.add(workflowTaskCandidate);
                }
                workflowTaskCandidate.setWorkflowTaskId(workflowTaskId)
                        .setRelatedParty(workflowTaskCandidate.getType() + IActivitiService.SEPARATOR + workflowTaskCandidate.getRelateId());
            }

            if (addList.size() > 0 && !this.saveBatch(addList)) {
                return false;
            }
            if (updateList.size() > 0 && !this.updateBatchById(updateList)) {
                return false;
            }
            if (deleteList.size() > 0) {
                List<Long> ids = new ArrayList<>();
                for (WorkflowTaskCandidate candidate : deleteList) {
                    ids.add(candidate.getId());
                }
                return this.removeByIds(ids);
            }
            return true;
        } else {
            return this.remove(queryWrapper);
        }
    }

    @Override
    public List<WorkflowTaskCandidateVO> getChildren(Long parentId) {
        QueryWrapper<WorkflowTaskCandidate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WorkflowTaskCandidate::getParentId, parentId);
        List<WorkflowTaskCandidate> candidateList = this.list(queryWrapper);
        return this.transform(candidateList);
    }
}
