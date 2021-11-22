package com.ecdata.cmp.iaas.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasImageService;
import com.ecdata.cmp.iaas.entity.dto.ImageServiceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IaasImageServiceMapper extends BaseMapper<IaasImageService> {

    IPage<ImageServiceVO> getImageServicePage(Page<ImageServiceVO> page, @Param("keyword") String keyword);

    IaasImageService queryImageByKey(String key);

    List<ImageServiceVO> getOsVersionList();

    List<ImageServiceVO> getOsVersions(@Param("imageVersion")String imageVersion);
}