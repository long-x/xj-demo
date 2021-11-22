package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.SysBusinessMemberAndUserAndPoolVO;
import com.ecdata.cmp.user.dto.SysBusinessMemberVO;
import com.ecdata.cmp.user.dto.response.SysBusinessMemberListResponse;
import com.ecdata.cmp.user.dto.response.SysBusinessMemberPageResponse;
import com.ecdata.cmp.user.dto.response.SysBusinessMemberResponse;
import com.ecdata.cmp.user.entity.SysBusinessGroupMember;
import com.ecdata.cmp.user.service.ISysBusinessGroupMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 19:04
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sys_business_member")
@Api(tags = "业务组成员相关接口")
public class SysBusinessMemberController {


    @Autowired
    private ISysBusinessGroupMemberService memberService;


    @PostMapping("/add")
    @ApiOperation(value = "新增业务组成员", notes = "新增业务组成员")
    public ResponseEntity<BaseResponse> add(@RequestBody SysBusinessMemberVO SysBusinessMemberVO) {
        SysBusinessGroupMember businessGroupMember = new SysBusinessGroupMember();
        BeanUtils.copyProperties(SysBusinessMemberVO, businessGroupMember);
        businessGroupMember.setId(SnowFlakeIdGenerator.getInstance().nextId());
        businessGroupMember.setCreateTime(DateUtil.getNow());
        BaseResponse baseResponse = new BaseResponse();
        if (memberService.save(businessGroupMember)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @PutMapping("/delete")
    @ApiOperation(value = "根据id删除业务组成员", notes = "根据id删除业务组成员")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("删除业务组成员 id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (memberService.delete(id)>0) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @PutMapping("/delete_batch")
    @ApiOperation(value = "批量删除业务组成员", notes = "批量删除业务组成员")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("批量删除业务组 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        List<String> list = Arrays.asList(idArray);
        if(memberService.deleteIn(list)){
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新业务组成员", notes = "更新业务组成员")
    public ResponseEntity<BaseResponse> update(@RequestBody SysBusinessMemberVO businessMemberVO) {
        SysBusinessGroupMember groupMember = new SysBusinessGroupMember();
        BeanUtils.copyProperties(businessMemberVO, groupMember);
        if (groupMember.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
        }
        BaseResponse baseResponse = new BaseResponse();
        if (memberService.updateById(groupMember)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update_correlation_user")
    @ApiOperation(value = "关联业务组成员", notes = "关联业务组成员")
    public ResponseEntity<BaseResponse> updateCorrelation(@RequestBody SysBusinessMemberAndUserAndPoolVO memberAndUserAndPoolVO) {
        BaseResponse baseResponse = new BaseResponse();
        if (memberService.updateCorrelationUser(memberAndUserAndPoolVO)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询业务组成员", notes = "根据id查询业务组成员")
    @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "path")
    public ResponseEntity<SysBusinessMemberResponse> getById(@PathVariable(name = "id") Long id) {
        SysBusinessMemberResponse memberResponse = new SysBusinessMemberResponse();
        SysBusinessGroupMember businessGroup = memberService.getById(id);
        if (businessGroup == null) {
            return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
        }
        SysBusinessMemberVO sysBusinessGroupVO = new SysBusinessMemberVO();
        BeanUtils.copyProperties(businessGroup, sysBusinessGroupVO);
        memberResponse.setData(sysBusinessGroupVO);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取全部业务组关联用户列表", notes = "获取全部业务组关联用户列表")
    public ResponseEntity<SysBusinessMemberListResponse> list() {
        List<SysBusinessMemberVO> memberVOList = new ArrayList<>();
        List<SysBusinessGroupMember> memberList = memberService.list();
        if (memberList != null && memberList.size() > 0) {
            for (SysBusinessGroupMember member : memberList) {
                SysBusinessMemberVO memberVO = new SysBusinessMemberVO();
                BeanUtils.copyProperties(member, memberVO);
                memberVOList.add(memberVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessMemberListResponse(memberVOList));
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查看业务组成员列表", notes = "分页查看业务组成员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<SysBusinessMemberPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                              @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                              @RequestParam(required = false) String keyword) {
        Page<SysBusinessMemberVO> page = new Page<>(pageNo, pageSize);
        IPage<SysBusinessMemberVO> result = memberService.qrySysBusinessMemberInfo(page, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(new SysBusinessMemberPageResponse(new PageVO<>(result)));
    }


}
