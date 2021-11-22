package com.ecdata.cmp.user.controller;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.IntegerResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.ProjectVO;
import com.ecdata.cmp.user.dto.request.SyncProjectRequest;
import com.ecdata.cmp.user.dto.response.ProjectListResponse;
import com.ecdata.cmp.user.dto.response.ProjectPageResponse;
import com.ecdata.cmp.user.dto.response.ProjectResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.entity.Project;
import com.ecdata.cmp.user.service.IProjectService;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxinsheng
 * @since 2019-05-08
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/project")
@Api(tags = "项目相关接口")
public class ProjectController {

    /**
     * 项目Service
     */
    @Autowired
    private IProjectService projectService;
//    /**
//     * 项目资源池Service
//     */
//    @Autowired
//    private IProjectResourcePoolService projectResourcePoolService;


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询项目", notes = "根据id查询项目")
    @ApiImplicitParam(name = "id", value = "项目id", required = true, paramType = "path")
    public ResponseEntity<ProjectResponse> getById(@PathVariable(name = "id") Long id) {
        ProjectVO projectVO = new ProjectVO();
        Project project = projectService.getById(id);
        BeanUtils.copyProperties(project, projectVO);
        return ResponseEntity.status(HttpStatus.OK).body(new ProjectResponse(projectVO));
    }

    @GetMapping("/name/{projectName}/cluster/{clusterId}")
    @ApiOperation(value = "查询项目", notes = "根据项目名和集群id查询项目")
    public ResponseEntity<ProjectResponse> getByNameAndClusterId(@PathVariable(name = "projectName") String projectName,
                                                                 @PathVariable(name = "clusterId") Long clusterId) {
        ProjectVO projectVO = new ProjectVO();
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Project::getClusterId, clusterId).eq(Project::getProjectName, projectName);
        List<Project> projectList = this.projectService.list(queryWrapper);
        if (projectList != null && projectList.size() > 0) {
            BeanUtils.copyProperties(projectList.get(0), projectVO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ProjectResponse(projectVO));
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查看项目", notes = "分页查看项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "clusterId", value = "集群id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<ProjectPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                    @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                    @RequestParam(required = false) Long clusterId,
                                                    @RequestParam(required = false) String keyword) {
        Page<ProjectVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> params = new HashMap<>();
        params.put("clusterId", clusterId);
        params.put("keyword", keyword);
        IPage<ProjectVO> result = projectService.qryProjectInfo(page, params);
        return ResponseEntity.status(HttpStatus.OK).body(new ProjectPageResponse(new PageVO<>(result)));

    }

    @GetMapping("/list")
    @ApiOperation(value = "获取项目列表", notes = "获取项目列表")
    @ApiImplicitParam(name = "clusterId", value = "集群id", paramType = "query", dataType = "long")
    public ResponseEntity<ProjectListResponse> list(@RequestParam(required = false) Long clusterId) {
        List<ProjectVO> projectVOList = new ArrayList<>();
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        if (clusterId != null) {
            queryWrapper.lambda().eq(Project::getClusterId, clusterId);
        }
        List<Project> projectList = projectService.list(queryWrapper);
        if (projectList != null && projectList.size() > 0) {
            for (Project project : projectList) {
                ProjectVO projectVO = new ProjectVO();
                BeanUtils.copyProperties(project, projectVO);
                projectVOList.add(projectVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ProjectListResponse(projectVOList));
    }

    @GetMapping("/list/size")
    @ApiOperation(value = "获取项目数量", notes = "获取项目数量")
    @ApiImplicitParam(name = "clusterId", value = "集群id", paramType = "query", dataType = "long")
    public ResponseEntity<IntegerResponse> listSize(@RequestParam(required = false) Long clusterId) {
        List<ProjectVO> projectVOList = new ArrayList<>();
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        if (clusterId != null) {
            queryWrapper.lambda().eq(Project::getClusterId, clusterId);
        }
        List<Project> projectList = projectService.list(queryWrapper);
        if (projectList != null && projectList.size() > 0) {
            for (Project project : projectList) {
                ProjectVO projectVO = new ProjectVO();
                BeanUtils.copyProperties(project, projectVO);
                projectVOList.add(projectVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IntegerResponse(projectVOList.size()));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增项目", notes = "新增项目")
    public ResponseEntity<BaseResponse> add(@RequestBody ProjectVO projectVO) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Long clusterId = projectVO.getClusterId();
        if (clusterId == null) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("集群不能为空");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        String projectName = projectVO.getProjectName();
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Project::getClusterId, clusterId).eq(Project::getProjectName, projectName);
        if (this.projectService.count(queryWrapper) > 0) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("此集群已存在" + projectName);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        if (projectService.addProject(projectVO)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加项目成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加项目失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改项目", notes = "修改项目")
    public ResponseEntity<BaseResponse> update(@RequestBody ProjectVO projectVO) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        Long id = projectVO.getId();
        if (id == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        Long clusterId = projectVO.getClusterId();
        if (clusterId == null) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("集群不能为空");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        String projectName = projectVO.getProjectName();
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .ne(Project::getId, id)
                .eq(Project::getClusterId, clusterId)
                .eq(Project::getProjectName, projectName);
        if (this.projectService.count(queryWrapper) > 0) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("此集群已存在" + projectName);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        if (projectService.updateProject(projectVO)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新项目成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加项目失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除项目")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) throws Exception {
        log.info("删除项目 project id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (projectService.delProject(id)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除项目成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除项目失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/removeBatch")
    @ApiOperation(value = "批量删除", notes = "批量删除项目")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) throws Exception {
        log.info("删除项目 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
//            this.projectService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.projectService.delProject(Long.parseLong(id));
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除项目成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

//    @PostMapping("/allocateResourcePool")
//    @ApiOperation(value = "为项目分配资源池", notes = "为项目分配资源池")
//    public ResponseEntity<BaseResponse> allocateResourcePool(@RequestBody ProjectVO projectVO) {
//        try {
//            projectResourcePoolService.allocateResourcePool(projectVO.getId(), projectVO.getResourcePoolList());
//            return ResponseEntity.status(HttpStatus.OK).body(ResultVoUtil.success("分配资源池成功"));
//        } catch (Exception e) {
//            log.info("add project error :{}", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @GetMapping("/getProjectByUserId")
    @ApiOperation(value = "根据用户id获取项目列表", notes = "根据用户id获取项目列表")
    public ResponseEntity<ProjectListResponse> getProjectByUserId(@RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(new ProjectListResponse(projectService.getProjectByUserId(userId)));
    }

    @PostMapping("/refreshParentIdsStr")
    @ApiOperation(value = "刷新所有父主键字符串", notes = "刷新所有父主键字符串")
    public ResponseEntity<BaseResponse> refreshParentIdsStr() {
        BaseResponse baseResponse = new BaseResponse();
        this.projectService.refreshParentIdsStr();
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        baseResponse.setMessage("刷新所有父主键字符串成功");
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }


    @PostMapping("/{id}/openshift/sync")
    @ApiOperation(value = "同步指定项目", notes = "同步指定项目")
    @ApiImplicitParam(name = "id", value = "项目id", required = true, paramType = "path")
    public ResponseEntity<BaseResponse> syncProject(@PathVariable(name = "id") Long id) throws IOException {
        this.projectService.syncOpenShiftProject(id);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse());
    }

    @PostMapping("/openshift/sync")
    @ApiOperation(value = "同步openshift的项目", notes = "同步openshift的项目")
    public ResponseEntity<BaseResponse> syncAllProject(@RequestBody SyncProjectRequest syncProjectRequest) throws IOException {
        this.projectService.syncOpenShiftProject(syncProjectRequest.getClusterId(), syncProjectRequest.getNameList());
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse());
    }

}
