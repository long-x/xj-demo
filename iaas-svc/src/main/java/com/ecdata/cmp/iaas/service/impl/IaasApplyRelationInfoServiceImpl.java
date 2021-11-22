package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.activiti.client.ActivitiClient;
import com.ecdata.cmp.activiti.client.WorkflowTaskClient;
import com.ecdata.cmp.activiti.dto.request.CandidateRequest;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskCandidateVO;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskVO;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyRelationInfo;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyResource;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyRelationInfoParam;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyRelationInfoParams;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyRelationInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasNextApplyUserParam;
import com.ecdata.cmp.iaas.entity.dto.apply.ProgramSupportParam;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyCalculateMapper;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyNetworkPolicyMapper;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyOtherMapper;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyRelationInfoMapper;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyResourceMapper;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplySecurityServerMapper;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplySoftwareServerMapper;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyStorageMapper;
import com.ecdata.cmp.iaas.service.IaasApplyRelationInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author xuxiaojian
 * @date 2020/4/16 11:10
 */
@Service
public class IaasApplyRelationInfoServiceImpl extends ServiceImpl<IaasApplyRelationInfoMapper, IaasApplyRelationInfo> implements IaasApplyRelationInfoService {

    @Autowired
    private WorkflowTaskClient workflowTaskClient;

    @Autowired
    private ActivitiClient activitiClient;

    @Autowired
    private IaasApplyCalculateMapper iaasApplyCalculateMapper;

    @Autowired
    private IaasApplyNetworkPolicyMapper iaasApplyNetworkPolicyMapper;

    @Autowired
    private IaasApplySecurityServerMapper iaasApplySecurityServerMapper;

    @Autowired
    private IaasApplyStorageMapper iaasApplyStorageMapper;

    @Autowired
    private IaasApplySoftwareServerMapper iaasApplySoftwareServerMapper;

    @Autowired
    private IaasApplyResourceMapper iaasApplyResourceMapper;

    @Autowired
    private IaasApplyOtherMapper iaasApplyOtherMapper;

    @Override
    public void saveIaasApplyRelationInfo(IaasApplyRelationInfoParams params) {
        WorkflowTaskVO workflowTaskVO = new WorkflowTaskVO();
        workflowTaskVO.setWorkflowId(params.getWorkflowId());
        workflowTaskVO.setProcessInstanceId(params.getProcessInstanceId());
        workflowTaskVO.setWorkflowStep(params.getWorkflowStep() + 1);
        workflowTaskVO.setType(2);

        List<WorkflowTaskCandidateVO> candidateList = new ArrayList<>();

        for (IaasApplyRelationInfoParam param : params.getParamList()) {
            if (StringUtils.isBlank(param.getRelationInfo())) {
                continue;
            }
            WorkflowTaskCandidateVO candidateVO = new WorkflowTaskCandidateVO();
            candidateVO.setRelateId(param.getRelationUser());
            candidateVO.setRelateName(param.getRelationUserName());
            candidateVO.setType(1);
            candidateList.add(candidateVO);

            List<Long> idsList = Arrays.asList(param.getRelationInfo().split(",")).stream().map(s -> Long.valueOf(s.trim())).collect(toList());

            if (CollectionUtils.isNotEmpty(idsList)) {
                for (Long id : idsList) {
                    IaasApplyRelationInfo info = new IaasApplyRelationInfo();
                    info.setId(SnowFlakeIdGenerator.getInstance().nextId());
                    info.setApplyId(params.getApplyId());
                    info.setBusinessId(id);
                    info.setCreateUser(Sign.getUserId());
                    info.setCreateTime(DateUtil.getNow());
                    info.setProcessInstanceId(params.getProcessInstanceId());
                    info.setRelationUser(param.getRelationUser());
                    this.baseMapper.insert(info);
                }
            }
        }

        //下一级会签人员（审核人）
        if (CollectionUtils.isNotEmpty(candidateList)) {
            workflowTaskVO.setCandidateList(candidateList);
            workflowTaskClient.add(workflowTaskVO);
        }
    }

    @Override
    public void handleRelationInfo(Long id, Long applyId, String processInstanceId) {
        IaasApplyRelationInfo info = new IaasApplyRelationInfo();
        info.setId(SnowFlakeIdGenerator.getInstance().nextId());
        info.setApplyId(applyId);
        info.setBusinessId(id);
        info.setCreateUser(Sign.getUserId());
        info.setCreateTime(DateUtil.getNow());
        info.setProcessInstanceId(processInstanceId);
        info.setRelationUser(Sign.getUserId());
        this.baseMapper.insert(info);
    }

    @Override
    public void programSupport(ProgramSupportParam param) {
        //是否方案支持
        IaasApplyResource applyResource = new IaasApplyResource();
        applyResource.setId(param.getApplyId());
        applyResource.setIsProgramSupport("1");
        iaasApplyResourceMapper.updateById(applyResource);

        //方案支持人
        CandidateRequest candidate = new CandidateRequest();
        candidate.setCandidateId(param.getCandidateId());
        candidate.setTaskId(param.getTaskId());
        candidate.setType(1);
        activitiClient.addCandidate(candidate);
    }

    @Override
    public void nextApplyUser(IaasNextApplyUserParam param) {
        WorkflowTaskVO workflowTaskVO = new WorkflowTaskVO();
        workflowTaskVO.setWorkflowId(param.getWorkflowId());
        workflowTaskVO.setProcessInstanceId(param.getProcessInstanceId());
        workflowTaskVO.setWorkflowStep(param.getWorkflowStep() + 1);
        workflowTaskVO.setType(2);

        List<WorkflowTaskCandidateVO> candidateList = new ArrayList<>();

        WorkflowTaskCandidateVO candidateVO = new WorkflowTaskCandidateVO();
        candidateVO.setRelateId(param.getRelateId());
        candidateVO.setRelateName(param.getRelateName());
        candidateVO.setType(1);
        candidateList.add(candidateVO);

        workflowTaskVO.setCandidateList(candidateList);
        workflowTaskClient.add(workflowTaskVO);
    }

    @Override
    public String queryApplyUser(Long applyId) {
        List<IaasApplyRelationInfoVO> list = this.baseMapper.queryRelationInfoList(applyId, Sign.getUserId());

        if (CollectionUtils.isEmpty(list)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        Map<String, List<IaasApplyRelationInfoVO>> map = list.stream().collect(groupingBy(IaasApplyRelationInfoVO::getRelationUserName));

        for (Map.Entry<String, List<IaasApplyRelationInfoVO>> m : map.entrySet()) {
            builder.append(m.getKey() + "(");
            List<Long> businessIds = m.getValue().stream().map(IaasApplyRelationInfoVO::getBusinessId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(iaasApplyCalculateMapper.queryApplyBatchCalculate(businessIds))) {
                builder.append(" 计算");
            }
            if (CollectionUtils.isNotEmpty(iaasApplyNetworkPolicyMapper.queryBatchApplyNetworkPolicy(businessIds))) {
                builder.append(" 网络");
            }

            if (CollectionUtils.isNotEmpty(iaasApplySecurityServerMapper.queryBatchSecurityServer(businessIds))) {
                builder.append(" 安全");
            }

            if (CollectionUtils.isNotEmpty(iaasApplyStorageMapper.queryBatchApplyStorage(businessIds))) {
                builder.append(" 存储");
            }

            if (CollectionUtils.isNotEmpty(iaasApplySoftwareServerMapper.queryBatchApplySoftwareServer(businessIds))) {
                builder.append(" 软件");
            }

            if (CollectionUtils.isNotEmpty(iaasApplyOtherMapper.queryBatchApplyOther(businessIds))) {
                builder.append(" 配置");
            }

            builder.append(") ");
        }

        return builder.toString();
    }

    @Override
    public List<Map<String, String>> queryApplyUserId(Long applyId) {
        List<IaasApplyRelationInfoVO> list = this.baseMapper.queryRelationInfoList2(applyId, Sign.getUserId());

        List<Map<String, String>> lists = new ArrayList<>();

        if (CollectionUtils.isEmpty(list)) {
            return lists;
        }

        Map<Long, List<IaasApplyRelationInfoVO>> map = list.stream().collect(groupingBy(IaasApplyRelationInfoVO::getRelationUser));

        for (Map.Entry<Long, List<IaasApplyRelationInfoVO>> m : map.entrySet()) {
            StringBuilder builder = new StringBuilder();
            Map<String, String> map1 = new HashMap<>();
            map1.put("id", System.currentTimeMillis() + "");
            map1.put("relationUser", m.getKey().toString());
            List<Long> businessIds = m.getValue().stream().map(IaasApplyRelationInfoVO::getBusinessId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(iaasApplyCalculateMapper.queryApplyBatchCalculate(businessIds))) {
                builder.append(" 计算");
            }
            if (CollectionUtils.isNotEmpty(iaasApplyNetworkPolicyMapper.queryBatchApplyNetworkPolicy(businessIds))) {
                builder.append(" 网络");
            }

            if (CollectionUtils.isNotEmpty(iaasApplySecurityServerMapper.queryBatchSecurityServer(businessIds))) {
                builder.append(" 安全");
            }

            if (CollectionUtils.isNotEmpty(iaasApplyStorageMapper.queryBatchApplyStorage(businessIds))) {
                builder.append(" 存储");
            }

            if (CollectionUtils.isNotEmpty(iaasApplySoftwareServerMapper.queryBatchApplySoftwareServer(businessIds))) {
                builder.append(" 软件");
            }

            if (CollectionUtils.isNotEmpty(iaasApplyOtherMapper.queryBatchApplyOther(businessIds))) {
                builder.append(" 配置");
            }
            if (builder.length() > 0) {
                map1.put("relationInfo", (builder.toString().substring(1, builder.length())).replaceAll(" ", ","));
            }
            lists.add(map1);
        }

        return lists;
    }

    /**
     * 删除审核人(实施人员)
     *
     * @param applyId
     * @param userId
     */
    @Override
    public boolean deleteApplyUser(Long applyId, Long userId) {
        List<IaasApplyRelationInfoVO> iaasApplyRelationInfoVOS = baseMapper.queryRelationInfoList3(applyId, userId);
        try {
            for (IaasApplyRelationInfoVO vo : iaasApplyRelationInfoVOS) {
                baseMapper.deleteById(vo.getId());
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
