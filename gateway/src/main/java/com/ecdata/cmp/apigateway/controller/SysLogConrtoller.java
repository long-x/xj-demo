package com.ecdata.cmp.apigateway.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.apigateway.entity.SysLog;
import com.ecdata.cmp.apigateway.entity.response.SysLogPageResponse;
import com.ecdata.cmp.apigateway.entity.response.SysLogResponse;
import com.ecdata.cmp.apigateway.entity.response.SysLogVO;
import com.ecdata.cmp.apigateway.service.ISysLogService;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: log controller
 * @Author: shig
 * @description: 系统日志控制层
 * @Date: 2019/12/3 4:11 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sysLog")
@Api(tags = "🍌系统日志相关的API")
public class SysLogConrtoller {
    /**
     * 系统日志(SysLog) service
     */
    @Autowired
    private ISysLogService sysLogService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查看系统日志列表", notes = "分页查看系统日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "displayName", value = "用户名称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "startTime", value = "起始时间(yyyy-MM-dd HH24:mi:ss)", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "endTime", value = "结束时间(yyyy-MM-dd HH24:mi:ss)", paramType = "query", dataType = "string")
    })
    public ResponseEntity<SysLogPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                   @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                   @RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false) String displayName,
                                                   @RequestParam(required = false) String startTime,
                                                   @RequestParam(required = false) String endTime
    ) {
        Page<SysLogVO> page = new Page<>(pageNo, pageSize);
        //封装查询条件map
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", (pageNo-1)*pageSize);
        map.put("pageSize", pageSize);
        map.put("keyword", keyword);
        map.put("displayName", displayName);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        //页数
        IPage<SysLogVO> result = sysLogService.queryLogPageByMap(page, map);
        //总数
        Long count = sysLogService.querySysLogCount(map);
        //防止模糊查询查不到数据
        if (count != null) {
            result.setTotal(count);
        }else{
            result.setTotal(0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SysLogPageResponse(new PageVO<>(result)));
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取系统日志详细信息", notes = "获取系统日志详细信息")
    @ApiImplicitParam(name = "id", value = "系统日志id", paramType = "query", dataType = "string")
    public ResponseEntity<SysLogResponse> getInfo(@RequestParam(required = false) String id) {
        SysLogResponse logResponse = new SysLogResponse();
        SysLogVO logVO = new SysLogVO();
        SysLog log = sysLogService.getById(id == null ? Sign.getUserId() : id);
        if (log == null) {
            return ResponseEntity.status(HttpStatus.OK).body(logResponse);
        }
        BeanUtils.copyProperties(log, logVO);
        logResponse.setData(logVO);
        return ResponseEntity.status(HttpStatus.OK).body(logResponse);
    }

}