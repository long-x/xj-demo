package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.BaremetalListResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xuxiaojian
 * @date 2020/4/24 14:00
 */
@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/specifications")
public interface BaremetalServersFlavorsClient {
    @GetMapping(path = "/get_baremetal_list")
    @ApiOperation(value = "获取裸金属列表", notes = "获取裸金属列表")
    BaremetalListResponse getBaremetalList(@RequestParam(name = "projectId", required = false) String projectId);

}
