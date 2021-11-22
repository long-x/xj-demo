package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.BaremetalListResponse;
import com.ecdata.cmp.huawei.dto.response.BaremetalServersFlavorsListResponse;
import com.ecdata.cmp.huawei.dto.response.CloudServersFlavorsListResponce;
import com.ecdata.cmp.huawei.dto.response.VpcListResponse;
//import com.ecdata.cmp.huawei.dto.vo.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import com.ecdata.cmp.huawei.dto.vo.BaremetalServersFlavorsVO;
import com.ecdata.cmp.huawei.dto.vo.CloudServersFlavorsVO;
import com.ecdata.cmp.huawei.dto.vo.VpcVO;
import com.ecdata.cmp.huawei.service.BaremetalServersFlavorsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/1 15:04
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/specifications")
@Api(tags = "裸金属/虚拟机规格相关 API")
public class BaremetalServersFlavorsController {


    @Autowired
    private BaremetalServersFlavorsService flavorsService;



    @GetMapping("/get_baremetalservers")
    @ApiOperation(value = "根据项目id查询裸金属规格列表", notes = "根据项目id查询裸金属规格列表")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "string")
    public ResponseEntity<BaremetalServersFlavorsListResponse> getBaremetalFlavorsService(@RequestParam(name = "id",required = false) String id) {
        try {
            List<BaremetalServersFlavorsVO> vpcList = flavorsService.getBaremetalServersFlavors(id);
            return ResponseEntity.status(HttpStatus.OK).body(new BaremetalServersFlavorsListResponse(vpcList));
        }catch (Exception e){
            log.info("getBaremetalFlavorsService error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }




    @GetMapping("/get_cloudServersFlavors")
    @ApiOperation(value = "根据项目id查询私有云规格列表", notes = "根据项目id查询私有云规格列表")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "string")
    public ResponseEntity<CloudServersFlavorsListResponce> getCloudServersFlavors(@RequestParam(name = "id",required = false) String id) {
        try {
            List<CloudServersFlavorsVO> vpcList = flavorsService.getCloudServersFlavors(id);
            return ResponseEntity.status(HttpStatus.OK).body(new CloudServersFlavorsListResponce(vpcList));
        }catch (Exception e){
            log.info("getCloudServersFlavors error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }


    @GetMapping("/get_baremetal_list")
    @ApiOperation(value = "获取裸金属列表", notes = "获取裸金属列表")
    @ApiImplicitParam(name = "projectId", value = "projectId", paramType = "query", dataType = "string")
    public ResponseEntity<BaremetalListResponse> getBaremetalList(@RequestParam(name = "projectId",required = false) String projectId) {
        try {
            List<BareMetalVO> vpcList = flavorsService.getBareMetalAll(projectId);
            return ResponseEntity.status(HttpStatus.OK).body(new BaremetalListResponse(vpcList));
        }catch (Exception e){
            log.info("getBaremetalList error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }





}
