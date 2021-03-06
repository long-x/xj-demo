package com.ecdata.cmp.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.BooleanResponse;
import com.ecdata.cmp.common.api.LongResponse;
import com.ecdata.cmp.common.crypto.Hash;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.dto.RightDTO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.RoleVO;
import com.ecdata.cmp.user.dto.UserDepartmentVO;
import com.ecdata.cmp.user.dto.UserRoleVO;
import com.ecdata.cmp.user.dto.UserVO;
import com.ecdata.cmp.user.dto.request.SysNotificationRequest;
import com.ecdata.cmp.user.dto.response.RightResponse;
import com.ecdata.cmp.user.dto.response.SuperiorsResponse;
import com.ecdata.cmp.user.dto.response.UserDepartmentListResponse;
import com.ecdata.cmp.user.dto.response.UserListResponse;
import com.ecdata.cmp.user.dto.response.UserPageResponse;
import com.ecdata.cmp.user.dto.response.UserResponse;
import com.ecdata.cmp.user.dto.response.UserRoleListResponse;
import com.ecdata.cmp.user.entity.Department;
import com.ecdata.cmp.user.entity.Project;
import com.ecdata.cmp.user.entity.Role;
import com.ecdata.cmp.user.entity.SysBusinessGroupMember;
import com.ecdata.cmp.user.entity.Tenant;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.entity.UserDepartment;
import com.ecdata.cmp.user.entity.UserProject;
import com.ecdata.cmp.user.entity.UserRole;
import com.ecdata.cmp.user.service.IDepartmentService;
import com.ecdata.cmp.user.service.IPermissionService;
import com.ecdata.cmp.user.service.IProjectService;
import com.ecdata.cmp.user.service.IRoleService;
import com.ecdata.cmp.user.service.ISysBusinessGroupMemberService;
import com.ecdata.cmp.user.service.ITenantService;
import com.ecdata.cmp.user.service.IUserDepartmentService;
import com.ecdata.cmp.user.service.IUserProjectService;
import com.ecdata.cmp.user.service.IUserRoleService;
import com.ecdata.cmp.user.service.IUserService;
import com.ecdata.cmp.user.service.SysNotificationService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author xuxinsheng
 * @since 2019-03-19
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/user")
@Api(tags = "??????????????????")
public class UserController {

    private static Pattern simplePwdPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,16}$");

    private static final List ALLOW_IMAGE_TYPE = Arrays.asList("image/jpeg", "image/jpg", "image/png");
//public static final String IMG_PATH = "E:\\develop\\nginx-1.15.3\\html\\images\\";  nginx?????????


    public static final String IMG_PATH = "http://10.10.10.92:22/opt/ecdata-cmp/pic";
//public static final String IMG_URL = "http://127.0.0.1/images/";  nginx?????????

//    public static final String IMG_URL = "http://10.10.10.92:8680/";

    @Value("${role.name.tenant_admin}")
    private String tenantAdminName;


    /**
     * ??????Service
     */
    @Autowired
    private IUserService userService;
    /**
     * ??????Service
     */
    @Autowired
    private ITenantService tenantService;
    /**
     * ??????Service
     */
    @Autowired
    private SysNotificationService sysNotifiService;
    /**
     * ??????Service
     */
    @Autowired
    private IDepartmentService departmentService;
    /**
     * ????????????Service
     */
    @Autowired
    private IUserDepartmentService userDepartmentService;
    /**
     * ??????Service
     */
    @Autowired
    private IRoleService roleService;
    /**
     * ??????Service
     */
    @Autowired
    private IProjectService projectService;
    /**
     * ????????????Service
     */
    @Autowired
    private IUserRoleService userRoleService;
    /**
     * ????????????Service
     */
    @Autowired
    private IUserProjectService userProjectService;
    /**
     * ??????Service
     */
    @Autowired
    private IPermissionService permissionService;
    /**
     * ???????????????????????????
     */
    @Autowired
    private ISysBusinessGroupMemberService sysBusinessGroupMemberService;

    @GetMapping("/roles/{id}")
    @ApiOperation(value = "??????id????????????", notes = "??????id????????????")
    @ApiImplicitParam(name = "id", value = "??????id", required = true, paramType = "path")
    public ResponseEntity<List<String>> getRolesByUserId(@PathVariable(name = "id") Long id) {
        List<RoleVO> roleList = roleService.qryRoleByUserId(id);
        List<String> collect = roleList.stream().map(RoleVO::getRoleAlias).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "??????id????????????", notes = "??????id????????????")
    @ApiImplicitParam(name = "id", value = "??????id", required = true, paramType = "path")
    public ResponseEntity<UserResponse> getById(@PathVariable(name = "id") Long id) {
        UserResponse userResponse = new UserResponse();
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setDepartmentList(departmentService.qryDepByUserId(id));
        userVO.setRoleList(roleService.qryRoleByUserId(id));
        userResponse.setData(userVO);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/query/{id}")
    @ApiOperation(value = "??????id????????????", notes = "??????id????????????")
    @ApiImplicitParam(name = "id", value = "??????id", required = true, paramType = "path")
    public ResponseEntity<UserResponse> queryById(@PathVariable(name = "id") Long id) {
        UserResponse userResponse = new UserResponse();
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setDepartmentList(departmentService.qryDepByUserId(id));
        userVO.setRoleList(roleService.qryRoleByUserId(id));
        userResponse.setData(userVO);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/page")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "?????????", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "?????????", paramType = "query", dataType = "string")
    })
    public ResponseEntity<UserPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                 @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                 @RequestParam(required = false) String keyword) {
        Page<UserVO> page = new Page<>(pageNo, pageSize);
        IPage<UserVO> result = userService.qryUserInfo(page, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(new UserPageResponse(new PageVO<>(result)));

    }

    @GetMapping("/list")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<UserListResponse> list() {
        List<UserVO> userVOList = new ArrayList<>();
        List<User> userList = userService.list();
        if (userList != null && userList.size() > 0) {
            for (User user : userList) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                userVOList.add(userVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new UserListResponse(userVOList));
    }

    @GetMapping("/info")
    @ApiOperation(value = "??????????????????", notes = "????????????????????????????????????")
    @ApiImplicitParam(name = "userId", value = "??????id", paramType = "query", dataType = "long")
    public ResponseEntity<UserResponse> getInfo(@RequestParam(required = false) Long userId) {
        UserResponse userResponse = new UserResponse();
        UserVO userVO = new UserVO();
        if (userId == null) {
            userId = Sign.getUserId();
        }
        User user = userService.getById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }
        BeanUtils.copyProperties(user, userVO);
        userVO.setPassword("");
        if (StringUtils.isEmpty(userVO.getAvatar())) {
            // ????????????
            userVO.setAvatar("/avatar2.jpg");
        }
        userVO.setDepartmentList(departmentService.qryDepByUserId(userId));
        userVO.setRoleList(roleService.qryRoleByUserId(userId));
        userVO.setProjectList(projectService.getProjectByUserId(userId));
        userVO.setPermissionList(permissionService.queryButtonByUser(userId));
        userResponse.setData(userVO);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);

    }

    @PostMapping("/add")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> add(@RequestBody UserVO userVO) throws Exception {
        String name = userVO.getName();
        String password = userVO.getPassword();
        String confirm = userVO.getConfirm();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password) || StringUtils.isEmpty(confirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_NAME_PASSWORD));
        }
        if (!password.equals(confirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.CONFIRM_PASSWORD));
        }
        // todo ??????????????????
        if (!simplePwdPattern.matcher(password).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.PWD_WRONG_FORMAT));
        }


        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        if ("sysadmin".equals(name) || userService.isExistUserName(user, false)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.EXIST_USER));
        }
        user.setPassword(Hash.encode(password));
        user.setCreateUser(Sign.getUserId());
        user.setCreateTime(DateUtil.getNow());
        // ??????id???????????????
        user.setId(SnowFlakeIdGenerator.getInstance().nextId());

        BaseResponse baseResponse = new BaseResponse();
        if (userService.save(user)) {
            userDepartmentService.insertBatch(user.getId(), userVO.getDepartmentList());
            userRoleService.insertBatch(user.getId(), userVO.getRoleList());
            userProjectService.insertBatch(user.getId(), userVO.getProjectList());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> update(@RequestBody UserVO userVO) throws Exception {
        User user = new User();
        BeanUtils.copyProperties(userVO, user);

        if (user.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
        }
        if (user.getDisplayName() == null || user.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.PARAM_MISS));
        }
        if (userService.isExistUserName(user, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.EXIST_USER));
        }

//        String password = user.getPassword();
        String confirm = userVO.getConfirm();
//        if ((StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(confirm)) && !password.equals(confirm)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.CONFIRM_PASSWORD));
//        }
        // todo ??????????????????
//        if (!simplePwdPattern.matcher(password).matches()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.PWD_WRONG_FORMAT));
//        }
//        if (StringUtils.isNotEmpty(password)) {
//            user.setPassword(Hash.encode(password));
//        }
        user.setUpdateUser(Sign.getUserId());
        user.setUpdateTime(DateUtil.getNow());

        BaseResponse baseResponse = new BaseResponse();
        if (userService.updateById(user)) {
            userDepartmentService.updateUserDep(user.getId(), userVO.getDepartmentList());
            userRoleService.updateUserRole(user.getId(), userVO.getRoleList());
            userProjectService.updateUserProject(user.getId(), userVO.getProjectList());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/changPassword")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<BaseResponse> changPassword(@RequestBody UserVO userVO) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        String name = userVO.getName();
        String password = userVO.getPassword();
        String confirm = userVO.getConfirm();

        log.info("????????????????????? name:{}", name);

        if (StringUtils.isEmpty(password) && StringUtils.isNotEmpty(confirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_NAME_PASSWORD));
        }

        if (!password.equals(confirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.CONFIRM_PASSWORD));
        }
        // todo ??????????????????
        if (!simplePwdPattern.matcher(password).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.PWD_WRONG_FORMAT));
        }

        User user = this.userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, userVO.getId()));

        if (name == null || user == null || !name.equals(user.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.USER_NOT_FOUND));
        }

        user.setPassword(Hash.encode(password));

        user.setUpdateUser(Sign.getUserId());
        user.setUpdateTime(DateUtil.getNow());
        if (userService.updateById(user)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/remove")
    @ApiOperation(value = "??????", notes = "????????????(????????????)")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("???????????? user id???{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (userService.removeById(id)) {
            userService.modifyUpdateRecord(id, Sign.getUserId());

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/removeBatch")
    @ApiOperation(value = "????????????", notes = "??????????????????(????????????)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("???????????? ids???{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("???????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            this.userService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.userService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
            }

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    @PutMapping("/frozenBatch")
    @ApiOperation(value = "??????/????????????", notes = "??????/????????????")
    public ResponseEntity<BaseResponse> frozenBatch(@RequestBody JSONObject jsonObject) {
        log.info("frozenBatch:{}", jsonObject);
        String ids = jsonObject.getString("ids");
        String status = jsonObject.getString("status");
        String[] arr = ids.split(",");
        for (String id : arr) {
            if (StringUtils.isNotEmpty(id)) {
                this.userService.update(
                        new User().setStatus(Integer.parseInt(status)).setUpdateUser(Sign.getUserId()).setUpdateTime(DateUtil.getNow()),
                        new UpdateWrapper<User>().lambda().eq(User::getId, id)
                );
            }
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("????????????");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping("/userDepartmentList")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @ApiImplicitParam(name = "userId", value = "??????id", paramType = "query", dataType = "long")
    public ResponseEntity<UserDepartmentListResponse> userDepartmentList(@RequestParam(required = false) Long userId) {
        QueryWrapper<UserDepartment> queryWrapper = new QueryWrapper<>();
        if (null != userId) {
            queryWrapper.lambda().eq(UserDepartment::getUserId, userId);
        }
        List<UserDepartment> userDepartmentList = userDepartmentService.list(queryWrapper);
        List<UserDepartmentVO> userDepartmentVOList = new ArrayList<>();
        if (userDepartmentList != null && userDepartmentList.size() > 0) {
            for (UserDepartment userDepartment : userDepartmentList) {
                UserDepartmentVO userDepartmentVO = new UserDepartmentVO();
                BeanUtils.copyProperties(userDepartment, userDepartmentVO);
                userDepartmentVOList.add(userDepartmentVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new UserDepartmentListResponse(userDepartmentVOList));

    }

    @PostMapping("/addUserDepartment")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<BaseResponse> addUserDepartment(@ApiParam(value = "??????id", required = true) @RequestParam Long userId,
                                                          @ApiParam(value = "??????Id??????", required = true) @RequestParam String depIds) {
        log.info("?????????????????? user id: {}, depIds: {}", userId, depIds);
        List<Map<String, Long>> list = new ArrayList<>();

        String[] arr = depIds.split(",");
        for (int i = 0; i < arr.length; ++i) {
            final int size = 2;
            Map<String, Long> map = Maps.newHashMapWithExpectedSize(size);
            map.put("id", SnowFlakeIdGenerator.getInstance().nextId());
            map.put("userId", userId);
            map.put("departmentId", Long.valueOf(arr[i]));
            list.add(map);
        }
        userService.insertBatchUserDep(list);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("????????????????????????");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

    }

    @DeleteMapping("/deleteBatchUserDep")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<BaseResponse> deleteBatchUserDep(@ApiParam(value = "????????????Id??????", required = true) @RequestParam String ids) {
        log.info("?????????????????? ids: {}", ids);
        String[] arr = ids.split(",");
        Long[] ints = new Long[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            ints[i] = Long.valueOf(arr[i]);
        }
        userService.deleteBatchUserDep(ints);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("????????????????????????");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

    }

    @GetMapping("/userRoleList")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    @ApiImplicitParam(name = "userId", value = "??????id", paramType = "query", dataType = "long")
    public ResponseEntity<UserRoleListResponse> userRoleList(@RequestParam(required = false) Long userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        if (null != userId) {
            queryWrapper.lambda().eq(UserRole::getUserId, userId);
        }
        List<UserRole> userRoleList = userRoleService.list(queryWrapper);
        List<UserRoleVO> userRoleVOList = new ArrayList<>();
        if (userRoleList != null && userRoleList.size() > 0) {
            for (UserRole userRole : userRoleList) {
                UserRoleVO userRoleVO = new UserRoleVO();
                BeanUtils.copyProperties(userRole, userRoleVO);
                userRoleVOList.add(userRoleVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new UserRoleListResponse(userRoleVOList));

    }

    @PostMapping("/addUserRole")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<BaseResponse> addUserRole(@ApiParam(value = "??????id", required = true) @RequestParam Long userId,
                                                    @ApiParam(value = "??????Id??????", required = true) @RequestParam String roleIds) {
        log.info("?????????????????? user id: {}, depIds: {}", userId, roleIds);
        List<Map<String, Long>> list = new ArrayList<>();

        String[] arr = roleIds.split(",");
        for (int i = 0; i < arr.length; ++i) {
            final int size = 2;
            Map<String, Long> map = Maps.newHashMapWithExpectedSize(size);
            map.put("id", SnowFlakeIdGenerator.getInstance().nextId());
            map.put("userId", userId);
            map.put("roleId", Long.valueOf(arr[i]));
            list.add(map);
        }
        userService.insertBatchUserRole(list);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("????????????????????????");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

    }

    @DeleteMapping("/deleteUserRole")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<BaseResponse> deleteUserRole(@ApiParam(value = "????????????Id??????", required = true) @RequestParam String ids) {
        log.info("?????????????????? ids: {}", ids);
        String[] arr = ids.split(",");
        Long[] ints = new Long[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            ints[i] = Long.valueOf(arr[i]);
        }
        userService.deleteBatchUserRole(ints);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("????????????????????????");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

    }

    @PutMapping("/resetPassword")
    @ApiOperation(value = "???????????????", notes = "???????????????")
    public ResponseEntity<BaseResponse> resetPassword(@RequestParam Long id) throws Exception {
        log.info("??????????????? user id???{}", id);
        BaseResponse baseResponse = new BaseResponse();
        User user = new User();
        user.setId(id)
                .setPassword(Hash.encode("88888888"))
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
        if (userService.updateById(user)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("???????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("???????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/forget_password")
    @ApiOperation(value = "??????????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> forgetPassword(@RequestBody UserVO userVO) {
        BaseResponse baseResponse = new BaseResponse();
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        try {
            Sign.setCurrentTenantId(userVO.getTenantId());


            Tenant tenant = tenantService.getById(userVO.getTenantId());
            if (tenant == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(), "??????????????????"));
            }

            if (StringUtils.isNotBlank(userVO.getName())) {
                if (!userService.isExistUserName(user, false)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(), "???????????????"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(), "?????????????????????"));
            }

            List<Long> ids = roleService.qryUserIdByRole(Arrays.asList(tenantAdminName));

            SysNotificationRequest sysNotifi = new SysNotificationRequest();
            sysNotifi.setMessage(String.format("??????[%s]??????????????????", userVO.getName()));
            sysNotifi.setDetail(String.format("??????[%s]??????[%s]???????????????????????????????????????", tenant.getTenantName(), userVO.getName()));
            sysNotifi.setUserIds(ids);
            sysNotifi.setType(2); //????????????
            BaseResponse baseResponse1 = sysNotifiService.addNotificationToUser(sysNotifi);


            if (baseResponse1.getCode() != ResultEnum.DEFAULT_SUCCESS.getCode()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(), "?????????????????????"));
            }

        } catch (Exception e) {
            log.debug("forgetPassword??????" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL));
        } finally {
            Sign.removeCurrentTenantId();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(), "????????????"));
    }


    @GetMapping("/superiors")
    @ApiOperation(value = "??????????????????????????????", notes = "??????????????????????????????")
    public ResponseEntity<SuperiorsResponse> getSuperiors() {
        Map<String, List<Long>> map = new HashMap<>();

        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.lambda().eq(Role::getRoleName, "admin");
        List<Role> roleList = roleService.list(roleQueryWrapper);
        List<Long> roleIdList = new ArrayList<>();
        if (roleList != null && roleList.size() > 0) {
            for (Role role : roleList) {
                roleIdList.add(role.getId());
            }
        }
        map.put("role", roleIdList);

        Long userId = Sign.getUserId();
        List<Department> departmentList = this.departmentService.qryParentDeploymentByUserId(userId);
        List<Long> departmentIdList = new ArrayList<>();
        if (departmentList != null && departmentList.size() > 0) {
            for (Department department : departmentList) {
                departmentIdList.add(department.getId());
            }
        }
        map.put("department", departmentIdList);

        List<Project> projectList = this.projectService.qryParentProjectByUserId(userId);
        List<Long> projectIdList = new ArrayList<>();
        if (projectList != null && projectList.size() > 0) {
            for (Project project : projectList) {
                projectIdList.add(project.getId());
            }
        }
        map.put("project", projectIdList);


        return ResponseEntity.status(HttpStatus.OK).body(new SuperiorsResponse(map));

    }

    @GetMapping("/getSnowFlakeIdGeneratorId")
    @ApiOperation(value = "????????????id", notes = "????????????id")
    public ResponseEntity<LongResponse> getSnowFlakeIdGeneratorId() {
        return ResponseEntity.status(HttpStatus.OK).body(new LongResponse(SnowFlakeIdGenerator.getInstance().nextId()));
    }


    @PutMapping("/update_user")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<BaseResponse> updateUser(@RequestBody UserVO userVO) throws Exception {


        User user = new User();
        BeanUtils.copyProperties(userVO, user);

        if (user.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
        }
        if (user.getDisplayName() == null || user.getName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.PARAM_MISS));
        }
        //???????????????
//        if (userService.isExistUserName(user, true)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.EXIST_USER));
//        }
//phone e-mail  originPwd  password confirm   ????????????????????????

        String password = userVO.getPassword(); //????????????????????????
        String confirm = userVO.getConfirm(); //????????????
        if ((StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(confirm)) && !password.equals(confirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.CONFIRM_PASSWORD));
        }
        // todo ??????????????????
        if (!simplePwdPattern.matcher(password).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.PWD_WRONG_FORMAT));
        }

//        String pattern="^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";
//        boolean isMatch = Pattern.matches(pattern, password);
//        if(!isMatch)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.PWD_WRONG_FORMAT));


        //????????????????????????
        User oldUser = userService.getById(user.getId());
        String pwd = oldUser.getPassword();
        //?????????????????????
        String originPwd = userVO.getOriginPwd();
        //???????????????????????????????????????
        if ((StringUtils.isNotEmpty(pwd) && StringUtils.isNotEmpty(originPwd)) && !pwd.equals(Hash.encode(originPwd))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.BAD_CREDENTIALS));
        }
//        //??????????????????
//        if(StringUtils.isNotEmpty(password))
//            if(pwd.equals(Hash.encode(password))){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.SAME_PASSWORD));
//        }

        user.setDisplayName(userVO.getDisplayName());
        user.setName(userVO.getName());

        if (StringUtils.isNotEmpty(password)) {
            user.setPassword(Hash.encode(password));
        }

        String phone = userVO.getPhone();
        String eMail = userVO.getEmail();
        if (StringUtils.isNotEmpty(phone)) {
            user.setPhone(phone);
        }
        if (StringUtils.isNotEmpty(eMail)) {
            user.setEmail(eMail);
        }
        user.setUpdateUser(Sign.getUserId());
        user.setUpdateTime(DateUtil.getNow());

        BaseResponse baseResponse = new BaseResponse();
        if (userService.updateById(user)) {
//            userDepartmentService.updateUserDep(user.getId(), userVO.getDepartmentList());
//            userRoleService.updateUserRole(user.getId(), userVO.getRoleList());
//            userProjectService.updateUserProject(user.getId(), userVO.getProjectList());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @GetMapping("/is_admin")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParam(name = "userId", value = "??????id", paramType = "query", dataType = "long")
    public ResponseEntity<BooleanResponse> isAdmin(@RequestParam Long userId) {
        boolean isAdmin = false;
        List<RoleVO> roleVOList = roleService.qryRoleByUserId(userId);
        if (roleVOList != null && roleVOList.size() > 0) {
            for (RoleVO roleVO : roleVOList) {
                if ("sys_admin".equals(roleVO.getRoleName()) || "tenant_admin".equals(roleVO.getRoleName())) {
                    isAdmin = true;
                    break;
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new BooleanResponse(isAdmin));
    }

    @GetMapping("/right")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParam(name = "userId", value = "??????id", paramType = "query", dataType = "long")
    public ResponseEntity<RightResponse> getUserRight(@RequestParam(required = false) Long userId) {
        RightDTO rightDTO = new RightDTO();
        List<Long> uidList = new ArrayList<>();
        List<Long> ridList = new ArrayList<>();
        List<Long> didList = new ArrayList<>();
        List<Long> pidList = new ArrayList<>();
        List<Long> bgidList = new ArrayList<>();

        userId = userId == null ? Sign.getUserId() : userId;
        rightDTO.setId(userId);
        uidList.add(userId);

        QueryWrapper<UserRole> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.lambda().eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = this.userRoleService.list(roleQueryWrapper);
        if (userRoles != null && userRoles.size() > 0) {
            for (UserRole userRole : userRoles) {
                ridList.add(userRole.getRoleId());
            }
        }

        QueryWrapper<UserDepartment> depQueryWrapper = new QueryWrapper<>();
        depQueryWrapper.lambda().eq(UserDepartment::getUserId, userId);
        List<UserDepartment> userDepartments = this.userDepartmentService.list(depQueryWrapper);
        if (userDepartments != null && userDepartments.size() > 0) {
            for (UserDepartment userDepartment : userDepartments) {
                didList.add(userDepartment.getDepartmentId());
            }
        }

        QueryWrapper<UserProject> projectQueryWrapper = new QueryWrapper<>();
        projectQueryWrapper.lambda().eq(UserProject::getUserId, userId);
        List<UserProject> userProjects = this.userProjectService.list(projectQueryWrapper);
        if (userProjects != null && userProjects.size() > 0) {
            for (UserProject userProject : userProjects) {
                pidList.add(userProject.getProjectId());
            }
        }

        QueryWrapper<SysBusinessGroupMember> bgQueryWrapper = new QueryWrapper<>();
        bgQueryWrapper.lambda().eq(SysBusinessGroupMember::getUserId, userId);
        List<SysBusinessGroupMember> userBg = this.sysBusinessGroupMemberService.list(bgQueryWrapper);
        if (userBg != null && userBg.size() > 0) {
            for (SysBusinessGroupMember member : userBg) {
                bgidList.add(member.getBusinessGroupId());
            }
        }

        rightDTO.setUserIdList(uidList);
        rightDTO.setRoleIdList(ridList);
        rightDTO.setDepartmentIdList(didList);
        rightDTO.setProjectIdList(pidList);
        rightDTO.setBusinessGroupIdList(bgidList);

        return ResponseEntity.status(HttpStatus.OK).body(new RightResponse(rightDTO));

    }
}
