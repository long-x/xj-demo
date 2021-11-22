package com.ecdata.cmp.iaas.controller;

import com.ecdata.cmp.iaas.entity.dto.IaasAreaVO;
import com.ecdata.cmp.iaas.service.IaasAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/12/6 13:03,
 */

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/area")
@Api(tags = "区域相关的API")
public class IaasAreaController {
    @Autowired
    IaasAreaService iaasAreaService;

    @GetMapping("/list")
    @ApiOperation(value = "区域查询", notes = "区域查询")
    public ResponseEntity<List<IaasAreaVO>> page() {
        List<IaasAreaVO> iaasAreaVOS = iaasAreaService.queryIaasAreas();
        return ResponseEntity.status(HttpStatus.OK).body(iaasAreaVOS);

    }

}
