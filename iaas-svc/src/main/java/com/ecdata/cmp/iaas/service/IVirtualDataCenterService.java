package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasVirtualDataCenter;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: IVirtualDataCenterService interface
 * @Author: shig
 * @description: 虚拟数据中心表(vdc)
 * @Date: 2019/12/13 4:34 下午
 */
public interface IVirtualDataCenterService extends IService<IaasVirtualDataCenter> {

    /**
     * 根据供应商id查询
     *
     * @param providerId
     * @return
     */
    List<IaasVirtualDataCenter> getVdcNameByProviderId(Long providerId);



    IaasVirtualDataCenterVO selectVdcById(String id);

}