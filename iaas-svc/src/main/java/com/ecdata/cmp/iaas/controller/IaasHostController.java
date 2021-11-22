package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.dto.IaasHostVO;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasHostPageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.ResourcePoolPageResponse;
import com.ecdata.cmp.iaas.service.IHostService;
import com.ecdata.cmp.iaas.service.IResourcePoolService;
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
import java.util.Map;

/**
 * @title: host controller
 * @Author: shig
 * @description: 主机控制层
 * @Date: 2019/12/3 1:55 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/host")
@Api(tags = "🦀️主机相关的API")
public class IaasHostController {
    /**
     * 主机
     * service
     */
    @Autowired
    private IHostService hostService;

    @GetMapping("/page")
    @ApiOperation(value = "查询物理主机信息", notes = "查询物理主机信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "100"),
            @ApiImplicitParam(name = "clusterId", value = "集群id", paramType = "query", dataType = "long")
    })
    public ResponseEntity<IaasHostPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                     @RequestParam(defaultValue = "100", required = false) Integer pageSize,
                                                     @RequestParam(required = true) Long clusterId) {
        Page<IaasHostVO> page = new Page<>(pageNo, pageSize);
        //调用分页查询方法
        Map<String, Object> params = new HashMap<>();
        params.put("clusterId", clusterId);
        IPage<IaasHostVO> result = hostService.queryHostInfoPage(page, params);

        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasHostPageResponse(new PageVO<>(result)));
    }
}