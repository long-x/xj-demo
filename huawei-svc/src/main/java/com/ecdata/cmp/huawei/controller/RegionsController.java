package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.availablezone.AvailableZone;
import com.ecdata.cmp.huawei.dto.region.ActualCapacity;
import com.ecdata.cmp.huawei.dto.region.RegionEntity;
import com.ecdata.cmp.huawei.dto.region.RegionRequestDTO;
import com.ecdata.cmp.huawei.dto.region.ResourcePool;
import com.ecdata.cmp.huawei.dto.response.ActualCapacityResponse;
import com.ecdata.cmp.huawei.dto.response.RegionEntityResponse;
import com.ecdata.cmp.huawei.dto.response.RegionsListResponse;
import com.ecdata.cmp.huawei.dto.token.TokenDTO;
import com.ecdata.cmp.huawei.dto.vo.RegionsVO;
import com.ecdata.cmp.huawei.service.AvailableZoneService;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.huawei.service.RegionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/9 15:01
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/regions")
@Api(tags = "区域API")
public class RegionsController {

    @Autowired
    private RegionsService regionsService;

    @Autowired
    private ManageOneService manageOneService;

    @Autowired
    private AvailableZoneService availableZoneService;


    @PostMapping("/get_regions_list")
    @ApiOperation(value = "查询区域列表", notes = "查询区域列表")
    public ResponseEntity<RegionsListResponse> getRegionsList(@RequestBody TokenDTO tokenInfos){
        Map map = new HashMap();
        map.put("X-Auth-Token",tokenInfos.getOcToken());
        try {
            List<RegionsVO> voList = regionsService.gitRegionsList(map);
            return ResponseEntity.status(HttpStatus.OK).body(new RegionsListResponse(voList));
        }catch (Exception e){
            log.info("get RegionsList error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/get_capacity")
    @ApiOperation(value = "根据区域ID查询区域当前容量", notes = "根据区域ID查询区域当前容量")
    public ResponseEntity<ActualCapacityResponse> getRegoinCapacity(@RequestBody RegionRequestDTO regionRequestDTO){
        try{
            Map<String, ActualCapacity> regoinAllCapacity = manageOneService.getRegoinAllCapacity(regionRequestDTO.getOmToken(), regionRequestDTO.getRegionId());
            return ResponseEntity.status(HttpStatus.OK).body(new ActualCapacityResponse(regoinAllCapacity));
        }catch (Exception e){
            log.info("get region capacity error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/get_region_entity")
    @ApiOperation(value = "查询区域下可用分区", notes = "查询区域下可用分区")
    public ResponseEntity<RegionEntityResponse> getRegoinEntity(@RequestBody TokenDTO tokenInfos){
        try{
            RegionEntity regionEntity = RegionEntity.builder().
                    id("sa-fb-1").
                    name("城投私有云").build();
            ResourcePool resourcePool=ResourcePool.builder().
                    id("FUSION_CLOUD_sa-fb-1").
                    name("OpenStack_sa-fb-1").build();
            List<AvailableZone> availableZoneList = availableZoneService.getAvailableZoneByInfraId(tokenInfos.getOcToken(), resourcePool.getId());
            resourcePool.setAvailableZoneList(availableZoneList);
            regionEntity.setResourcePool(resourcePool);
            return ResponseEntity.status(HttpStatus.OK).body(new RegionEntityResponse(regionEntity));
        }catch (Exception e){
            log.info("get region entity error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
