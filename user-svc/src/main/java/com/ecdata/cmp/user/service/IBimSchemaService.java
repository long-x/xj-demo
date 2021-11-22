package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.request.BimSchemaReq;
import com.ecdata.cmp.user.dto.response.BimSchemaResp;

public interface IBimSchemaService {
    BimSchemaResp schemaService(BimSchemaReq bimSchemaReq);
}
