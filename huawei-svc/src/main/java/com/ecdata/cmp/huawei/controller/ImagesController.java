package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.ImagesListResponse;
import com.ecdata.cmp.huawei.dto.vo.ImagesVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.service.ImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/3 16:37
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/images")
@Api(tags = "虚拟机镜像api")
public class ImagesController {


    @Autowired
    private ImagesService imagesService;

    @PutMapping("/get_images_list")
    @ApiOperation(value = "查询虚拟机镜像列表", notes = "查询虚拟机镜像列表")
    public ResponseEntity<ImagesListResponse> getImagesList(@RequestBody RequestVO requestVO) {
        try {
            List<ImagesVO> images = imagesService.getImages(requestVO);
            return ResponseEntity.status(HttpStatus.OK).body(new ImagesListResponse(images));
        } catch (Exception e) {
            log.info("getImagesList error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}
