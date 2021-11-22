package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.activiti.client.ActivitiClient;
import com.ecdata.cmp.activiti.dto.response.ActTaskListResponse;
import com.ecdata.cmp.activiti.dto.vo.ActTaskVO;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.huawei.client.CloudVmClient;
import com.ecdata.cmp.huawei.dto.vo.CloudVmVO;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyResource;
import com.ecdata.cmp.iaas.entity.dto.ApplyInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.entity.dto.apply.OAResultVO;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyResourceMapper;
import com.ecdata.cmp.iaas.service.IApplyService;
import com.ecdata.cmp.iaas.service.OaService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/4/14 9:29
 */
@Service
public class OaServiceImpl implements OaService {
    private static Logger logger = LoggerFactory.getLogger(OaServiceImpl.class);

    @Autowired
    private IaasApplyResourceMapper applyResourceMapper;

    @Autowired
    private IApplyService applyService;

    @Autowired
    private ActivitiClient activitiClient;

    @Override
    public BaseResponse updateOaApprovalResult(OAResultVO oaResultVO) {
        BaseResponse baseResponse = new BaseResponse();

        QueryWrapper<IaasApplyResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasApplyResource::getGroupProcessInstanceId, oaResultVO.getWorkflowId());
        IaasApplyResource applyResource = applyResourceMapper.selectOne(queryWrapper);

        if (applyResource == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("流程实例不存在！");
            return baseResponse;
        }

        IaasApplyResourceVO applyResourceVO = new IaasApplyResourceVO();
        applyResourceVO.setId(applyResource.getId());

        ApplyInfoVO vo = new ApplyInfoVO();
        vo.setSubmitOa("1");
        vo.setComment(oaResultVO.getApprovalOpinion());
        vo.setApplyResourceVO(applyResourceVO);

        if ("1".equals(oaResultVO.getApprovalResult())) {
            vo.setOutcome("同意");
        } else {
            vo.setOutcome("驳回");
        }

        //先查询到taskId,然后再调用完成接口
        ActTaskListResponse actTaskPageResponse = activitiClient.queryRunTask(applyResource.getProcessInstanceId(), false);

        if (actTaskPageResponse == null
                || actTaskPageResponse.getCode() != 0
                || actTaskPageResponse.getData() == null
                || CollectionUtils.isEmpty(actTaskPageResponse.getData())) {
            baseResponse.setCode(202);
            baseResponse.setMessage("查询TaskId异常！");
            return baseResponse;
        }

        ActTaskVO actTaskVO = actTaskPageResponse.getData().get(0);
        vo.setTaskId(actTaskVO.getId());
        vo.setUserDisplayName("集团审核主管");
        vo.setUserId(81744358524907528L);
        baseResponse = applyService.apply(vo);
        baseResponse.setMessage("成功");
        return baseResponse;
    }
}
