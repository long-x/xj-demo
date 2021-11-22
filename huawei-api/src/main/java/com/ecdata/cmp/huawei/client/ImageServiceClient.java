package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.response.ImagesListResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

/**
 * @title: ImageServiceClient
 * @Author: shig
 * @description: 镜像服务客户端api
 * @Date: 2019/12/13 9:27 上午
 */
@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/images")
public interface ImageServiceClient {

    @PutMapping(path = "/get_images_list")
    @ApiOperation(value = "查询虚拟机镜像列表", notes = "查询虚拟机镜像列表")
    ImagesListResponse getImagesList(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                     @RequestBody RequestVO requestVO) throws IOException;
}

