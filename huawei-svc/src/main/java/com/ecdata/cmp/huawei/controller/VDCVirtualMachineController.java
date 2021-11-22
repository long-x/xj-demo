package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.VDCVirtualMachineListResponse;
import com.ecdata.cmp.huawei.dto.response.VDCVirtualMachineResponse;
import com.ecdata.cmp.huawei.dto.response.VmFlavorsResponse;
import com.ecdata.cmp.huawei.dto.vm.VmFlavors;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.huawei.service.VDCVirtualMachineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/10 11:40
 * @modified By：
 */

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/virtualMachine")
@Api(tags = "虚拟机信息")
public class VDCVirtualMachineController {


    @Autowired
    private VDCVirtualMachineService machineService;

    @PutMapping("/get_virtual_machine_list")
    @ApiOperation(value = "查询虚拟机列表(运维面Token)", notes = "查询虚拟机列表(运维面Token)")
    public ResponseEntity<VDCVirtualMachineListResponse> getVirtualMachineList(@RequestBody RequestVO requestVO){
        try {
            List<VirtualMachineVO> list = machineService.listVM(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VDCVirtualMachineListResponse(list));
        }catch (Exception e){
            log.info("getVirtualMachineList error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }


    @PutMapping("/get_virtual_machine_info")
    @ApiOperation(value = "查询虚拟机详情(运维面Token)", notes = "查询虚拟机详情(运维面Token)")
    public ResponseEntity<VDCVirtualMachineResponse> getVirtualMachineInfo(@RequestBody RequestVO requestVO){
        try {
            VirtualMachineVO  virtualMachineVO= machineService.VMInfo(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VDCVirtualMachineResponse(virtualMachineVO));
        }catch (Exception e){
            log.info("getVirtualMachineInfo error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PutMapping("/getVmFlavorsList")
    @ApiOperation(value = "查询云服务器规格详情列表", notes = "查询云服务器规格详情列表")
    public ResponseEntity<VmFlavorsResponse> getVmFlavorsList(@RequestBody RequestVO requestVO){
        try {
            List<VmFlavors> vmFlavors = machineService.VmFlavorsList(requestVO.getOcToken(), requestVO.getProjectId());
            return ResponseEntity.status(HttpStatus.OK).body(new VmFlavorsResponse(vmFlavors));
        }catch (Exception e){
            log.info("getVmFlavorsList error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }




}
