package com.ecdata.cmp.iaas.client;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.IaasConstant;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforResponse;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import com.ecdata.cmp.iaas.entity.dto.response.sangfor.MinTimeObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = IaasConstant.SERVICE_NAME, path = "/v1/sangfor")
public interface IaasSangforClient {
    @PostMapping(path = "/add_batch")
    BaseResponse addBatch(@RequestBody List<SangforSecurityRiskVO> riskVOs);

    @GetMapping(path = "/min_unhandled")
    @ApiOperation(value = "最小未处理时间", notes = "最小未处理时间")
    MinTimeObject minUnhandled();

    @PostMapping(path ="/update")
    @ApiOperation(value = "风险更新", notes = "风险更新")
    BaseResponse update(@RequestBody SangforSecurityRiskVO riskVO);

    @GetMapping(path ="/find/{id}")
    @ApiOperation(value = "查询详情", notes = "查询详情")
    @ApiImplicitParam(name = "id", value = "风险id", required = true, paramType = "path")
    SangforResponse find(@PathVariable(name = "id") Long id);

}
