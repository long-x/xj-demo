package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.request.BimUserReq;
import com.ecdata.cmp.user.dto.response.BimUserResp;

public interface IBimUserService {
    BimUserResp userCreateService(BimUserReq bimUserReq);

    BimUserResp userUpdateService(BimUserReq bimUserReq);

    BimUserResp userDeleteService(BimUserReq bimUserReq);

    BimUserResp queryAllUserIdsService(BimUserReq bimUserReq);

    BimUserResp queryUserByIdService(BimUserReq bimUserReq);
}
