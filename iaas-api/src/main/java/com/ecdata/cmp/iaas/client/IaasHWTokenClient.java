package com.ecdata.cmp.iaas.client;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.iaas.IaasConstant;
import com.ecdata.cmp.iaas.entity.dto.AvailbleZoneReqVO;
import com.ecdata.cmp.iaas.entity.dto.RequestVO;
import com.ecdata.cmp.iaas.entity.dto.TokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = IaasConstant.SERVICE_NAME, path = "/v1/iaas_hw_token")
public interface IaasHWTokenClient {
    @GetMapping(path = "/get_requestVOFlavor")
    RequestVO getRequestVOFlavor(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                 @RequestParam(name = "projectKey") String projectKey,
                                 @RequestParam(name = "vdcId") String vdcId);

    @GetMapping(path = "/get_requestVO")
    RequestVO getRequestVO(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                           @RequestParam(name = "vdcId") String vdcId);

    @GetMapping(path = "/get_TokenDTO")
    TokenDTO listTokenDTO(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token);


    @GetMapping(path = "/get_AvailbleZoneId")
    AvailbleZoneReqVO getAvailbleZoneId(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                                  @RequestParam(name = "proId") String proId);

    @GetMapping(path = "/get_AvailbleZoneReqVO")
    AvailbleZoneReqVO listAvailbleZoneReqVO(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                            @RequestParam(name = "azId",required = false) String azId);


}
