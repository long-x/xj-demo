package com.ecdata.cmp.activiti.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.activiti.dto.request.DisableRequest;
import com.ecdata.cmp.activiti.dto.response.WorkflowListResponse;
import com.ecdata.cmp.activiti.dto.response.WorkflowPageResponse;
import com.ecdata.cmp.activiti.dto.response.WorkflowResponse;
import com.ecdata.cmp.activiti.dto.response.WorkflowStepListResponse;
import com.ecdata.cmp.activiti.dto.response.WorkflowStepResponse;
import com.ecdata.cmp.activiti.dto.vo.WorkflowStepVO;
import com.ecdata.cmp.activiti.dto.vo.WorkflowVO;
import com.ecdata.cmp.activiti.entity.Workflow;
import com.ecdata.cmp.activiti.entity.WorkflowStep;
import com.ecdata.cmp.activiti.service.IWorkflowService;
import com.ecdata.cmp.activiti.service.IWorkflowStepRightService;
import com.ecdata.cmp.activiti.service.IWorkflowStepService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.dto.RightDTO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.response.RightResponse;
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
 * @since 2020-01-08
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/workflow")
@Api(tags = "工作流相关接口")
public class WorkflowController {

    /**
     * 工作流Service
     */
    @Autowired
    private IWorkflowService workflowService;

    /**
     * 工作流步骤Service
     */
    @Autowired
    private IWorkflowStepService workflowStepService;

    /**
     * 工作流步骤权限Service
     */
    @Autowired
    private IWorkflowStepRightService workflowStepRightService;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询工作流", notes = "根据id查询工作流")
    @ApiImplicitParam(name = "id", value = "工作流id", required = true, paramType = "path")
    public ResponseEntity<WorkflowResponse> getById(@PathVariable(name = "id") Long id) {
        WorkflowVO workflowVO = new WorkflowVO();
        Workflow workflow = workflowService.getById(id);
        BeanUtils.copyProperties(workflow, workflowVO);
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowResponse(workflowVO));

    }

    @GetMapping("/{id}/detail")
    @ApiOperation(value = "根据id查询工作流详情", notes = "根据id查询工作流详情")
    public ResponseEntity<WorkflowResponse> getDetailById(@PathVariable(name = "id") Long id) {
        WorkflowVO workflowVO = workflowService.getDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowResponse(workflowVO));
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查看工作流", notes = "分页查看工作流")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "type", value = "类型(1:默认类型;2:集团审批类型;3:paas默认审批类型;)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<WorkflowPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                     @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                     @RequestParam(required = false) Integer type,
                                                     @RequestParam(required = false) String keyword) {
        QueryWrapper<Workflow> queryWrapper = new QueryWrapper<>();
        if (type != null) {
            queryWrapper.lambda().eq(Workflow::getType, type);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(Workflow::getWorkflowName, keyword).or().like(Workflow::getRemark, keyword);
        }
        Page<Workflow> page = new Page<>(pageNo, pageSize);
        IPage<Workflow> result = workflowService.page(page, queryWrapper);
        List<Workflow> workflowList = result.getRecords();
        List<WorkflowVO> workflowVOList = workflowService.transform(workflowList);
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowPageResponse(new PageVO<>(result, workflowVOList)));

    }

    @GetMapping("/list")
    @ApiOperation(value = "获取工作流列表", notes = "获取工作流列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型(1:默认类型;2:集团审批类型;3:paas默认审批类型;)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<WorkflowListResponse> list(@RequestParam(required = false) Integer type,
                                                     @RequestParam(required = false) String keyword) {
        QueryWrapper<Workflow> queryWrapper = new QueryWrapper<>();
        if (type != null) {
            queryWrapper.lambda().eq(Workflow::getType, type);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(Workflow::getWorkflowName, keyword).or().like(Workflow::getRemark, keyword);
        }
        List<Workflow> workflowList = workflowService.list(queryWrapper);
        List<WorkflowVO> workflowVOList = workflowService.transform(workflowList);
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowListResponse(workflowVOList));

    }

    @PostMapping("/add")
    @ApiOperation(value = "新增工作流", notes = "新增工作流")
    public ResponseEntity<BaseResponse> add(@RequestBody WorkflowVO workflowVO) {
        BaseResponse baseResponse = this.workflowService.addWorkflow(workflowVO);
        if (baseResponse.getCode().equals(ResultEnum.DEFAULT_SUCCESS.getCode())) {
            baseResponse.setMessage("添加工作流成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加工作流失败");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }

    }

    @PutMapping("/update")
    @ApiOperation(value = "修改工作流", notes = "修改工作流")
    public ResponseEntity<BaseResponse> update(@RequestBody WorkflowVO workflowVO) {
        BaseResponse baseResponse = this.workflowService.updateWorkflow(workflowVO);
        if (baseResponse.getCode().equals(ResultEnum.DEFAULT_SUCCESS.getCode())) {
            baseResponse.setMessage("修改工作流成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改工作流失败");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }

    }

    @PutMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除工作流")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("删除工作流 workflow id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (workflowService.removeById(id)) {
            workflowService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除工作流成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除工作流失败");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }

    }

    @PutMapping("/remove_batch")
    @ApiOperation(value = "批量删除", notes = "批量删除工作流")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("删除工作流 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            this.workflowService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.workflowService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除工作流成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    @PutMapping("/update_disabled_state")
    @ApiOperation(value = "修改工作流禁用状态", notes = "修改工作流禁用状态")
    public ResponseEntity<BaseResponse> updateDisableState(@RequestBody DisableRequest disableRequest) {
        BaseResponse baseResponse = new BaseResponse();
        Long id = disableRequest.getId();
        if (id == null) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("缺少工作流id");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        Integer isDisabled = disableRequest.getIsDisabled();
        if (isDisabled == null) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("缺少禁用状态");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        boolean b = this.workflowService.updateDisableState(id, isDisabled);
        if (b) {
            baseResponse.setMessage("修改工作流禁用状态成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改工作流禁用状态失败");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    @GetMapping("/step")
    @ApiOperation(value = "获取工作流步骤", notes = "获取工作流步骤")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workflowId", value = "工作流id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "workflowStepId", value = "工作流步骤id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "sort", value = "排序", paramType = "query", dataType = "int")
    })
    public ResponseEntity<WorkflowStepListResponse> getWorkflowStep(@RequestParam(required = false) Long workflowId,
                                                                    @RequestParam(required = false) Long workflowStepId,
                                                                    @RequestParam(required = false) Integer sort) {
        List<WorkflowStepVO> retList = new ArrayList<>();
        QueryWrapper<WorkflowStep> queryWrapper = new QueryWrapper<>();
        if (workflowId != null) {
            queryWrapper.lambda().eq(WorkflowStep::getWorkflowId, workflowId);
        }
        if (workflowStepId != null) {
            queryWrapper.lambda().eq(WorkflowStep::getId, workflowStepId);
        }
        if (sort != null) {
            queryWrapper.lambda().eq(WorkflowStep::getSort, sort);
        }
        List<WorkflowStep> stepList = this.workflowStepService.list(queryWrapper);
        if (stepList != null && stepList.size() > 0) {
            for (WorkflowStep step : stepList) {
                WorkflowStepVO vo = new WorkflowStepVO();
                BeanUtils.copyProperties(step, vo);
                retList.add(vo);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowStepListResponse(retList));
    }

    @GetMapping("/step_ct")
    @ApiOperation(value = "城投定制获取工作流步骤", notes = "城投定制获取工作流步骤")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workflowId", value = "工作流id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "workflowStepId", value = "工作流步骤id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "sort", value = "排序", paramType = "query", dataType = "int")
    })
    public ResponseEntity<WorkflowStepListResponse> getWorkflowStepCt(@RequestParam(required = false) Long workflowId,
                                                                      @RequestParam(required = false) Long workflowStepId,
                                                                      @RequestParam(required = false) Integer sort) {
        List<WorkflowStepVO> retList = new ArrayList<>();
        QueryWrapper<WorkflowStep> queryWrapper = new QueryWrapper<>();
        if (workflowId != null) {
            queryWrapper.lambda().eq(WorkflowStep::getWorkflowId, workflowId);
        }
        if (workflowStepId != null) {
            queryWrapper.lambda().eq(WorkflowStep::getId, workflowStepId);
        }
        if (sort != null) {
            queryWrapper.lambda().eq(WorkflowStep::getSort, sort);
        }
        List<WorkflowStep> stepList = this.workflowStepService.list(queryWrapper);
        if (stepList != null && stepList.size() > 0) {
            for (WorkflowStep step : stepList) {
                WorkflowStepVO vo = new WorkflowStepVO();
                BeanUtils.copyProperties(step, vo);
                if ("同意".equals(vo.getOptionOne()) || "结束".equals(vo.getOptionOne())) {
                    vo.setOptionOne(vo.getOptionOne());
                }

                if ("驳回".equals(vo.getOptionThree())) {
                    vo.setOptionThree(vo.getOptionThree());
                }
                vo.setOptionTwo("");

//                if (sort == 7) {
//                    vo.setOptionTwo("变更流程");
//                }

                if (sort == 8) {
                    vo.setOptionThree("");
                }

                retList.add(vo);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowStepListResponse(retList));
    }

    @GetMapping("/next_step")
    @ApiOperation(value = "获取下一步工作流步骤", notes = "获取下一步工作流步骤")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workflowId", value = "工作流id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "sort", value = "排序", paramType = "query", dataType = "int")
    })
    public ResponseEntity<WorkflowStepResponse> getNextWorkflowStep(@RequestParam Long workflowId,
                                                                    @RequestParam Integer sort) {
        WorkflowStep step = this.workflowStepService.getNextWorkflowStep(workflowId, sort);
        WorkflowStepVO stepVO = new WorkflowStepVO();
        if (step != null) {
            BeanUtils.copyProperties(step, stepVO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowStepResponse(stepVO));
    }

    @GetMapping("/get_next_step_right")
    @ApiOperation(value = "获取下一步的权限信息", notes = "获取下一步的权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workflowId", value = "工作流id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "currentSort", value = "当前序号", paramType = "query", dataType = "int")
    })
    public ResponseEntity<RightResponse> getNextStepRight(@RequestParam Long workflowId,
                                                          @RequestParam Integer currentSort) {
        RightDTO rightDTO = new RightDTO();
        WorkflowStep step = this.workflowStepService.getNextWorkflowStep(workflowId, currentSort);
        if (step != null && step.getId() != null) {
            rightDTO = this.workflowStepRightService.getRight(step.getId());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new RightResponse(rightDTO));
    }
}
