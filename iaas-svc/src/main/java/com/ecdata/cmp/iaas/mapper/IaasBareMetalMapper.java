package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasBareMetal;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
//import com.ecdata.cmp.huawei.dto.vo.BareMetalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface IaasBareMetalMapper extends BaseMapper<IaasBareMetal> {

    IPage<BareMetalVO> getBareMetalVOPage(Page<BareMetalVO> page, @Param("map") Map map);

}