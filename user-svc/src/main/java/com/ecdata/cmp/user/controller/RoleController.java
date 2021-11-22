package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.RoleVO;
import com.ecdata.cmp.user.dto.UserVO;
import com.ecdata.cmp.user.dto.response.*;
import com.ecdata.cmp.user.entity.Role;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-03-19
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/role")
@Api(tags = "角色相关接口")
public class RoleController {

    /**
     * 角色Service
     */
    @Autowired
    private IRoleService roleService;


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询角色", notes = "根据id查询角色")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path")
    public ResponseEntity<RoleResponse> getById(@PathVariable(name = "id") Long id) {
        RoleVO roleVO = new RoleVO();
        Role role = roleService.getById(id);
        BeanUtils.copyProperties(role, roleVO);
        return ResponseEntity.status(HttpStatus.OK).body(new RoleResponse(roleVO));

    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查看角色", notes = "分页查看角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<RolePageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                 @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                 @RequestParam(required = false) String keyword) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(Role::getRoleName, keyword).or().like(Role::getRoleAlias, keyword);
        }
        Page<Role> page = new Page<>(pageNo, pageSize);
        IPage<Role> result = roleService.page(page, queryWrapper);
        List<Role> roleList = result.getRecords();
        List<RoleVO> roleVOList = roleService.transform(roleList);
        return ResponseEntity.status(HttpStatus.OK).body(new RolePageResponse(new PageVO<>(result, roleVOList)));

    }

    @GetMapping("/list")
    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    public ResponseEntity<RoleListResponse> list() {
        List<Role> roleList = roleService.list();
        List<RoleVO> roleVOList = roleService.transform(roleList);
        return ResponseEntity.status(HttpStatus.OK).body(new RoleListResponse(roleVOList));

    }

    @GetMapping("/getITDirectors")
    @ApiOperation(value = "IT主管审批人", notes = "IT主管审批人")
    @ApiImplicitParam(name = "roleId", value = "角色id", required = true, paramType = "Long")
    public ResponseEntity<UserListResponse> getITDirectors(Long roleId) {
        List<User> userList = roleService.getITDirectors(roleId);
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVOList.add(userVO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new UserListResponse(userVOList));

    }

    @PostMapping("/add")
    @ApiOperation(value = "新增角色", notes = "新增角色")
    public ResponseEntity<BaseResponse> add(@RequestBody RoleVO roleVO) {
        BaseResponse baseResponse = new BaseResponse();
        if ("sys_admin".equals(roleVO.getRoleName())) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加角色失败, 不能添加sys_admin角色");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleVO, role);
        role.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow());
        if (roleService.save(role)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加角色成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加角色失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/update")
    @ApiOperation(value = "修改角色", notes = "修改角色")
    public ResponseEntity<BaseResponse> update(@RequestBody RoleVO roleVO) {
        BaseResponse baseResponse = new BaseResponse();
        if (roleVO.getId() == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        Role role = this.roleService.getById(roleVO.getId());
        String roleName = roleVO.getRoleName();
        String oldRoleName = role.getRoleName();
        if ("sys_admin".equals(oldRoleName) && !"sys_admin".equals(roleName)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("不能修改sys_admin角色名");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        if (!"sys_admin".equals(oldRoleName) && "sys_admin".equals(roleName)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("角色名修改为sys_admin");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        if ("tenant_admin".equals(oldRoleName) && !"tenant_admin".equals(roleName)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("不能修改tenant_admin角色名");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        if (!"tenant_admin".equals(oldRoleName) && "tenant_admin".equals(roleName)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("角色名修改为tenant_admin");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        BeanUtils.copyProperties(roleVO, role);
        role.setUpdateUser(Sign.getUserId());
        role.setUpdateTime(DateUtil.getNow());
        if (roleService.updateById(role)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("修改角色成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("修改角色失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除角色")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("删除角色 role id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (roleService.removeById(id)) {
            roleService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除角色成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除角色失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/removeBatch")
    @ApiOperation(value = "批量删除", notes = "批量删除角色")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("删除角色 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            this.roleService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.roleService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除角色成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    @GetMapping("/list_admin")
    @ApiOperation(value = "获取管理员角色列表", notes = "获取管理员角色列表")
    public ResponseEntity<RoleListResponse> listAdmin() {
        List<RoleVO> roleVOList = roleService.listAdmin();
        return ResponseEntity.status(HttpStatus.OK).body(new RoleListResponse(roleVOList));

    }
}
