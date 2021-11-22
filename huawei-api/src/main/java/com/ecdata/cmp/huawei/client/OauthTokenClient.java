package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.api.StringResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/ManageOne")
public interface OauthTokenClient {

    @GetMapping(path = "/manageOne1")
    StringResponse getTokenInfoByUser(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                      @RequestParam(value = "ipAdress", required = false) String ipAdress,
                                      @RequestParam(value = "userName") String userName,
                                      @RequestParam(value = "password") String password) throws IOException;


    @GetMapping(path = "/getToken")
    TokenInfoResponse getTokenByUser(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                     @RequestParam(value = "projectId", required = false) String projectId,
                                     @RequestParam(value = "domainName") String domainName,
                                     @RequestParam(value = "userName") String userName,
                                     @RequestParam(value = "password") String password) throws IOException;

    @GetMapping(path = "/getTokenByVdcUser")
    StringResponse getTokenByVdcUser(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                                     @RequestParam(value = "projectId", required = false) String projectId,
                                                     @RequestParam(value = "domainName") String domainName,
                                                     @RequestParam(value = "userName") String userName,
                                                     @RequestParam(value = "password") String password) throws IOException;


    @GetMapping(path = "/CheckVdcUser")
    StringResponse CheckVdcUser(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                 @RequestParam(value = "domainName") String domainName,
                                 @RequestParam(value = "userName") String userName,
                                 @RequestParam(value = "password") String password) throws IOException;


    @PutMapping(path = "/getTokenNew")
    TokenInfoResponse getTokenAll(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                  @RequestBody  IaasProviderVO iaasProviderVO) throws IOException;


}
