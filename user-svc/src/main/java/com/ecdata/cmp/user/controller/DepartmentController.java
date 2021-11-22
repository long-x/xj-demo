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
import com.ecdata.cmp.user.dto.DepartmentVO;
import com.ecdata.cmp.user.dto.response.DepartmentListResponse;
import com.ecdata.cmp.user.dto.response.DepartmentPageResponse;
import com.ecdata.cmp.user.dto.response.DepartmentResponse;
import com.ecdata.cmp.user.entity.Department;
import com.ecdata.cmp.user.service.IDepartmentService;
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

import java.util.*;

/**
 * @author xuxinsheng
 * @since 2019-03-19
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/department")
@Api(tags = "部门相关接口")
public class DepartmentController {

    /**
     * 部门Service
     */
    @Autowired
    private IDepartmentService departmentService;


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询部门", notes = "根据id查询部门")
    @ApiImplicitParam(name = "id", value = "部门id", required = true, paramType = "path")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable(name = "id") Long id) {
        Department department = departmentService.getById(id);
        DepartmentVO departmentVO = new DepartmentVO();
        BeanUtils.copyProperties(department, departmentVO);
        return ResponseEntity.status(HttpStatus.OK).body(new DepartmentResponse(departmentVO));
    }

    @GetMapping("/getTreeList")
    @ApiOperation(value = "查询所有树形结构部门", notes = "查询所有树形结构部门")
    @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    public ResponseEntity<DepartmentListResponse> getTreeList(@RequestParam(required = false) String keyword) {
        List<DepartmentVO> list = departmentService.getTreeList(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(new DepartmentListResponse(list));
    }

    @GetMapping("/qry_tree_list")
    @ApiOperation(value = "筛选已选择树形结构部门", notes = "筛选已选择树形结构部门")
    @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    public Map<String,Object> qryTreeList(@RequestParam(required = false) String keyword) {
        Map<String,Object> map = new HashMap<>();


        List<DepartmentVO> list = departmentService.qryTreeList(keyword);
        List<String> checkId = departmentService.qryCheckTreeList();
        map.put("resultCode", "200");
        map.put("resultMsg", "成功");
        map.put("data", list);
        map.put("checkId", checkId);
        return map;
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查看部门", notes = "分页查看部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<DepartmentPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                       @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                       @RequestParam(required = false) String keyword) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(Department::getDepartmentName, keyword).or().like(Department::getDepartmentAlias, keyword);
        }
        Page<Department> page = new Page<>(pageNo, pageSize);
        IPage<Department> result = departmentService.page(page, queryWrapper);
        List<DepartmentVO> departmentVOList = new ArrayList<>();
        List<Department> departmentList = result.getRecords();
        if (departmentList != null && departmentList.size() > 0) {
            for (Department department : departmentList) {
                DepartmentVO departmentVO = new DepartmentVO();
                BeanUtils.copyProperties(department, departmentVO);
                departmentVOList.add(departmentVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new DepartmentPageResponse(new PageVO<>(result, departmentVOList)));


    }

    @GetMapping("/list")
    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
    public ResponseEntity<DepartmentListResponse> list() {
        List<Department> departmentList = departmentService.list();
        List<DepartmentVO> departmentVOList = new ArrayList<>();
        if (departmentList != null && departmentList.size() > 0) {
            for (Department department : departmentList) {
                DepartmentVO departmentVO = new DepartmentVO();
                BeanUtils.copyProperties(department, departmentVO);
                departmentVOList.add(departmentVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new DepartmentListResponse(departmentVOList));

    }

    @PostMapping("/add")
    @ApiOperation(value = "新增部门", notes = "新增部门")
    public ResponseEntity<BaseResponse> add(@RequestBody DepartmentVO departmentVO) {
        BaseResponse baseResponse = new BaseResponse();
        Department department = new Department();
        BeanUtils.copyProperties(departmentVO, department);
        Long parentId = department.getParentId();
        if (parentId == null || parentId < 1) {
            department.setParentIdsStr("0");
        } else {
            Department parent = this.departmentService.getById(parentId);
            String parentIdsStr = parent.getParentIdsStr();
            if (StringUtils.isEmpty(parentIdsStr)) {
                parentIdsStr = parentId.toString();
            } else {
                parentIdsStr = parentId + "," + parentIdsStr;
            }
            department.setParentIdsStr(parentIdsStr);
        }
        department.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow());
        if (departmentService.save(department)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加部门成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加部门失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改部门", notes = "修改部门")
    public ResponseEntity<BaseResponse> update(@RequestBody DepartmentVO departmentVO) {
        BaseResponse baseResponse = new BaseResponse();
        Department department = new Department();
        BeanUtils.copyProperties(departmentVO, department);
        Long id = department.getId();
        if (id == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Department::getId, id);
        Long parentId = department.getParentId();
        if (parentId == null) {
            queryWrapper.lambda().isNull(Department::getParentId);
        } else {
            queryWrapper.lambda().eq(Department::getId, parentId);
        }
        int count = this.departmentService.count(queryWrapper);
        if (count == 0) {
            if (parentId == null || parentId < 1) {
                department.setParentIdsStr("0");
            } else {
                Department parent = this.departmentService.getById(parentId);
                String parentIdsStr = parent.getParentIdsStr();
                if (StringUtils.isEmpty(parentIdsStr)) {
                    parentIdsStr = parentId.toString();
                } else {
                    parentIdsStr = parentId + "," + parentIdsStr;
                }
                department.setParentIdsStr(parentIdsStr);
            }
        }
        department.setUpdateUser(Sign.getUserId());
        department.setUpdateTime(DateUtil.getNow());
        if (departmentService.updateById(department)) {
            if (count == 0) {
                this.departmentService.updateSubParentIdsStr(department);
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新部门成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新部门失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除部门")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        BaseResponse baseResponse = new BaseResponse();
        log.info("删除部门 department id：{}", id);
        if (departmentService.removeById(id)) {
            departmentService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除部门成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除部门失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/removeBatch")
    @ApiOperation(value = "批量删除", notes = "批量删除部门")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("删除部门 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            this.departmentService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.departmentService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    @PostMapping("/refreshParentIdsStr")
    @ApiOperation(value = "刷新所有父主键字符串", notes = "刷新所有父主键字符串")
    public ResponseEntity<BaseResponse> refreshParentIdsStr() {
        BaseResponse baseResponse = new BaseResponse();
        this.departmentService.refreshParentIdsStr();
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("刷新所有父主键字符串成功");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

    }


}
