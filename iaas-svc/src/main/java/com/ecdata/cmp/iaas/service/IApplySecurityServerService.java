package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyCalculateVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplySecurityServerVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;

/**
 * @author ty
 * @date 2020/3/11 10:44
 */
public interface IApplySecurityServerService {
    AuditResponse saveSecurityServer(IaasApplySecurityServerVO iaasApplySecurityServerVO) throws Exception;

    BaseResponse deleteSecurityServer(Long id) throws Exception;
}
