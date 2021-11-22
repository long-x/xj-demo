package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.apply.OAResultVO;

/**
 * @author xuxiaojian
 * @date 2020/4/14 9:28
 */
public interface OaService {
    BaseResponse updateOaApprovalResult(OAResultVO oaResultVO);
}
