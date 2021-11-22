package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.dto.IaasHostVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasHostInfoResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasHostListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasHostPageResponse;
import com.ecdata.cmp.iaas.service.IHostService;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @title: HostController controller
 * @Author: shig
 * @description: 主机 控制层
 * @Date: 2019/11/25 4:01 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/Host")
@Api(tags = "🐠主机 相关的API")
public class HostController {
    /**
     * 主机( Host) service
     */
    @Autowired
    private IHostService hostService;

    @GetMapping("/list")
    @ApiOperation(value = "❌（废弃)获取主机使用趋势列表", notes = "获取主机使用趋势列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeRange", value = "时间范围", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "usageRate", value = "使用率类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "clusterId", value = "集群id", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasHostListResponse> list(@RequestParam(required = true) String timeRange,
                                                     @RequestParam(required = true) String usageRate,
                                                     @RequestParam(required = false) String clusterId) {
        IaasHostVO iaasHostVO = new IaasHostVO();
        //查询
        return ResponseEntity.status(HttpStatus.OK).body(new IaasHostListResponse(hostService.getInfoByHostVO(iaasHostVO)));
    }

    @GetMapping("/page")
    @ApiOperation(value = "❌（废弃)分页查看主机利用率 ", notes = "分页查看主机利用率 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "timeRange", value = "时间范围", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "usageRate", value = "使用率类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "clusterId", value = "主机id", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasHostPageResponse>

    page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
         @RequestParam(defaultValue = "20", required = false) Integer pageSize,
         @RequestParam(required = true) String timeRange,
         @RequestParam(required = true) String usageRate,
         @RequestParam(required = false) String clusterId) {

        Page<IaasHostVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();

        //调用分页查询方法
        IPage<IaasHostVO> result = hostService.queryHostVoPage(page, map);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasHostPageResponse(new PageVO<>(result)));
    }


    @GetMapping("/qr_iss_host_list")
    @ApiOperation(value = "查询主机利用率", notes = "查询主机利用率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "state", value = "状态", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasHostPageResponse> qrIssHostList(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                              @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                              @RequestParam(required = false) String keyword,
                                                              @RequestParam(required = false) String state) {
        Page<IaasHostVO> page = new Page<>(pageNo, pageSize);
        //调用分页查询方法
        Map<String, Object> params = new HashMap<>();
        if (keyword != null) {
            params.put("keyword", keyword);
        }
        if (state != null) {
            params.put("state", state);
        }

        IPage<IaasHostVO> result = hostService.qrIssHostList(page, params);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasHostPageResponse(new PageVO<>(result)));
    }


    @GetMapping("/qr_iss_host_info/{id}")
    @ApiOperation(value = "根据id查询单个主机利用率", notes = "根据id查询单个主机利用率")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    public ResponseEntity<IaasHostInfoResponse> qrtIaasVirtualMachineInfo(@PathVariable(name = "id") String id) {

        IaasHostVO machineVO = hostService.qrIssHostInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body(new IaasHostInfoResponse(machineVO));
    }


}