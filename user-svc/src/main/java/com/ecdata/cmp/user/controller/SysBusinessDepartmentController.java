package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.DepartmentVO;
import com.ecdata.cmp.user.dto.SysBusinessMemberAndUserAndPoolVO;
import com.ecdata.cmp.user.dto.response.DepartmentPageResponse;
import com.ecdata.cmp.user.service.ISysBusinessGroupDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/20 11:12
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sys_business_department")
@Api(tags = "业务组部门接口")
public class SysBusinessDepartmentController {


    @Autowired
    private ISysBusinessGroupDepartmentService departmentService;


    @PostMapping("/add")
    @ApiOperation(value = "新增业务组部门关系", notes = "新增业务组部门关系")
    public ResponseEntity<BaseResponse> add(@RequestBody SysBusinessMemberAndUserAndPoolVO vo) {
        BaseResponse baseResponse = new BaseResponse();

        //保存成功再关联资源池和成员
        if (departmentService.updateCorrelationDepartment(vo)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        }
        baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
        baseResponse.setMessage("添加失败");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }


    @GetMapping("/get_list")
    @ApiOperation(value = "查询业务组下面的部门信息", notes = "查询业务组下面的部门信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "id", value = "业务组id", paramType = "query", dataType = "long")
    })

    public ResponseEntity<DepartmentPageResponse> getList(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                          @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                          @RequestParam(required = false) Long id) {

        Page<DepartmentVO> page = new Page<>(pageNo, pageSize);
        IPage<DepartmentVO> departmentList = departmentService.getDepartmentList(page, id);
        return ResponseEntity.status(HttpStatus.OK).body(new DepartmentPageResponse(new PageVO<>(departmentList)));

    }


    @PostMapping("/remove")
    @ApiOperation(value = "移除部门", notes = "移除部门")
    public ResponseEntity<BaseResponse> remove(@RequestBody SysBusinessMemberAndUserAndPoolVO vo) {
        BaseResponse baseResponse = new BaseResponse();

        //保存成功再关联资源池和成员
        if (departmentService.deleteDepartment(vo)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("移除成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        }
        baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
        baseResponse.setMessage("移除失败");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }


}
