package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.request.BimOrgReq;
import com.ecdata.cmp.user.dto.response.BimOrgResp;

public interface IBimOrgService {
    BimOrgResp orgCreateService(BimOrgReq bimOrgReq);

    BimOrgResp orgUpdateService(BimOrgReq bimOrgReq);

    BimOrgResp orgDeleteService(BimOrgReq bimOrgReq);

    BimOrgResp queryAllOrgIdsService(BimOrgReq bimOrgReq);

    BimOrgResp queryOrgByIdService(BimOrgReq bimOrgReq);
}
