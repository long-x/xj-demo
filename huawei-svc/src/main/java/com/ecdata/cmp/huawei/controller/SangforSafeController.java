package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.service.SangforSafeService;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sangfor/safe")
@Api(tags = "深信服安全api")
public class SangforSafeController {

    @Autowired
    private SangforSafeService sangforSafeService;

    @PostMapping("/risk/deal")
    @ApiOperation(value = "风险处置", notes = "风险处置")
    public boolean getRiskBusiness(@RequestBody SangforSecurityRiskVO riskVO){
        log.info("getRiskBusiness, riskVO={}", riskVO);
        return sangforSafeService.dealRisk(riskVO);
    }


}
