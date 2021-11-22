package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.VmHistoryDataResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/14 10:36
 * @modified By：
 */
@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/vmhistorydata")
public interface VmhistorydataClient {

    @PutMapping("/get_vmhistory_cpu")
    @ApiOperation(value = "查询虚拟机历史性能-CPU(运维面Token)", notes = "查询虚拟机历史性能-CPU(运维面Token)")
    VmHistoryDataResponse getVmHistoryCpu(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                          @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_vmhistory_memory")
    @ApiOperation(value = "查询虚拟机历史性能-内存(运维面Token)", notes = "查询虚拟机历史性能-内存(运维面Token)")
    VmHistoryDataResponse getVmHistoryMemory(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                          @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_vmhistory_netIn")
    @ApiOperation(value = "查询虚拟机历史性能-网络下载(运维面Token)", notes = "查询虚拟机历史性能-网络下载(运维面Token)")
    VmHistoryDataResponse getVmHistoryNetIn(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                           @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_vmhistory_netOut")
    @ApiOperation(value = "查询虚拟机历史性能-网络上传(运维面Token)", notes = "查询虚拟机历史性能-网络上传(运维面Token)")
    VmHistoryDataResponse getVmHistoryNetOut(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                            @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_vmhistory_diskwrite")
    @ApiOperation(value = "查询虚拟机历史性能-硬盘读(运维面Token)", notes = "查询虚拟机历史性能-硬盘读(运维面Token)")
    VmHistoryDataResponse getVmHistoryDiskwrite(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                             @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_vmhistory_diskread")
    @ApiOperation(value = "查询虚拟机历史性能-硬盘写(运维面Token)", notes = "查询虚拟机历史性能-硬盘写(运维面Token)")
    VmHistoryDataResponse getVmHistoryDiskread(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                                @RequestBody RequestVO requestVO)throws Exception;

}
