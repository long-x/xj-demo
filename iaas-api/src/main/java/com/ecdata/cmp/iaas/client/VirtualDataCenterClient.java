package com.ecdata.cmp.iaas.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.iaas.IaasConstant;
import com.ecdata.cmp.iaas.entity.dto.response.VirtualDataCenterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/27 15:38
 * @modified By：
 */

@FeignClient(value = IaasConstant.SERVICE_NAME, path = "/v1/virtualDataCenter")
public interface VirtualDataCenterClient {


    @GetMapping(path = "/get_vdc_by_id")
    VirtualDataCenterResponse getVdcById(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                         @RequestParam(name = "id") String id);

}
