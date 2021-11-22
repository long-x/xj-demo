package com.ecdata.cmp.iaas.controller;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.entity.IaasProject;
import com.ecdata.cmp.iaas.entity.dto.IaasProjectVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasProjecResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasProjectListResponse;
import com.ecdata.cmp.iaas.service.IProjectService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @title: IaasVirtualDataCenter
 * @Author: shig
 * @description: iaas项目表
 * @Date: 2019/12/13 5:05 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/project")
@Api(tags = "🦊项目表 相关的API")
public class IaasProjectController {
    @Autowired
    private IProjectService projectService;

    @GetMapping("/getProjectNameByVdcId")
    @ApiOperation(value = "根据vdc id查询项目名称", notes = "根据vdc id查询项目名称")
    public ResponseEntity<IaasProjectListResponse> getProjectNameByVdcId(@RequestParam(required = true) Long vdcId) {
        //去重查询
        List<IaasProject> projects = projectService.getProjectNameByVdcId(vdcId);
        //响应对象
        List<IaasProjectVO> projectVOS = new ArrayList<>();
        //不为空，复制到拓展类
        if (projects != null && projects.size() > 0) {
            for (IaasProject project : projects) {
                IaasProjectVO projectVO = new IaasProjectVO();
                BeanUtils.copyProperties(project, projectVO);
                projectVOS.add(projectVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IaasProjectListResponse(projectVOS));
    }


    @GetMapping("/info")
    @ApiOperation(value = "根据项目id查询", notes = "根据项目id查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<IaasProjecResponse> info(@RequestParam(required = true) Long id) {
        IaasProjecResponse iaasProjecResponse = new IaasProjecResponse();
        //查询条件
        if (id == null) {
            id = Sign.getUserId();
        }
        //查询改id是否存在
        IaasProjectVO iaasProjectVO = projectService.getByProjecAndVdcId(id);
        if (iaasProjectVO == null) {
            return ResponseEntity.status(HttpStatus.OK).body(iaasProjecResponse);
        }
        iaasProjecResponse.setData(iaasProjectVO);
        return ResponseEntity.status(HttpStatus.OK).body(iaasProjecResponse);
    }

}