package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.HostVolumesVOListResponse;
import com.ecdata.cmp.huawei.dto.vo.HostVolumesVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.service.HostVolumesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 11:23
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/hostvolumes")
@Api(tags = "虚拟机磁盘信息")
public class HostVolumesController {

    @Autowired
    private HostVolumesService hostVolumesService;


    @PutMapping("/get_host_volumes")
    @ApiOperation(value = "虚拟机磁盘信息", notes = "虚拟机磁盘信息")
    public ResponseEntity<HostVolumesVOListResponse> getHostVolumesList(@RequestBody RequestVO requestVO){
        //tokenId -->  yytoken(tangyu100)
        try {
            List<HostVolumesVO> list = hostVolumesService.getHostVolumesList(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new HostVolumesVOListResponse(list));
        }catch (Exception e){
            log.info("getHostVolumesList error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }



}
