package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasImageService;
import com.ecdata.cmp.iaas.entity.dto.ImageServiceVO;
import com.ecdata.cmp.iaas.entity.dto.response.ImageServiceVOPageResponse;

import java.util.List;
import java.util.Map;

public interface IImageServiceService extends IService<IaasImageService> {
    /**
     * 镜像模糊查询
     * @param page
     * @param keyword
     * @return
     */
    IPage<ImageServiceVO> getImageServicePage(Page<ImageServiceVO> page, String keyword);


    List<IaasImageService> getAllImageList();

    Map<String, List<String>> getOsVersionList();

    List<ImageServiceVO> getName(String imageVersion);
}
