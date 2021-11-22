package com.ecdata.cmp.user.controller;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.request.BimOrgReq;
import com.ecdata.cmp.user.dto.request.BimSchemaReq;
import com.ecdata.cmp.user.dto.request.BimUserReq;
import com.ecdata.cmp.user.dto.response.BimOrgResp;
import com.ecdata.cmp.user.dto.response.BimSchemaResp;
import com.ecdata.cmp.user.dto.response.BimUserResp;
import com.ecdata.cmp.user.mapper.UserMapper;
import com.ecdata.cmp.user.service.IBimOrgService;
import com.ecdata.cmp.user.service.IBimSchemaService;
import com.ecdata.cmp.user.service.IBimUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @title: biamIdentityManage
 * @Author: shig
 * @description: 竹云身份管理
 * @Date: 2020/3/5 5:27 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/bimIdentityManage")
@Api(tags = "🏎竹云身份管理 相关的API")
public class BimIdentityManageController {
    @Autowired
    private IBimSchemaService bimSchemaService;

    @Autowired
    private IBimUserService bimUserService;

    @Autowired
    private IBimOrgService bimOrgService;

    @PostMapping("/SchemaService")
    @ApiOperation(value = "对象属性字段查询", notes = "对象属性字段查询")
    public ResponseEntity<BimSchemaResp> schemaService(@RequestBody BimSchemaReq bimSchemaReq) throws Exception {
        BimSchemaResp bimSchemaResp = bimSchemaService.schemaService(bimSchemaReq);
        if (bimSchemaResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimSchemaResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimSchemaResp);
    }

    @PostMapping("/UserCreateService")
    @ApiOperation(value = "账号创建", notes = "账号创建")
    public ResponseEntity<BimUserResp> UserCreateService(@RequestBody BimUserReq bimUserReq) throws Exception {
        BimUserResp bimUserResp = bimUserService.userCreateService(bimUserReq);
        if (bimUserResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
    }

    @PostMapping("/UserUpdateService")
    @ApiOperation(value = "账户修改", notes = "账户修改")
    public ResponseEntity<BimUserResp> userUpdateService(@RequestBody BimUserReq bimUserReq) throws Exception {
        BimUserResp bimUserResp = bimUserService.userUpdateService(bimUserReq);
        if (bimUserResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
    }

    @PostMapping("/UserDeleteService")
    @ApiOperation(value = "账户删除", notes = "账户删除")
    public ResponseEntity<BimUserResp> userDeleteService(@RequestBody BimUserReq bimUserReq) throws Exception {
        BimUserResp bimUserResp = bimUserService.userDeleteService(bimUserReq);
        if (bimUserResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
    }

    @PostMapping("/QueryAllUserIdsService")
    @ApiOperation(value = "批量查询账号 ID", notes = "批量查询账号 ID")
    public ResponseEntity<BimUserResp> queryAllUserIdsService(@RequestBody BimUserReq bimUserReq) throws Exception {
        BimUserResp bimUserResp = bimUserService.queryAllUserIdsService(bimUserReq);
        if (bimUserResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
    }

    @PostMapping("/QueryUserByIdService")
    @ApiOperation(value = "根据账号 ID 查询账号详细内容", notes = "根据账号 ID 查询账号详细内容")
    public ResponseEntity<BimUserResp> queryUserByIdService(@RequestBody BimUserReq bimUserReq) throws Exception {
        BimUserResp bimUserResp = bimUserService.queryUserByIdService(bimUserReq);
        if (bimUserResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimUserResp);
    }

    @PostMapping("/OrgCreateService")
    @ApiOperation(value = "组织机构创建", notes = "组织机构创建")
    public ResponseEntity<BimOrgResp> orgCreateService(@RequestBody BimOrgReq bimOrgReq) throws Exception {
        BimOrgResp bimOrgResp = bimOrgService.orgCreateService(bimOrgReq);
        if (bimOrgResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
    }

    @PostMapping("/OrgUpdateService")
    @ApiOperation(value = "组织机构修改", notes = "组织机构修改")
    public ResponseEntity<BimOrgResp> orgUpdateService(@RequestBody BimOrgReq bimOrgReq) throws Exception {
        BimOrgResp bimOrgResp = bimOrgService.orgUpdateService(bimOrgReq);
        if (bimOrgResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
    }

    @PostMapping("/OrgDeleteService")
    @ApiOperation(value = "组织机构删除", notes = "组织机构删除")
    public ResponseEntity<BimOrgResp> orgDeleteService(@RequestBody BimOrgReq bimOrgReq) throws Exception {
        BimOrgResp bimOrgResp = bimOrgService.orgDeleteService(bimOrgReq);
        if (bimOrgResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
    }

    @PostMapping("/QueryAllOrgIdsService")
    @ApiOperation(value = "批量查询组织机构 ID", notes = "批量查询组织机构 ID")
    public ResponseEntity<BimOrgResp> queryAllOrgIdsService(@RequestBody BimOrgReq bimOrgReq) throws Exception {
        BimOrgResp bimOrgResp = bimOrgService.queryAllOrgIdsService(bimOrgReq);
        if (bimOrgResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
    }

    @PostMapping("/QueryOrgByIdService")
    @ApiOperation(value = "根据组织机构 ID 查询组织机构详细内容", notes = "根据组织机构 ID 查询组织机构详细内容")
    public ResponseEntity<BimOrgResp> queryOrgByIdService(@RequestBody BimOrgReq bimOrgReq) throws Exception {
        BimOrgResp bimOrgResp = bimOrgService.queryOrgByIdService(bimOrgReq);
        if (bimOrgResp == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bimOrgResp);
    }

}