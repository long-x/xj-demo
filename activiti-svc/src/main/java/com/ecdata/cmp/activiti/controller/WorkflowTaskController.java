package com.ecdata.cmp.activiti.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.activiti.dto.response.WorkflowTaskListResponse;
import com.ecdata.cmp.activiti.dto.response.WorkflowTaskPageResponse;
import com.ecdata.cmp.activiti.dto.response.WorkflowTaskResponse;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskCandidateVO;
import com.ecdata.cmp.activiti.dto.vo.WorkflowTaskVO;
import com.ecdata.cmp.activiti.entity.WorkflowTask;
import com.ecdata.cmp.activiti.entity.WorkflowTaskCandidate;
import com.ecdata.cmp.activiti.service.IActivitiService;
import com.ecdata.cmp.activiti.service.IWorkflowTaskCandidateService;
import com.ecdata.cmp.activiti.service.IWorkflowTaskService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
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

import java.util.Arrays;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2020-04-15
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/workflow_task")
@Api(tags = "???????????????????????????")
public class WorkflowTaskController {

    /**
     * ???????????????Service
     */
    @Autowired
    private IWorkflowTaskService workflowTaskService;

    /**
     * ?????????????????????Service
     */
    @Autowired
    private IWorkflowTaskCandidateService workflowTaskCandidateService;

    @GetMapping("/{id}")
    @ApiOperation(value = "??????id?????????????????????", notes = "??????id?????????????????????")
    @ApiImplicitParam(name = "id", value = "???????????????id", required = true, paramType = "path")
    public ResponseEntity<WorkflowTaskResponse> getById(@PathVariable(name = "id") Long id) {
        WorkflowTaskVO workflowTaskVO = new WorkflowTaskVO();
        WorkflowTask workflowTask = workflowTaskService.getById(id);
        BeanUtils.copyProperties(workflowTask, workflowTaskVO);
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowTaskResponse(workflowTaskVO));

    }

    @GetMapping("/page")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "?????????", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "???????????????", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "workflowId", value = "?????????id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "workflowStep", value = "???????????????", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "processInstanceId", value = "????????????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "taskId", value = "??????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "isApproved", value = "???????????????(0:?????????;1:?????????)", paramType = "query", dataType = "int")
    })
    public ResponseEntity<WorkflowTaskPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                         @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                         @RequestParam(required = false) Long workflowId,
                                                         @RequestParam(required = false) Integer workflowStep,
                                                         @RequestParam(required = false) String processInstanceId,
                                                         @RequestParam(required = false) String taskId,
                                                         @RequestParam(required = false) Integer isApproved) {
        QueryWrapper<WorkflowTask> queryWrapper = new QueryWrapper<>();
        if (workflowId != null) {
            queryWrapper.lambda().eq(WorkflowTask::getWorkflowId, workflowId);
        }
        if (workflowStep != null) {
            queryWrapper.lambda().eq(WorkflowTask::getWorkflowStep, workflowStep);
        }
        if (StringUtils.isEmpty(processInstanceId)) {
            queryWrapper.lambda().eq(WorkflowTask::getProcessInstanceId, processInstanceId);
        }
        if (StringUtils.isEmpty(taskId)) {
            queryWrapper.lambda().eq(WorkflowTask::getTaskId, taskId);
        }
        if (isApproved != null) {
            queryWrapper.lambda().eq(WorkflowTask::getIsApproved, isApproved);
        }
        Page<WorkflowTask> page = new Page<>(pageNo, pageSize);
        IPage<WorkflowTask> result = workflowTaskService.page(page, queryWrapper);
        List<WorkflowTask> workflowTaskList = result.getRecords();
        List<WorkflowTaskVO> workflowTaskVOList = workflowTaskService.transform(workflowTaskList);
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowTaskPageResponse(new PageVO<>(result, workflowTaskVOList)));

    }

    @GetMapping("/list")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workflowId", value = "?????????id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "workflowStep", value = "???????????????", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "processInstanceId", value = "????????????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "taskId", value = "??????id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "isApproved", value = "???????????????(0:?????????;1:?????????)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "detailFlag", value = "????????????(0:?????????????????????;1:????????????)",
                    paramType = "query", dataType = "int", defaultValue = "0")
    })
    public ResponseEntity<WorkflowTaskListResponse> list(@RequestParam(required = false) Long workflowId,
                                                         @RequestParam(required = false) Integer workflowStep,
                                                         @RequestParam(required = false) String processInstanceId,
                                                         @RequestParam(required = false) String taskId,
                                                         @RequestParam(required = false) Integer isApproved,
                                                         @RequestParam(required = false, defaultValue = "0") Integer detailFlag) {
        QueryWrapper<WorkflowTask> queryWrapper = new QueryWrapper<>();
        if (workflowId != null) {
            queryWrapper.lambda().eq(WorkflowTask::getWorkflowId, workflowId);
        }
        if (workflowStep != null) {
            queryWrapper.lambda().eq(WorkflowTask::getWorkflowStep, workflowStep);
        }
        if (StringUtils.isNotEmpty(processInstanceId)) {
            queryWrapper.lambda().eq(WorkflowTask::getProcessInstanceId, processInstanceId);
        }
        if (StringUtils.isNotEmpty(taskId)) {
            queryWrapper.lambda().eq(WorkflowTask::getTaskId, taskId);
        }
        if (isApproved != null) {
            queryWrapper.lambda().eq(WorkflowTask::getIsApproved, isApproved);
        }
        List<WorkflowTask> workflowTaskList = workflowTaskService.list(queryWrapper);
        List<WorkflowTaskVO> workflowTaskVOList = workflowTaskService.transform(workflowTaskList);
        if (workflowTaskVOList != null && workflowTaskVOList.size() > 0 && detailFlag != null && detailFlag == 1) {
            for (WorkflowTaskVO workflowTaskVO : workflowTaskVOList) {
                List<WorkflowTaskCandidate> candidateList = this.workflowTaskCandidateService.listCandidate(workflowTaskVO.getId(), null);
                workflowTaskVO.setCandidateList(this.workflowTaskCandidateService.transform(candidateList));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new WorkflowTaskListResponse(workflowTaskVOList));
    }

    @PostMapping("/add")
    @ApiOperation(value = "?????????????????????", notes = "?????????????????????")
    public ResponseEntity<BaseResponse> add(@RequestBody WorkflowTaskVO workflowTaskVO) {
        BaseResponse baseResponse = this.workflowTaskService.addWorkflowTask(workflowTaskVO);
        if (baseResponse.getCode().equals(ResultEnum.DEFAULT_SUCCESS.getCode())) {
            baseResponse.setMessage("???????????????????????????");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("???????????????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }

    }

    @PutMapping("/update")
    @ApiOperation(value = "?????????????????????", notes = "?????????????????????")
    public ResponseEntity<BaseResponse> update(@RequestBody WorkflowTaskVO workflowTaskVO) {
        BaseResponse baseResponse = this.workflowTaskService.updateWorkflowTask(workflowTaskVO);
        if (baseResponse.getCode().equals(ResultEnum.DEFAULT_SUCCESS.getCode())) {
            baseResponse.setMessage("???????????????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("???????????????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }

    }

    @PutMapping("/remove")
    @ApiOperation(value = "??????", notes = "?????????????????????")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("????????????????????? workflowTask id???{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (workflowTaskService.removeById(id)) {
            workflowTaskService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("???????????????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("???????????????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }

    }

    @PutMapping("/remove_batch")
    @ApiOperation(value = "????????????", notes = "???????????????????????????")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("????????????????????? ids???{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("???????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            this.workflowTaskService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.workflowTaskService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("???????????????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }


    @PostMapping("/candidate/add")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    public ResponseEntity<BaseResponse> addCandidate(@RequestBody WorkflowTaskCandidateVO workflowTaskCandidateVO) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        if (workflowTaskCandidateVO.getWorkflowTaskId() == null) {
            baseResponse.setMessage("?????????????????????id");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        Long relateId = workflowTaskCandidateVO.getRelateId();
        if (relateId == null) {
            baseResponse.setMessage("????????????id");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        Integer type = workflowTaskCandidateVO.getType();
        if (type == null) {
            baseResponse.setMessage("????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
        WorkflowTaskCandidate candidate = new WorkflowTaskCandidate();
        BeanUtils.copyProperties(workflowTaskCandidateVO, candidate);
        candidate.setId(SnowFlakeIdGenerator.getInstance().nextId()).setRelatedParty(type + IActivitiService.SEPARATOR + relateId).setIsApproved(0)
                .setCreateUser(Sign.getUserId()).setCreateTime(DateUtil.getNow());
        if (workflowTaskCandidateService.save(candidate)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("???????????????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("???????????????????????????");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }
}
