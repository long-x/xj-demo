package com.ecdata.cmp.iaas.controller;

import com.ecdata.cmp.common.api.MapListResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.iaas.service.HosthistoryPerformanceService;
import com.ecdata.cmp.iaas.service.VmhistoryPerformanceService;
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
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/17 16:46
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/hosthistoryperformance")
@Api(tags = "主机历史性能查询")
public class HosthistoryPerformanceController {

    @Autowired
    private HosthistoryPerformanceService hosthistoryPerformanceService;

    @PutMapping("/get_hosthistory")
    @ApiOperation(value = "查询主机历史性能", notes = "查询主机历史性能")
    public ResponseEntity<MapListResponse> getHostHistoryCpu(@RequestBody RequestVO requestVO) {
        try {
            List<Map<String, Object>> vmHistoryData = hosthistoryPerformanceService.getHostHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new MapListResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getHostHistoryCpu error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }




}
