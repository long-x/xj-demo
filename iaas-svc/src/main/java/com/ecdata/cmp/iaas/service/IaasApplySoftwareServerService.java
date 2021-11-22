package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplySoftwareServerVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;

/**
 * @author xuxiaojian
 * @date 2020/3/11 10:40
 */
public interface IaasApplySoftwareServerService {

    AuditResponse saveIaasApplySoftwareServer(IaasApplySoftwareServerVO vo);

    BaseResponse deleteIaasApplySoftwareServer(Long id);
}
