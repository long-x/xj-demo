package com.ecdata.cmp.user.client;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.user.UserConstant;
import com.ecdata.cmp.user.dto.SysBusinessPoolVO;
import com.ecdata.cmp.user.dto.response.SysBusinessPoolListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/22 14:34
 * @modified By：
 */
@FeignClient(value = UserConstant.SERVICE_NAME, path = "/v1/sys_business_pool")
public interface SysBusinessPoolClient {

    @PostMapping(path = "/add_batch")
    BaseResponse addBatch(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                          @RequestBody List<SysBusinessPoolVO> poolVOList);


    @PutMapping(path = "/remove")
    BaseResponse remove(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                        @RequestBody List<SysBusinessPoolVO> poolVOList);


    @PutMapping(path = "/update")
    BaseResponse update(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                        @RequestBody List<SysBusinessPoolVO> poolVOList);

    @GetMapping(path = "/list")
    SysBusinessPoolListResponse list(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                     @RequestParam(name = "type", required = false) Integer type,
                                     @RequestParam(name = "businessGroupId", required = false) Long businessGroupId,
                                     @RequestParam(name = "poolId", required = false) Long poolId);

}
