package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.VmHistoryDataResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VmHistoryDataVO;
import com.ecdata.cmp.huawei.service.VmHistoryDataService;
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
 * @date ：Created in 2019/12/11 20:40
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/vmhistorydata")
@Api(tags = "虚拟机历史性能")
public class VmHistoryDataController {

    @Autowired
    private VmHistoryDataService vmHistoryDataService;

    //各项指标查询编码
    private static final String CPU_INDICATOR_ID = "562958543421441";//CPU
    private static final String MEMORY_INDICATOR_ID = "562958543486979";//内存
//    private static final String DISK_INDICATOR_ID = "562958543618052";//磁盘
    private static final String NET_IN_INDICATOR_ID = "562958543552537";//网络流入
    private static final String NET_OUT_INDICATOR_ID = "562958543552537";//网络流入
    private static final String DISK_WRITE_INDICATOR_ID = "562958543618061";//硬盘写
    private static final String DISK_READ_INDICATOR_ID = "562958543618062";//硬盘读
    //interval 按照时间查询
    private static final String MINUTE = "MINUTE";
    private static final String HOUR = "HOUR";
    private static final String DAY = "DAY";
    private static final String WEEK = "WEEK";
    private static final String MONTH = "MONTH";

    @PutMapping("/get_vmhistory_cpu")
    @ApiOperation(value = "查询虚拟机历史性能-CPU(运维面Token)", notes = "查询虚拟机历史性能-CPU(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getVmHistoryCpu(@RequestBody RequestVO requestVO){
        //tokenId -->  yytoken(tangyu100)
        //{
        //  "obj_type_id" : "562958543355904",
        // "indicator_ids":[
        //   CPU：562958543421441  内存：562958543486979  磁盘：562958543618052
        //        ],
        //  "obj_ids" : ["E9147D6D0B6532A4B9DC74C95D4A7966"],
        //  "interval" : "MINUTE",
        //  "range" : "BEGIN_END_TIME",
        //  "begin_time" : "1575518063000",
        //  "end_time" : "1575604463000"
        //  }
        try {
            requestVO.setIndicatorIds(CPU_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = vmHistoryDataService.getVmHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getVmHistoryCpu error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/get_vmhistory_memory")
    @ApiOperation(value = "查询虚拟机历史性能-内存(运维面Token)", notes = "查询虚拟机历史性能-内存(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getVmHistoryMemory(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(MEMORY_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = vmHistoryDataService.getVmHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getVmHistoryMemory error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


//    @PutMapping("/get_vmhistory_disk")
//    @ApiOperation(value = "查询虚拟机历史性能-硬盘(运维面Token)", notes = "查询虚拟机历史性能-硬盘(运维面Token)")
//    public ResponseEntity<VmHistoryDataResponse> getVmHistoryDisk(@RequestBody RequestVO requestVO){
//        try {
//            requestVO.setIndicatorIds(DISK_INDICATOR_ID);
//            VmHistoryDataVO vmHistoryData = vmHistoryDataService.getVmHistoryData(requestVO);
//            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
//        }catch (Exception e){
//            log.info("getVmHistoryDisk error");
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }

    @PutMapping("/get_vmhistory_netIn")
    @ApiOperation(value = "查询虚拟机历史性能-网络下载(运维面Token)", notes = "查询虚拟机历史性能-网络下载(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getVmHistoryNetIn(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(NET_IN_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = vmHistoryDataService.getVmHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getVmHistoryNetIn error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/get_vmhistory_netOut")
    @ApiOperation(value = "查询虚拟机历史性能-网络上传(运维面Token)", notes = "查询虚拟机历史性能-网络上传(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getVmHistoryNetOut(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(NET_OUT_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = vmHistoryDataService.getVmHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getVmHistoryNetOut error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/get_vmhistory_diskwrite")
    @ApiOperation(value = "查询虚拟机历史性能-硬盘写(运维面Token)", notes = "查询虚拟机历史性能-硬盘写(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getVmHistoryDiskwrite(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(DISK_WRITE_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = vmHistoryDataService.getVmHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getVmHistoryDiskwrite error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    @PutMapping("/get_vmhistory_diskread")
    @ApiOperation(value = "查询虚拟机历史性能-硬盘读(运维面Token)", notes = "查询虚拟机历史性能-硬盘读(运维面Token)")
    public ResponseEntity<VmHistoryDataResponse> getVmHistoryDiskread(@RequestBody RequestVO requestVO){
        try {
            requestVO.setIndicatorIds(DISK_READ_INDICATOR_ID);
            VmHistoryDataVO vmHistoryData = vmHistoryDataService.getVmHistoryData(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new VmHistoryDataResponse(vmHistoryData));
        }catch (Exception e){
            log.info("getVmHistoryDiskread error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



}
