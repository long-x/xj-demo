package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
//import com.ecdata.cmp.huawei.dto.vo.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasBareMetalPageResponse;
import com.ecdata.cmp.iaas.service.IaasBareMetalService;
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
 * @author ：xuj
 * @date ：Created in 2020/5/11 14:39
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/bare_metal")
@Api(tags = "裸金属 相关的API")
public class IaasBareMetalController {

    @Autowired
    private IaasBareMetalService iaasBareMetalService;


    @GetMapping("/page")
    @ApiOperation(value = "分页查看裸金属 ", notes = "分页查看裸金属 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "projectId", value = "项目id(仅华为云-vdc—项目)", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasBareMetalPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                          @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                          @RequestParam(required = false) String projectId) {

        Page<BareMetalVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("projectId", projectId);

        //调用分页查询方法
        IPage<BareMetalVO> result = iaasBareMetalService.getBareMetalVOPage(page, map);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasBareMetalPageResponse(new PageVO<>(result)));
    }

}
