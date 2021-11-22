package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyNetworkPolicyVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;

/**
 * @author xuxiaojian
 * @date 2020/3/11 10:40
 */
public interface IaasApplyNetworkPolicyService {

    AuditResponse saveIaasApplyNetworkPolicy(IaasApplyNetworkPolicyVO vo);

    BaseResponse deleteIaasApplyNetworkPolicy(Long id);
}
