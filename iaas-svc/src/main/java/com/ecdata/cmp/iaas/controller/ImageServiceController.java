package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.IaasImageService;
import com.ecdata.cmp.iaas.entity.dto.ImageServiceVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasImageServiceListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasImageServiceResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasImageServiceTypeResponse;
import com.ecdata.cmp.iaas.entity.dto.response.ImageServiceVOPageResponse;
import com.ecdata.cmp.iaas.service.IImageServiceService;
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
import java.util.Map;

/**
 * @title: image service
 * @Author: shig
 * @description: 镜像服务
 * @Date: 2019/12/12 3:57 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/imageService")
@Api(tags = "🙉镜像服务 相关的API")
public class ImageServiceController {

    @Autowired
    private IImageServiceService imageServiceService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查看镜像服务 ", notes = "分页查看镜像服务 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<ImageServiceVOPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                           @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                           @RequestParam(required = false) String keyword) {
        Page<ImageServiceVO> page = new Page<>(pageNo, pageSize);
        //调用分页查询方法
        IPage<ImageServiceVO> result = imageServiceService.getImageServicePage(page, keyword);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new ImageServiceVOPageResponse(new PageVO<>(result)));
    }


    @GetMapping("/info")
    @ApiOperation(value = "获取镜像服务 信息", notes = "获取镜像服务 信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "镜像服务id", paramType = "query", dataType = "long")
    })
    public ResponseEntity<IaasImageServiceResponse> info(@RequestParam(required = true) Long id) {
        IaasImageServiceResponse imageServiceResponse = new IaasImageServiceResponse();
        ImageServiceVO imageServiceVO = new ImageServiceVO();
        if (id == null) {
            id = Sign.getUserId();
        }
        //查询改id是否存在
        IaasImageService iaasImageService = imageServiceService.getById(id);
        if (iaasImageService == null) {
            return ResponseEntity.status(HttpStatus.OK).body(imageServiceResponse);
        }
        BeanUtils.copyProperties(iaasImageService, imageServiceVO);
        imageServiceResponse.setData(imageServiceVO);
        return ResponseEntity.status(HttpStatus.OK).body(imageServiceResponse);
    }

    @GetMapping("/image_list")
    @ApiOperation(value = "获取镜像服务列表", notes = "获取镜像服务列表")
    public ResponseEntity<IaasImageServiceTypeResponse> imageList() {
        IaasImageServiceTypeResponse iaasImageServiceTypeResponse = new IaasImageServiceTypeResponse();

        Map<String, List<String>> allImageList = imageServiceService.getOsVersionList();

        if(allImageList != null && allImageList.size()>0){
            iaasImageServiceTypeResponse.setData(allImageList);
            return ResponseEntity.status(HttpStatus.OK).body(iaasImageServiceTypeResponse);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iaasImageServiceTypeResponse);
    }

    @GetMapping("/getImageServiceByOs_Version")
    @ApiOperation(value = "根据镜像操作系统版本获取镜像列表", notes = "根据镜像操作系统版本获取镜像列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imageVersion", value = "imageVersion", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "vdcId", value = "vdcId", paramType = "query", dataType = "String")
    })
    public ResponseEntity<IaasImageServiceListResponse> imageVersion(@RequestParam(required = false) String imageVersion) {
        IaasImageServiceListResponse imageServiceListResponse = new IaasImageServiceListResponse();

        List<ImageServiceVO> allImageList = imageServiceService.getName(imageVersion);

        if(allImageList != null && allImageList.size() > 0){
            imageServiceListResponse.setData(allImageList);
            return ResponseEntity.status(HttpStatus.OK).body(imageServiceListResponse);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(imageServiceListResponse);
    }


}