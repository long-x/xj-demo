package com.ecdata.cmp.user.client;

import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.user.UserConstant;
import com.ecdata.cmp.user.dto.response.SysBusinessGroupListResponse;
import com.ecdata.cmp.user.dto.response.SysBusinessGroupResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 10:53
 * @modified By：
 */
@FeignClient(value = UserConstant.SERVICE_NAME, path = "/v1/sys_business_group")
public interface SysBusinessGroupClient {

    @GetMapping(path = "/get_list_by_pool_id/{poolId}")
    SysBusinessGroupListResponse getlistByPoolId(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                                 @PathVariable(name = "poolId") Long poolId);

    @GetMapping(path = "/get_dis_business_group_name")
    SysBusinessGroupListResponse getDisBusinessGroupName(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                                         @RequestParam(name = "businessGroupName", required = false) String businessGroupName);

    @GetMapping(path = "/get_pool_ids_by_user_id/{userId}")
    LongListResponse getPoolIdsByUserId(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                        @PathVariable(name = "userId") Long userId,
                                        @RequestParam(name = "type", required = false) Integer type);

    @GetMapping(path = "/get_business_group_by_user")
    @ApiOperation(value = "根据用户id查询业务组", notes = "根据用户id查询业务组")
    SysBusinessGroupListResponse getBusinessGroupByUser(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                                        @RequestParam(name = "userId", required = false) String userId);

    @GetMapping(path = "/qr_group_by_userid/{userId}")
    SysBusinessGroupListResponse qrGroupByUserId(@PathVariable(name = "userId") String userId);

    @GetMapping(path = "/getSysBusinessGroupBySnp/{id}")
    @ApiOperation(value = "根据业务组id查询相关信息", notes = "根据业务组id查询相关信息")
    ResponseEntity<SysBusinessGroupResponse> getSysBusinessGroupBySnp(@PathVariable(name = "id") Long id);
}
