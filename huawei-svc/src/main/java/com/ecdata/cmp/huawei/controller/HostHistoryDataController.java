package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.VmHistoryDataResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VmHistoryDataVO;
import com.ecdata.cmp.huawei.service.HostHistoryDataService;
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

/**
 * @author ：xuj
 * @date ：Created in 2019/12/17 16:06
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/hosthistorydata")
@Api(tags = "主机历史性能")
public class HostHistoryDataController {

    @Autowired
    private HostHistoryDataService hostHistoryDataService;


    //各项指标查询编码
    private static final String CPU_INDICATOR_ID = "1407379178586113";//CPU
    private static final String MEMORY_INDICATOR_ID = "1407379178651656";//内存
    private static final String NET_IN_INDICATOR_ID = "1407379178717195";//网络流入
    private static final String NET_OUT_INDICATOR_ID = "1407379178717196";//网络流入
    private static final String DISK_WRITE_INDICATOR_ID = "1407379178782738";//硬盘写
    private static final String DISK_READ_INDICATOR_ID = "1407379178782739";//硬盘读

    @PutMapping("/get_hosthistory_cpu")
    @ApiOperation(value = "查询主机历史性能-CPU(运维面Token)", notes = "查询主机历史性能-CPU(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getHostHistoryCpu(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(CPU_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = hostHistoryDataService.getHostHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getHostHistoryCpu error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/get_hosthistory_memory")
    @ApiOperation(value = "查询主机历史性能-内存(运维面Token)", notes = "查询主机历史性能-内存(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getHostHistoryMemory(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(MEMORY_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = hostHistoryDataService.getHostHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getHostHistoryMemory error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/get_hosthistory_netIn")
    @ApiOperation(value = "查询主机历史性能-网络下载(运维面Token)", notes = "查询主机历史性能-网络下载(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getHostHistoryNetIn(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(NET_IN_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = hostHistoryDataService.getHostHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getHostHistoryNetIn error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/get_hosthistory_netOut")
    @ApiOperation(value = "查询主机历史性能-网络上传(运维面Token)", notes = "查询主机历史性能-网络上传(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getHostHistoryNetOut(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(NET_OUT_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = hostHistoryDataService.getHostHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getHostHistoryNetOut error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/get_hosthistory_diskwrite")
    @ApiOperation(value = "查询虚拟机历史性能-硬盘写(运维面Token)", notes = "查询虚拟机历史性能-硬盘写(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getHostHistoryDiskwrite(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(DISK_WRITE_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = hostHistoryDataService.getHostHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getHostHistoryDiskwrite error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    @PutMapping("/get_hosthistory_diskread")
    @ApiOperation(value = "查询主机历史性能-硬盘读(运维面Token)", notes = "查询主机历史性能-硬盘读(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getHostHistoryDiskread(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(DISK_READ_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = hostHistoryDataService.getHostHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getHostHistoryDiskread error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



}
