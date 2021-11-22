package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyNetworkPolicy;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyNetworkPolicyVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyNetworkPolicyMapper;
import com.ecdata.cmp.iaas.service.IaasApplyNetworkPolicyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuxiaojian
 * @date 2020/3/11 10:45
 */
@Service
public class IaasApplyNetworkPolicyServiceImpl implements IaasApplyNetworkPolicyService {
    @Autowired
    private IaasApplyNetworkPolicyMapper applyNetworkPolicyMapper;

    @Override
    public AuditResponse saveIaasApplyNetworkPolicy(IaasApplyNetworkPolicyVO vo) {
        IaasApplyNetworkPolicy queryNetworkPolicy = applyNetworkPolicyMapper.selectById(vo.getId());
        AuditResponse auditResponse = new AuditResponse();

        if (queryNetworkPolicy == null) {
            IaasApplyNetworkPolicy applyStorage = new IaasApplyNetworkPolicy();
            BeanUtils.copyProperties(vo, applyStorage);
            long id = SnowFlakeIdGenerator.getInstance().nextId();

            auditResponse.setId(id);
            applyStorage.setId(id);
            applyStorage.setCreateTime(DateUtil.getNow());
            applyStorage.setCreateUser(Sign.getUserId());
            applyStorage.setIsDeleted(0);
            applyNetworkPolicyMapper.insert(applyStorage);
        } else {
            auditResponse.setId(queryNetworkPolicy.getId());
            BeanUtils.copyProperties(vo, queryNetworkPolicy);
            applyNetworkPolicyMapper.updateById(queryNetworkPolicy);
        }

        return auditResponse;
    }

    @Override
    public BaseResponse deleteIaasApplyNetworkPolicy(Long id) {
        applyNetworkPolicyMapper.deleteById(id);
        return BaseResponse.builder().build();
    }
}
