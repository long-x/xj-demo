package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.IaasTimedTask;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTimedTaskVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasBMListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasTimedTaskPageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasTimedTaskResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasVirtualMachineListResponse;
import com.ecdata.cmp.iaas.service.IaasTimedTaskService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/29 16:35
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/timed_task")
@Api(tags = "定时开关机相关的API")
public class IaasTimedTaskController {

    @Autowired
    private IaasTimedTaskService service;

    @PostMapping("/add")
    @ApiOperation(value = "新增开关机任务", notes = "新增开关机任务")
    public ResponseEntity<BaseResponse> add(@RequestBody IaasTimedTaskVO iaasTimedTaskVO) throws Exception {

        //响应基础对象
        BaseResponse baseResponse = null;
        //新增：成功、失败
        if (service.add(iaasTimedTaskVO)) {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse = new BaseResponse();
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    /**
     * 获取虚拟机列表
     *
     * @return
     */
    @GetMapping("/vm_list")
    @ApiOperation(value = "根据业务组id获取虚拟机列表", notes = "根据业务组id获取虚拟机列表")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "String")
    public ResponseEntity<IaasVirtualMachineListResponse> VmList(@RequestParam(required = false) String id) {
        List<IaasVirtualMachineVO> vmByGropu = service.getVMByGropu(id);
        if (vmByGropu != null && vmByGropu.size() > 0) {
            IaasVirtualMachineListResponse response = new IaasVirtualMachineListResponse();

            response.setData(vmByGropu);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IaasVirtualMachineListResponse(vmByGropu));
    }


    /**
     * 获取裸金属列表
     *
     * @return
     */
    @GetMapping("/bm_list")
    @ApiOperation(value = "根据业务组id获取裸金属列表", notes = "根据业务组id获取裸金属列表")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "String")
    public ResponseEntity<IaasBMListResponse> BmList(@RequestParam(required = false) String id) {
        List<BareMetalVO> bare = service.getBareByGropu(id);
        if (bare != null && bare.size() > 0) {
            IaasBMListResponse response = new IaasBMListResponse();

            response.setData(bare);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IaasBMListResponse(bare));
    }


    @PutMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除任务")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) throws Exception {
        log.info("删除任务 id id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (service.delTask(id)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }


    @GetMapping("/info")
    @ApiOperation(value = "根据id任务", notes = "根据id任务")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "Long")
    public ResponseEntity<IaasTimedTaskResponse> info(@RequestParam(required = true) Long id) {
        IaasTimedTaskResponse taskResponse = new IaasTimedTaskResponse();

        IaasTimedTaskVO taskInfo = service.getTaskInfo(id);
        if (taskInfo == null) {
            return ResponseEntity.status(HttpStatus.OK).body(taskResponse);
        }
        taskResponse.setData(taskInfo);
        return ResponseEntity.status(HttpStatus.OK).body(taskResponse);
    }



    @GetMapping("/page")
    @ApiOperation(value = "分页查看计划任务 ", notes = "分页查看计划任务 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20")
    })
    public ResponseEntity<IaasTimedTaskPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                          @RequestParam(defaultValue = "20", required = false) Integer pageSize) {

        Page<IaasTimedTaskVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();

        //调用分页查询方法
        IPage<IaasTimedTaskVO> result = service.getIaasTimedTaskVOPage(page, map);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasTimedTaskPageResponse(new PageVO<>(result)));
    }



    @PostMapping("/update")
    @ApiOperation(value = "更新计划任务", notes = "更新计划任务")
    public ResponseEntity<BaseResponse> update(@RequestBody IaasTimedTaskVO iaasTimedTaskVO) {
        IaasTimedTask timedTask = new IaasTimedTask();
        BeanUtils.copyProperties(iaasTimedTaskVO, timedTask);
        BaseResponse baseResponse = new BaseResponse();
        if (timedTask.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.MISS_PRIMARY_KEY));
        }
        if (service.updateById(timedTask)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }



}
