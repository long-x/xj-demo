package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.AvailableZoneResourceResponse;
import com.ecdata.cmp.huawei.dto.response.AvailableZoneResponse;
import com.ecdata.cmp.huawei.dto.response.BmsListResponse;
import com.ecdata.cmp.huawei.dto.vo.AvailbleZoneReqVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/AvailableArea")
public interface AvailableZoneClient {

    @PostMapping(path = "/get_availablezone_resource")
    @ApiOperation(value = "根据可用区ID查询可用区资源", notes = "根据可用区ID查询可用区资源")
    AvailableZoneResourceResponse getAvailableZoneResource(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                                           @RequestBody AvailbleZoneReqVO availbleZoneReqVO) throws IOException;

    @PostMapping(path = "/get_availablezone_by_projectId")
    @ApiOperation(value = "根据项目id查询所有可用区(去除裸金属)", notes = "根据项目id查询所有可用区(去除裸金属)")
    AvailableZoneResponse getAvailableZoneByProjectId(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                                      @RequestBody AvailbleZoneReqVO availbleZoneReqVO) throws IOException;

    @GetMapping(path = "/get_bms_list_by_prjectid")
    @ApiOperation(value = "根据项目id获取裸金属列表", notes = "根据项目id获取裸金属列表")
    BmsListResponse getBmsByProjectId(@RequestParam(value = "projectId", required = false) String projectId);

}
