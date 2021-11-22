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
 * @date ：Created in 2019/12/17 16:36
 * @modified By：
 */
@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/hosthistorydata")
public interface HosthistorydataClient {

    @PutMapping("/get_hosthistory_cpu")
    @ApiOperation(value = "查询主机历史性能-CPU(运维面Token)", notes = "查询主机历史性能-CPU(运维面Token)")
    VmHistoryDataResponse getHostHistoryCpu(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                          @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_hosthistory_memory")
    @ApiOperation(value = "查询主机历史性能-内存(运维面Token)", notes = "查询主机历史性能-内存(运维面Token)")
    VmHistoryDataResponse getHostHistoryMemory(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                             @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_hosthistory_netIn")
    @ApiOperation(value = "查询主机历史性能-网络下载(运维面Token)", notes = "查询主机历史性能-网络下载(运维面Token)")
    VmHistoryDataResponse getHostHistoryNetIn(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                            @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_hosthistory_netOut")
    @ApiOperation(value = "查询主机历史性能-网络上传(运维面Token)", notes = "查询主机历史性能-网络上传(运维面Token)")
    VmHistoryDataResponse getHostHistoryNetOut(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                             @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_hosthistory_diskwrite")
    @ApiOperation(value = "查询主机历史性能-硬盘读(运维面Token)", notes = "查询主机历史性能-硬盘读(运维面Token)")
    VmHistoryDataResponse getHostHistoryDiskwrite(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                                @RequestBody RequestVO requestVO)throws Exception;

    @PutMapping("/get_hosthistory_diskread")
    @ApiOperation(value = "查询主机历史性能-硬盘写(运维面Token)", notes = "查询主机历史性能-硬盘写(运维面Token)")
    VmHistoryDataResponse getHostHistoryDiskread(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                               @RequestBody RequestVO requestVO)throws Exception;

}
