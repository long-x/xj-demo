package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.IaasAreaSum;
import com.ecdata.cmp.iaas.entity.dto.IaasAreaVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.entity.dto.response.apply.IaasApplyResourcePageResponse;
import com.ecdata.cmp.iaas.service.IaasAreaService;
import com.ecdata.cmp.iaas.service.IaasAreaSumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hhj
 * @since 2019/5/26
 */

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/area")
@Api(tags = "计算资源相关的API")
public class IaasAreaSumController {
//    @Autowired
//    IaasAreaSumService iaasAreaSumService;
//
//    @GetMapping("/page")
//    @ApiOperation(value = "资源总数查询", notes = "资源总数查询")
//    public ResponseEntity<List<IaasAreaSum>> getByServerNameAndAreaName(
//                                                              @RequestParam(required = false) String serverName,
//                                                              @RequestParam(required = false) String areaName) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("serverName", serverName);
//        params.put("areaName", areaName);
//        List<IaasAreaSum> iaasAreaSumList = iaasAreaSumService.getByServerNameAndAreaName(serverName, serverName);
//        return ResponseEntity.status(HttpStatus.OK).body(iaasAreaSumList);
//
//    }


}
