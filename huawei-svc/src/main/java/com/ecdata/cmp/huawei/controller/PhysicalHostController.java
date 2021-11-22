package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.PhysicalHostListResponse;
import com.ecdata.cmp.huawei.dto.vo.PhysicalHostVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.service.PhysicalHostService;
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
 * @date ：Created in 2019/12/11 15:33
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/physicalhost")
@Api(tags = "宿主机使用率")
public class PhysicalHostController {

    @Autowired
    private PhysicalHostService hostService;

    @PutMapping("/get_physical_host_list")
    @ApiOperation(value = "宿主机使用率(运营面Token)", notes = "宿主机使用率(运营面Token)")
    public ResponseEntity<PhysicalHostListResponse> getPhysicalhostList(@RequestBody RequestVO requestVO){
        //tokenId -->  yytoken(tangyu100)
        try {
            List<PhysicalHostVO> list = hostService.getPhysicalHostList(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new PhysicalHostListResponse(list));
        }catch (Exception e){
            log.info("getPhysicalhostList error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }




}
