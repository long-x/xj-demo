package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyOtherVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;

/**
 * @author ty
 * @date 2020/3/10 15:21
 */
public interface IaasApplyOtherService {
    AuditResponse saveIaasApplyOther(IaasApplyOtherVO iaasApplyOtherVO) throws Exception;

    BaseResponse deleteIaasApplyOther(Long id) throws Exception;
}
