package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.common.api.BaseResponse;
//import com.ecdata.cmp.huawei.dto.vo.BareMetalVO;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import com.ecdata.cmp.iaas.entity.IaasBareMetal;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import com.ecdata.cmp.iaas.entity.dto.response.MetalMapResponse;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/16 11:42
 * @modified By：
 */

public interface IaasBareMetalService extends IService<IaasBareMetal> {

    IPage<BareMetalVO> getBareMetalVOPage(Page<BareMetalVO> page, @Param("map") Map map);

    //纳管虚拟机或裸金属
    MetalMapResponse nanotubeResource(Long projectId);

    //纳管裸金属
    BaseResponse syncUpdate(ResourcePoolVO resourcePoolVO);
}
