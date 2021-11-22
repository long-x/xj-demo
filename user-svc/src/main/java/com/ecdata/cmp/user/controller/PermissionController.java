package com.ecdata.cmp.user.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.JSONArrayResponse;
import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.user.dto.PermissionVO;
import com.ecdata.cmp.user.dto.response.PermissionListResponse;
import com.ecdata.cmp.user.dto.response.PermissionTreeAndIdsResponse;
import com.ecdata.cmp.user.entity.Permission;
import com.ecdata.cmp.user.entity.RolePermission;
import com.ecdata.cmp.user.service.IPermissionService;
import com.ecdata.cmp.user.service.IRolePermissionService;
import com.ecdata.cmp.user.utils.PermissionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/permission")
@Api(tags = "权限相关的API")
public class PermissionController {
    /**
     * 权限Service
     */
    @Autowired
    private IPermissionService permissionService;


    /**
     * 角色权限Service
     */
    @Autowired
    private IRolePermissionService rolePermissionService;

    @GetMapping("/tree")
    @ApiOperation(value = "查询树形结构菜单权限", notes = "查询树形结构菜单权限")
    public ResponseEntity<PermissionListResponse> getTree() {
        List<PermissionVO> tree = this.permissionService.getTree();
        return ResponseEntity.status(HttpStatus.OK).body(new PermissionListResponse(tree));
    }

    @GetMapping("/treeAndIds")
    @ApiOperation(value = "查询树形结构菜单权限和所有id集合", notes = "查询树形结构菜单权限和所有id集合")
    public ResponseEntity<PermissionTreeAndIdsResponse> getTreeAndIds() {
        Map<String, List> map = this.permissionService.getTreeAndIds();
        return ResponseEntity.status(HttpStatus.OK).body(new PermissionTreeAndIdsResponse(map));

    }

    @GetMapping("/getRouterByUser")
    @ApiOperation(value = "查询用户拥有的菜单权限和按钮权限", notes = "查询用户拥有的菜单权限和按钮权限")
    @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", dataType = "long")
    public ResponseEntity<JSONArrayResponse> getRouterByUser(@RequestParam(required = false) Long userId,HttpServletRequest request){
        if (userId == null) {
            userId = Sign.getUserId();
        }
        log.info("前端传入code{}",request.getParameter("code"));
        List<Permission> metaList = permissionService.queryByUser(userId);
        JSONArray jsonArray = PermissionUtil.generateRouterMap(metaList);
        return ResponseEntity.status(HttpStatus.OK).body(new JSONArrayResponse(jsonArray));

    }


    @PostMapping("/add")
    @ApiOperation(value = "添加菜单权限", notes = "添加菜单权限")
    public ResponseEntity<BaseResponse> add(@RequestBody PermissionVO permissionVO) {
        BaseResponse baseResponse = new BaseResponse();
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionVO, permission);
        permission = PermissionUtil.intelligentPermission(permission);

        String path = permission.getPath();
        if (StringUtils.isEmpty(path)) {
            baseResponse.setResultEnum(ResultEnum.MISS_PERMISSION_PATH);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Permission::getPath, path);
        int count = this.permissionService.count(queryWrapper);
        if (count > 0) {
            baseResponse.setResultEnum(ResultEnum.EXIST_PERMISSION_PATH);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

        if (permissionService.addPermission(permission)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加菜单权限成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加菜单权限失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改菜单权限", notes = "修改菜单权限")
    public ResponseEntity<BaseResponse> update(@RequestBody PermissionVO permissionVO) {
        BaseResponse baseResponse = new BaseResponse();

        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionVO, permission);
        permission.setName(null); // name统一由path转换
        permission = PermissionUtil.intelligentPermission(permission);
        Long permissionId = permission.getId();
        if (permissionId == null) {
            baseResponse.setResultEnum(ResultEnum.PARAM_MISS);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        String path = permissionVO.getPath();
        if (StringUtils.isEmpty(path)) {
            baseResponse.setResultEnum(ResultEnum.MISS_PERMISSION_PATH);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Permission::getPath, path).ne(Permission::getId, permissionId);
        int count = this.permissionService.count(queryWrapper);
        if (count > 0) {
            baseResponse.setResultEnum(ResultEnum.EXIST_PERMISSION_PATH);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

        permission.setUpdateUser(Sign.getUserId());
        permission.setUpdateTime(DateUtil.getNow());
        if (permissionService.updatePermission(permission)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新菜单权限成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新菜单权限失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除菜单权限")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("删除菜单权限 permission id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (permissionService.removePermission(id)) {
            permissionService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除菜单权限成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除菜单权限失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/removeBatch")
    @ApiOperation(value = "批量删除", notes = "批量删除菜单权限")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("删除菜单权限 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            for (String idStr : idArray) {
                Long id = Long.parseLong(idStr);
                this.permissionService.removePermission(id);
                this.permissionService.modifyUpdateRecord(id, Sign.getUserId());
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    @GetMapping("/queryRolePermission")
    @ApiOperation(value = "查询角色授权", notes = "查询角色授权")
    @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "query", dataType = "long")
    public ResponseEntity<LongListResponse> queryRolePermission(@RequestParam Long roleId) {
        List<RolePermission> list = rolePermissionService.list(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId, roleId));
        List<Long> ids = new ArrayList<>();
        for (RolePermission rp : list) {
            ids.add(rp.getPermissionId());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new LongListResponse(ids));
    }

    @PostMapping("/saveRolePermission")
    @ApiOperation(value = "保存角色授权", notes = "保存角色授权")
    public ResponseEntity<BaseResponse> saveRolePermission(@RequestBody JSONObject json) {
        Long roleId = json.getLong("roleId");
        String permissionIds = json.getString("permissionIds");
        this.rolePermissionService.saveRolePermission(roleId, permissionIds);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("保存角色授权成功");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

}
