package com.ecdata.cmp.iaas.client;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.common.dto.ProcessVO;
import com.ecdata.cmp.iaas.IaasConstant;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.iaas.entity.dto.response.alert.IaasAlertListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = IaasConstant.SERVICE_NAME, path = "/v1/alert")
public interface IaasAlertClient {
    @PostMapping(path = "/add_batch")
    BaseResponse addBatch(@RequestBody List<IaasAlertVO> iavos);

    @PutMapping(path = "/update_batch")
    LongListResponse updateBatch(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                 @RequestParam(name = "huaweiIds") List<Long> ids);

    @PutMapping(path = "/update_process")
    LongListResponse updateProcess(@RequestBody ProcessVO processVO);

    @GetMapping(path = "/fetch_unhandled_alerts")
    IaasAlertListResponse fetchUnhandled();
}
