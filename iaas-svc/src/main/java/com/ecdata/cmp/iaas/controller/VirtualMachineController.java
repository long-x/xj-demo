package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasVirtualMachinePageResponse;
import com.ecdata.cmp.iaas.service.IaasVirtualMachineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: VirtualMachine
 * @Author: shig
 * @description: 虚拟机
 * @Date: 2019/11/25 4:43 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/virtualMachine")
@Api(tags = "🍀虚拟机 相关的API")
public class VirtualMachineController {

    /**
     * 虚拟机( VirtualMachine) service
     */
    @Autowired
    private IaasVirtualMachineService virtualMachineService;


    @GetMapping("/page")
    @ApiOperation(value = "分页查看虚拟机 ", notes = "分页查看虚拟机 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "projectId", value = "项目id(仅华为云-vdc—项目)", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "poolId", value = "资源池id", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasVirtualMachinePageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                               @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                               @RequestParam(required = false) String projectId,
                                                               @RequestParam(required = false) String poolId) {

        Page<IaasVirtualMachineVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("poolId", poolId);
        map.put("projectId", projectId);

        //调用分页查询方法
        IPage<IaasVirtualMachineVO> result = virtualMachineService.getVirtualMachineVOPage(page, map);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasVirtualMachinePageResponse(new PageVO<>(result)));
    }

}