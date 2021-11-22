package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.huawei.dto.response.SecurityGroupsListResponse;
import com.ecdata.cmp.huawei.dto.response.VpcListResponse;
import com.ecdata.cmp.huawei.dto.token.TokenVdcVO;
import com.ecdata.cmp.huawei.dto.vo.SecurityGroupsVO;
import com.ecdata.cmp.huawei.dto.vo.VpcVO;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.huawei.service.SecurityGroupsService;
import com.ecdata.cmp.iaas.client.VirtualDataCenterClient;
import com.ecdata.cmp.iaas.entity.dto.response.VirtualDataCenterResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/4 18:51
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/securityGroups")
@Api(tags = "安全组api")
public class SecurityGroupsController {

    @Autowired
    private SecurityGroupsService securityGroupsService;

    @Autowired
    private ManageOneService manageOneService;


    @GetMapping("/get_security_groups_list")
    @ApiOperation(value = "查询安全组", notes = "查询安全组")
    @ApiImplicitParam(name = "tokenId", value = "token", paramType = "query", dataType = "String")
    public ResponseEntity<SecurityGroupsListResponse> getSecurityGroups(@RequestParam(name="tokenId",required = true) String tokenId) {
        try {
            Map map = new HashMap();
            map.put("X-Auth-Token",tokenId);
            List<SecurityGroupsVO> groupsList = securityGroupsService.getSecurityGroupsList(map);
            return ResponseEntity.status(HttpStatus.OK).body(new SecurityGroupsListResponse(groupsList));
        }catch (Exception e){
            log.info("getSecurityGroups error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }




    @GetMapping("/get_security_groups_list_by_token")
    @ApiOperation(value = "前端调用查询安全组", notes = "前端调用查询安全组")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "string")
    public ResponseEntity<SecurityGroupsListResponse> getSecurityGroupsByToken(@RequestParam(name = "id",required = false) String id) {
        try {

            List<SecurityGroupsVO> groupsList = securityGroupsService.getSecurityGroupsList(id);
            return ResponseEntity.status(HttpStatus.OK).body(new SecurityGroupsListResponse(groupsList));
        }catch (Exception e){
            log.info("getSecurityGroupsByToken error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }




}
