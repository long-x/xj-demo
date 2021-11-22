package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.*;
import com.ecdata.cmp.user.dto.response.*;
import com.ecdata.cmp.user.entity.SysBusinessGroup;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.service.*;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/20 11:12
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sys_business_group")
@Api(tags = "业务组相关接口")
public class SysBusinessController {


    @Autowired
    private ISysBusinessGroupService sysBusinessGroupService;
    @Autowired
    private ISysBusinessGroupMemberService memberService;
    @Autowired
    private ISysBusinessGroupResourcePoolService poolService;

    /**
     * 用户Service
     */
    @Autowired
    private IUserService userService;
    /**
     * 角色Service
     */
    @Autowired
    private IRoleService roleService;





    @PostMapping("/add")
    @ApiOperation(value = "新增业务组", notes = "新增业务组")
    public ResponseEntity<BaseResponse> add(@RequestBody SysBusinessGroupVO sysBusinessGroupVO) {
        BaseResponse baseResponse = new BaseResponse();
        SysBusinessGroup sysBusinessGroup = new SysBusinessGroup();
        BeanUtils.copyProperties(sysBusinessGroupVO, sysBusinessGroup);
        sysBusinessGroup.setId(SnowFlakeIdGenerator.getInstance().nextId());
        sysBusinessGroup.setCreateTime(DateUtil.getNow());
        sysBusinessGroup.setUpdateTime(DateUtil.getNow());
        sysBusinessGroup.setCreateUser(Sign.getUserId());

        SysBusinessMemberAndUserAndPoolVO userAndPoolVO = new SysBusinessMemberAndUserAndPoolVO();

        //先保存业务组主表
        if (sysBusinessGroupService.save(sysBusinessGroup)) {
            userAndPoolVO.setBusinessGroupId(sysBusinessGroup.getId());
            userAndPoolVO.setUserId(sysBusinessGroupVO.getUserId());
            userAndPoolVO.setPoolId(sysBusinessGroupVO.getPoolId());
            userAndPoolVO.setPoolList(sysBusinessGroupVO.getPoolList());
            //保存成功再关联资源池和成员
            poolService.updateCorrelationPool(userAndPoolVO);
            memberService.updateCorrelationUser(userAndPoolVO);
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @PutMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除业务组(逻辑删除)")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("删除业务组 id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (sysBusinessGroupService.removeById(id)) {
            sysBusinessGroupService.modifyUpdateRecord(id, Sign.getUserId());

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @PutMapping("/remove_batch")
    @ApiOperation(value = "批量删除", notes = "批量删除业务组(逻辑删除)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("批量删除业务组 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            //业务组删除之前判断是否有下级业务组，或者有关联关系(用户/资源池)
                for (String id : idArray) {
                    boolean isDel = sysBusinessGroupService.getParentGroup(id);
                    if (isDel){
                        this.sysBusinessGroupService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                    }else{
                        baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
                        baseResponse.setMessage("业务组包含上下级关系，删除失败！");
                        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
                    }
                }
                baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
                baseResponse.setMessage("删除成功");
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

//            this.sysBusinessGroupService.removeByIds(Arrays.asList(idArray));
//            for (String id : idArray) {
//                this.sysBusinessGroupService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
//            }

        }
    }


    @PutMapping("/update")
    @ApiOperation(value = "更新业务组", notes = "更新业务组")
    public ResponseEntity<BaseResponse> update(@RequestBody SysBusinessGroupVO sysBusinessGroupVO) {
        SysBusinessGroup sysBusinessGroup = new SysBusinessGroup();
        BeanUtils.copyProperties(sysBusinessGroupVO, sysBusinessGroup);

        if (sysBusinessGroup.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
        }
        sysBusinessGroup.setUpdateUser(Sign.getUserId());
        sysBusinessGroup.setUpdateTime(DateUtil.getNow());
        BaseResponse baseResponse = new BaseResponse();

        SysBusinessMemberAndUserAndPoolVO userAndPoolVO = new SysBusinessMemberAndUserAndPoolVO();

        if (sysBusinessGroupService.updateById(sysBusinessGroup)) {
            userAndPoolVO.setBusinessGroupId(sysBusinessGroup.getId());
            userAndPoolVO.setUserId(sysBusinessGroupVO.getUserId());
            userAndPoolVO.setPoolId(sysBusinessGroupVO.getPoolId());
            userAndPoolVO.setPoolList(sysBusinessGroupVO.getPoolList());
            poolService.updateCorrelationPool(userAndPoolVO);
            memberService.updateCorrelationUser(userAndPoolVO);
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @GetMapping("/qur_user_by_groupid")
    @ApiOperation(value = "查询关联用户信息", notes = "查询关联用户信息")
    @ApiImplicitParam(name = "id", value = "主键id", paramType = "query", dataType = "long")
    public ResponseEntity<UserListResponse> getByIdaaa(@RequestParam(required = false) Long id) {
        List<Long> ids = new ArrayList<>();
        List<UserVO> userList = new ArrayList<UserVO>();
        if (id != null && !"".equals(id)) {
            ids.add(id);
        }
        List<Long> memberByIds = memberService.getMemberByIds(ids);
        for (Long userId : memberByIds) {
            UserVO userVO = new UserVO();
            User user = userService.getById(userId);
            BeanUtils.copyProperties(user, userVO);
            userVO.setRoleList(roleService.qryRoleByUserId(userId));
            userList.add(userVO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new UserListResponse(userList));
    }


    @GetMapping("/get_iaas_resource_pool")
    @ApiOperation(value = "根据业务组ID查询关联资源池", notes = "根据业务组ID查询关联资源池")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "id", value = "主键id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<SysBusinessGroupPoolListResponse> getIaasResourcepool(
            @RequestParam(defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(defaultValue = "20", required = false) Integer pageSize,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String keyword) {
        Page<SysBusinessGroupVO> page = new Page<>(pageNo, pageSize);
        IPage<SysBusinessGroupResourcePoolVO> resourcePpool = sysBusinessGroupService.getIaasResourcePpool(page, id,keyword);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupPoolListResponse(new PageVO<>(resourcePpool)));
    }

    @GetMapping("/getSysBusinessGroupBySnp/{id}")
    @ApiOperation(value = "根据业务组缩写查询序号", notes = "根据业务组缩写查询序号")
    @ApiImplicitParam(name = "id", value = "主键id", paramType = "query", dataType = "long")
    public ResponseEntity<SysBusinessGroupResponse> getSysBusinessGroupBySnp(@PathVariable(required = false) Long id) {
        SysBusinessGroupResponse sysBusinessGroupResponse = new SysBusinessGroupResponse();
        SysBusinessGroupVO sysBusinessGroupVO = new SysBusinessGroupVO();
        SysBusinessGroup businessGroup = sysBusinessGroupService.getById(id);
        if (businessGroup != null){
            BeanUtils.copyProperties(businessGroup, sysBusinessGroupVO);
            sysBusinessGroupResponse.setData(sysBusinessGroupVO);
            return ResponseEntity.status(HttpStatus.OK).body(sysBusinessGroupResponse);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sysBusinessGroupResponse);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据业务组id查询关联信息", notes = "根据业务组id查询关联信息")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path")
    public ResponseEntity<SysBusinessGroupResponse> getById(@PathVariable(name = "id") Long id) {
        SysBusinessGroupResponse sysBusinessGroupResponse = new SysBusinessGroupResponse();
        SysBusinessGroup businessGroup = sysBusinessGroupService.getById(id);
        //根据业务组查询用户id<list> 再根据用户id查询到所有用户信息 返回前端
        List<Long> ids = new ArrayList<>();
        List<UserVO> userList = new ArrayList<UserVO>();
        ids.add(id);
        List<Long> memberByIds = memberService.getMemberByIds(ids);
        for (Long userId : memberByIds) {
            UserVO userVO = new UserVO();
            User user = userService.getById(userId);
            BeanUtils.copyProperties(user, userVO);
            userVO.setRoleList(roleService.qryRoleByUserId(userId));
            userList.add(userVO);
        }
        //根据业务组查询资源池id   再关联查询资源池信息

        List<SysBusinessGroupResourcePoolVO> resourcePpool = sysBusinessGroupService.getIaasResourcePpool(id);
        if (businessGroup == null) {
            return ResponseEntity.status(HttpStatus.OK).body(sysBusinessGroupResponse);
        }
        SysBusinessGroupVO sysBusinessGroupVO = new SysBusinessGroupVO();
        sysBusinessGroupVO.setUserList(userList);
        sysBusinessGroupVO.setPoolList(resourcePpool);
        BeanUtils.copyProperties(businessGroup, sysBusinessGroupVO);
        sysBusinessGroupResponse.setData(sysBusinessGroupVO);
        return ResponseEntity.status(HttpStatus.OK).body(sysBusinessGroupResponse);
    }


    @GetMapping("/page")
    @ApiOperation(value = "分页查看业务组", notes = "分页查看业务组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<SysBusinessGroupListResponse> page(@RequestParam(required = false) String keyword) {
        List<SysBusinessGroupVO> result = sysBusinessGroupService.qrySysBusinessGroupInfo(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupListResponse(result));
    }


    @GetMapping("/get_group_list")
    @ApiOperation(value = "分页查看业务组", notes = "分页查看业务组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<SysBusinessGroupPageResponse> getGroupList(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                                     @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                                     @RequestParam(required = false) String keyword) {
        Page<SysBusinessGroupVO> page = new Page<>(pageNo, pageSize);
        IPage<SysBusinessGroupVO> result = sysBusinessGroupService.getGroupList(page, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupPageResponse(new PageVO<>(result)));
    }


    @GetMapping("/list")
    @ApiOperation(value = "获取全部业务组列表", notes = "获取全部业务组列表")
    public ResponseEntity<SysBusinessGroupListResponse> list() {
        List<SysBusinessGroupVO> sysBusinessGroupVOList = new ArrayList<>();
        List<SysBusinessGroup> sysBusinessGroupList = sysBusinessGroupService.list();
        if (sysBusinessGroupList != null && sysBusinessGroupList.size() > 0) {
            for (SysBusinessGroup sysBusinessGroup : sysBusinessGroupList) {
                SysBusinessGroupVO sysBusinessGroupVO = new SysBusinessGroupVO();
                BeanUtils.copyProperties(sysBusinessGroup, sysBusinessGroupVO);
                sysBusinessGroupVOList.add(sysBusinessGroupVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupListResponse(sysBusinessGroupVOList));
    }


    @GetMapping("/get_list_by_pool_id/{poolId}")
    @ApiOperation(value = "根据资源池id获取业务组列表", notes = "根据资源池id获取业务组列表")
    @ApiImplicitParam(name = "poolId", value = "资源池id", required = false, paramType = "path")
    public ResponseEntity<SysBusinessGroupListResponse> getlistByPoolId(@PathVariable(name = "poolId") Long poolId) {
        log.info("资源池id ={}", poolId);
        List<SysBusinessGroupVO> sysBusinessGroupList = sysBusinessGroupService.getlistByPoolId(poolId);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupListResponse(sysBusinessGroupList));
    }

    @GetMapping("/get_dis_business_group_name")
    @ApiOperation(value = "根据资源池id获取业务组名", notes = "根据资源池业务组名模糊获取业务组名")
    @ApiImplicitParam(name = "businessGroupName", value = "业务组名", required = false, paramType = "path")
    public ResponseEntity<SysBusinessGroupListResponse> getDisBusinessGroupName(@RequestParam(name = "businessGroupName", required = false) String businessGroupName) {
        log.info("业务组名 ={}", businessGroupName);
        List<SysBusinessGroupVO> sysBusinessGroupList = sysBusinessGroupService.getDisBusinessGroupName(businessGroupName);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupListResponse(sysBusinessGroupList));
    }

    @GetMapping("/qr_group_by_userid/{userId}")
    @ApiOperation(value = "根据用户id获取业务组", notes = "根据用户id获取业务组")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path")
    public ResponseEntity<SysBusinessGroupListResponse> qrGroupByUserId(@PathVariable(name = "userId") String userId) {
        log.info("用户id ={}", userId);
        List<SysBusinessGroupVO> list = sysBusinessGroupService.qrGroupByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupListResponse(list));
    }

    @GetMapping("/get_pool_ids_by_user_id/{userId}")
    @ApiOperation(value = "根据用户id获取资源id列表", notes = "根据用户id获取资源id列表")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型(1:iaas;2:paas;)", paramType = "query", dataType = "int")
    })
    public ResponseEntity<LongListResponse> getPoolIdsByUserId(@PathVariable(name = "userId") Long userId,
                                                               @RequestParam(required = false) Integer type) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("type", type);
        List<Long> ids = sysBusinessGroupService.getPoolIds(map);
        return ResponseEntity.status(HttpStatus.OK).body(new LongListResponse(ids));
    }


    @GetMapping("/user/distribution")
    @ApiOperation(value = "获取用业务组户分布信息", notes = "获取用业务组户分布信息")
    public ResponseEntity<DistributionResponse> getContainerDistribution() {
        DistributionDTO distribution = this.sysBusinessGroupService.getUserDistribution();
        return ResponseEntity.status(HttpStatus.OK).body(new DistributionResponse(distribution));
    }

    @PutMapping("/get_resource_pool_list")
    @ApiOperation(value = "根据资源池id获取资源池", notes = "根据资源池id获取资源池")
    public ResponseEntity<SysBusinessGroupPoolVOListResponse> getResourcePoolList(@RequestParam(name = "ids") String ids){
        String[] idArray = ids.split(",");
        List list = new ArrayList();
        for(int i=0;i<idArray.length;i++){
            list.add(idArray[i]);
        }
        List<SysBusinessGroupResourcePoolVO> poolList = sysBusinessGroupService.getResourcePoolList(list);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupPoolVOListResponse(poolList));
    }


    @GetMapping("/get_business_group_by_user")
    @ApiOperation(value = "根据用户id查询业务组", notes = "根据用户id查询业务组")
    @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "string")
    public ResponseEntity<SysBusinessGroupListResponse> getBusinessGroupByUser(
            @RequestParam(required = false)String userId) {
        log.info("用户ID ={}", userId);
        List<SysBusinessGroupVO> businessGroupByUser = sysBusinessGroupService.getBusinessGroupByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupListResponse(businessGroupByUser));
    }

    @GetMapping("/get_business_group_by_id")
    @ApiOperation(value = "根据id查询业务组信息", notes = "根据id查询业务组信息")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "string")
    public ResponseEntity<SysBusinessGroupResponse> getBusinessGroupById(
            @RequestParam(required = false)String id) {
        log.info("业务组id ={}", id);
        SysBusinessGroup businessGroupByUser = sysBusinessGroupService.getBusinessGroupById(id);
        SysBusinessGroupVO businessGroupVO = new SysBusinessGroupVO();
        BeanUtils.copyProperties(businessGroupByUser, businessGroupVO);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessGroupResponse(businessGroupVO));
    }


}
