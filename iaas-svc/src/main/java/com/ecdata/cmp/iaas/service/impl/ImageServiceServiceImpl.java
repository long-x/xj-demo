package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasImageService;
import com.ecdata.cmp.iaas.entity.dto.ImageServiceVO;
import com.ecdata.cmp.iaas.entity.dto.response.ImageServiceVOPageResponse;
import com.ecdata.cmp.iaas.mapper.IaasImageServiceMapper;
import com.ecdata.cmp.iaas.service.IImageServiceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @title: ImageService Service
 * @Author: shig
 * @description:
 * @Date: 2019/12/12 5:17 下午
 */
@Slf4j
@Service
public class ImageServiceServiceImpl extends ServiceImpl<IaasImageServiceMapper, IaasImageService> implements IImageServiceService {

    @Autowired
    private IaasImageServiceMapper iaasImageServiceMapper;

    @Override
    public IPage<ImageServiceVO> getImageServicePage(Page<ImageServiceVO> page, String keyword) {
        return baseMapper.getImageServicePage(page, keyword);
    }

    /**
     * 查询所有镜像列表
     * @return
     */
    @Override
    public List<IaasImageService> getAllImageList() {
        QueryWrapper<IaasImageService> query = new QueryWrapper<>();
        query.eq("is_deleted", 0);
        List<IaasImageService> userList = baseMapper.selectList(query);

//        Map<String,List<IaasImageService>> map=userList.stream().collect(Collectors.groupingBy(IaasImageService::getPlatform));


        return userList;
    }

    @Override
    public Map<String, List<String>> getOsVersionList() {
        Map<String, List<String>> map = new HashMap<>();
        List<ImageServiceVO> imageServiceVOS = iaasImageServiceMapper.getOsVersionList();
        if (CollectionUtils.isNotEmpty(imageServiceVOS)) {
            for (ImageServiceVO imageServiceVO: imageServiceVOS) {
                if (map.get(imageServiceVO.getOsType()) == null) {
                    map.put(imageServiceVO.getOsType(), new ArrayList<>());
                }
                Optional.ofNullable(map.get(imageServiceVO.getOsType())).ifPresent(e -> e.add(imageServiceVO.getOsVersion()));
            }
        }

        return map;
    }

    @Override
    public List<ImageServiceVO> getName(String imageVersion) {
        return iaasImageServiceMapper.getOsVersions(imageVersion);
    }


}