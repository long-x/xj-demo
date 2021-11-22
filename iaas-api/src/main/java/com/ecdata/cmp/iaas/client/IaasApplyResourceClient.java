package com.ecdata.cmp.iaas.client;

import com.ecdata.cmp.iaas.IaasConstant;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = IaasConstant.SERVICE_NAME, path = "/v1/apply")
public interface IaasApplyResourceClient {
    @GetMapping(path ="/query_apply_resource_processInstanceId/{processInstanceId}")
    @ApiOperation(value = "自动发放资源", notes = "自动发放资源")
    @ApiImplicitParam(name = "processInstanceId", value = "processInstanceId", paramType = "query", dataType = "String")
    String queryApplyResourceByProcessInstanceId(@RequestParam(name = "processInstanceId") String processInstanceId);

}
