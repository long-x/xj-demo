package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.huawei.ManageOneConstant;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/1 17:16
 * @modified By：
 */
@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/on_off_machine_baremetal")
public interface OnOffMachineBaremetalClient {

    @GetMapping(path = "/on_off_machine")
    @ApiOperation(value = "开机/关机虚拟机", notes = "开机/关机虚拟机")
    ResponseEntity<BaseResponse> onOffMachine(
            @RequestParam(name = "projectId", required = false) String projectId,
            @RequestParam(name = "vmKey", required = false) String vmKey,
            @RequestParam(name = "type", required = false) String type);


    @GetMapping(path = "/onOffBaremetal")
    @ApiOperation(value = "开机/关机裸金属", notes = "开机/关机裸金属")
    ResponseEntity<BaseResponse> onOffBaremetal(
            @RequestParam(name = "projectId", required = false) String projectId,
            @RequestParam(name = "vmKey", required = false) String vmKey,
            @RequestParam(name = "type", required = false) String type);

}
