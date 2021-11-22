package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.IaasDayTimes;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineMonitorVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasVirtualMachineMonitorListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasVirtualMachineMonitorPageResponse;
import com.ecdata.cmp.iaas.mapper.IaasDayTimesMapper;
import com.ecdata.cmp.iaas.service.IVirtualMachineMonitorService;
import com.ecdata.cmp.iaas.utils.DayUtils;
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

import java.util.*;

/**
 * @title: VirtualMachineMonitor
 * @Author: shig
 * @description: 虚拟机监控
 * @Date: 2019/11/25 4:43 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/virtualMachineMonitor")
@Api(tags = "🍂趋势统计:虚拟机监控相关的API")
public class VirtualMachineMonitorController {

    /**
     * 虚拟机监控( VirtualMachineMonitor) service
     */
    @Autowired
    private IVirtualMachineMonitorService virtualMachineMonitorService;

    /**
     * 天数日期主表
     */
    @Autowired
    private IaasDayTimesMapper iaasDayTimesMapper;

    @GetMapping("/list")
    @ApiOperation(value = "获取虚拟机监控使用趋势列表", notes = "获取虚拟机监控使用趋势列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeRange", value = "时间范围(1:最近7天,2:最近30天,3:最近3月,4:最近6月,5:最近一年)", paramType = "query", dataType = "string", defaultValue = "1"),
            @ApiImplicitParam(name = "usageRate", value = "使用率类型(1:CPU使用率,2:内存使用率)", paramType = "query", dataType = "string", defaultValue = "1"),
            @ApiImplicitParam(name = "clusterId", value = "集群id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型(1:虚拟机,2:集群,3:主机)", paramType = "query", dataType = "string", defaultValue = "2")
    })
    public ResponseEntity<IaasVirtualMachineMonitorListResponse> list(@RequestParam(defaultValue = "1", required = true) String timeRange,
                                                                      @RequestParam(defaultValue = "1", required = true) String usageRate,
                                                                      @RequestParam(required = false) String clusterId,
                                                                      @RequestParam(defaultValue = "2", required = true) String type) {
        //天数日期主表，先删除再新增
        setDayTime(timeRange);

        //查询条件
        IaasVirtualMachineMonitorVO iaasVirtualMachineMonitorVO = new IaasVirtualMachineMonitorVO();
        iaasVirtualMachineMonitorVO.setTimeRange(timeRange);
        iaasVirtualMachineMonitorVO.setUsageRate(usageRate);
        if (clusterId != null) {
            iaasVirtualMachineMonitorVO.setClusterId(Long.parseLong(clusterId));
        }
        iaasVirtualMachineMonitorVO.setType(Long.parseLong(type));
        //返回封装list对象
        List<IaasVirtualMachineMonitorVO> virtualMachineMonitorVOS = null;
        //type:2 集群菜单,3 宿主机菜单
        if ("2".equals(type)) {
            virtualMachineMonitorVOS = virtualMachineMonitorService.getClusterRateByVMMV(iaasVirtualMachineMonitorVO);
        } else {
            virtualMachineMonitorVOS = virtualMachineMonitorService.getHostRateByVMMV(iaasVirtualMachineMonitorVO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IaasVirtualMachineMonitorListResponse(virtualMachineMonitorVOS));
    }

    private void setDayTime(@RequestParam(defaultValue = "1", required = true) String timeRange) {
        int day = 7;
        if ("2".equals(timeRange)) {
            day = 30;
        }
        if ("3".equals(timeRange)) {
            day = 90;
        }
        if ("4".equals(timeRange)) {
            day = 180;
        }
        if ("5".equals(timeRange)) {
            day = 365;
        }

        //删除全部
        iaasDayTimesMapper.deleteAll();
        List<String> times = DayUtils.getDaysBetwwen(day);
        //批量新增
        iaasDayTimesMapper.insertForeach(times);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查看虚拟机监控利用率 ", notes = "分页查看虚拟机监控利用率 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "timeRange", value = "时间范围(1:最近7天,2:最近30天,3:最近3月,4:最近6月,5:最近一年)", paramType = "query", dataType = "string", defaultValue = "1"),
            @ApiImplicitParam(name = "usageRate", value = "使用率类型(1:CPU使用率,2:内存使用率)", paramType = "query", dataType = "string", defaultValue = "1"),
            @ApiImplicitParam(name = "clusterId", value = "集群id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型(1:虚拟机,2:集群,3:主机)", paramType = "query", dataType = "string", defaultValue = "2"),
    })
    public ResponseEntity<IaasVirtualMachineMonitorPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                                      @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                                      @RequestParam(defaultValue = "1", required = true) String timeRange,
                                                                      @RequestParam(defaultValue = "1", required = true) String usageRate,
                                                                      @RequestParam(required = false) String clusterId,
                                                                      @RequestParam(defaultValue = "2", required = true) String type) {
        //查询条件
        Map<String, Object> map = new HashMap<>();
        map.put("timeRange", timeRange);
        map.put("usageRate", usageRate);
        map.put("type", type);
        if (clusterId != null) {
            map.put("clusterId", clusterId);
        }
        //返回对象
        Page<IaasVirtualMachineMonitorVO> page = new Page<>(pageNo, pageSize);
        //type:2 集群菜单,3 宿主机菜单
        IPage<IaasVirtualMachineMonitorVO> result = null;
        if ("2".equals(type)) {
            result = virtualMachineMonitorService.getClusterRateByVMMVPage(page, map);
        } else {
            result = virtualMachineMonitorService.getHostRateByVMMVPage(page, map);
        }
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasVirtualMachineMonitorPageResponse(new PageVO<>(result)));
    }

}