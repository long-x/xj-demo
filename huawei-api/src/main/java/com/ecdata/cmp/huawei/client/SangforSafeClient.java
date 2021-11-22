package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/sangfor/safe")
public interface SangforSafeClient {
    @PostMapping("/risk/deal")
    @ApiOperation(value = "风险处置", notes = "风险处置")
    boolean getRiskBusiness(@RequestBody SangforSecurityRiskVO riskVO);
}
