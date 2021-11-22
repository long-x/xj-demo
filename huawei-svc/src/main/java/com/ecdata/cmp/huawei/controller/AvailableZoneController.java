package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.availablezone.*;
import com.ecdata.cmp.huawei.dto.response.*;
import com.ecdata.cmp.huawei.dto.vo.AvailbleZoneReqVO;
import com.ecdata.cmp.huawei.dto.vo.BmsInfoVO;
import com.ecdata.cmp.huawei.service.AvailableZoneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/AvailableArea")
@Api(tags = "可用分区API")
public class AvailableZoneController {

    @Autowired
    private AvailableZoneService availableZoneService;

    @PostMapping("/get_availablezone_detail")
    @ApiOperation(value = "根据可用区ID查询可用区当前容量", notes = "根据可用区ID查询可用区当前容量")
    public ResponseEntity<AvailableZoneStatResponse> getAvailableAreaDetail(@RequestBody AvailbleZoneReqVO availbleZoneReqVO){
        try{
            AvailableZoneStatistics availableZoneDetail = availableZoneService.getAvailableZoneDetail(availbleZoneReqVO.getOmToken(), availbleZoneReqVO.getAzoneId());
            return ResponseEntity.status(HttpStatus.OK).body(new AvailableZoneStatResponse(availableZoneDetail));
        }catch (Exception e){
            log.info("get availablezone detail error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/get_availablezone_resource")
    @ApiOperation(value = "根据可用区ID查询可用区资源", notes = "根据可用区ID查询可用区资源")
    public ResponseEntity<AvailableZoneResourceResponse> getAvailableZoneResource(@RequestBody AvailbleZoneReqVO availbleZoneReqVO){
        try{
            AvailableZoneResource availableZoneResource = availableZoneService.getAvailableZoneResource(availbleZoneReqVO.getOmToken(), availbleZoneReqVO.getAzoneId());
            return ResponseEntity.status(HttpStatus.OK).body(new AvailableZoneResourceResponse(availableZoneResource));
        }catch (Exception e){
            log.info("get availablezone resource error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/get_availablezone_by_projectId")
    @ApiOperation(value = "根据项目id查询所有可用区(去除裸金属)", notes = "根据项目id查询所有可用区(去除裸金属)")
    public ResponseEntity<AvailableZoneResponse> getAvailableZoneByProjectId(@RequestBody AvailbleZoneReqVO availbleZoneReqVO){
        try{
            List<AvailableZone> availableZoneList = availableZoneService.getAvailableZoneByProjectId(availbleZoneReqVO.getOcToken(), availbleZoneReqVO.getProjectId());
            return ResponseEntity.status(HttpStatus.OK).body(new AvailableZoneResponse(availableZoneList));
        }catch (Exception e){
            log.info("get availablezone by projectId error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping("/get_all_az")
    @ApiOperation(value = "获取全部分区", notes = "获取全部分区")
    @ApiImplicitParam(name = "type", value = "type", paramType = "query", dataType = "string")
    public ResponseEntity<SysAzoneListResponse> getAllAz(@RequestParam(required = false) String type) {
        SysAzoneListResponse listResponse = new SysAzoneListResponse();
        try {
            List<SysAzone> azAllList = availableZoneService.getAzAllList(type);
            listResponse.setData(azAllList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(listResponse);
    }


    @GetMapping("/get_volume_by_azid")
    @ApiOperation(value = "根据azid获取磁盘名称", notes = "根据azid获取磁盘名称")
    @ApiImplicitParam(name = "azId", value = "azId", paramType = "query", dataType = "string")
    public ResponseEntity<DimensionListResponse> getVolume(@RequestParam(required = false) String azId) {
        DimensionListResponse dimensionListResponse = new DimensionListResponse();
        try {
            List<DimensionInfo> diskName = availableZoneService.getDiskName(azId);
            dimensionListResponse.setData(diskName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dimensionListResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dimensionListResponse);
    }


    @GetMapping("/get_bms_list")
    @ApiOperation(value = "根据vdckey获取裸金属列表", notes = "根据vdckey获取裸金属列表")
    @ApiImplicitParam(name = "vdcKey", value = "vdcKey", paramType = "query", dataType = "string")
    public ResponseEntity<BmsListResponse> getBmsByVdc(@RequestParam(required = false) String vdcKey) {
        BmsListResponse bmsListResponse = new BmsListResponse();
        try {
            List<BmsInfoVO> bmsList = availableZoneService.getBmsList(vdcKey);
            bmsListResponse.setData(bmsList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bmsListResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bmsListResponse);
    }


    @GetMapping("/get_bms_list_by_prjectid")
    @ApiOperation(value = "根据项目id获取裸金属列表", notes = "根据项目id获取裸金属列表")
    @ApiImplicitParam(name = "projectId", value = "projectId", paramType = "query", dataType = "string")
    public ResponseEntity<BmsListResponse> getBmsByProjectId(@RequestParam(required = false) String projectId) {
        BmsListResponse bmsListResponse = new BmsListResponse();
        try {
            List<BmsInfoVO> bmsList = availableZoneService.getBmsListByProjectId(projectId);
            bmsListResponse.setData(bmsList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bmsListResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bmsListResponse);
    }

}
