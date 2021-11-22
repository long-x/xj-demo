package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineMonitor;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineMonitorVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @title: IVirtualMachineMonitorService interface
 * @Author: shig
 * @description: 虚拟监控接口
 * @Date: 2019/11/25 4:34 下午
 */
public interface IVirtualMachineMonitorService extends IService<IaasVirtualMachineMonitor> {
    List<IaasVirtualMachineMonitor> queryVMMVoInfoByType(Long type);

    List<IaasVirtualMachineMonitorVO> getClusterRateByVMMV(IaasVirtualMachineMonitorVO iaasVirtualMachineMonitorVO);

    List<IaasVirtualMachineMonitorVO> getHostRateByVMMV(IaasVirtualMachineMonitorVO iaasVirtualMachineMonitorVO);

    IPage<IaasVirtualMachineMonitorVO> getClusterRateByVMMVPage(Page<IaasVirtualMachineMonitorVO> page, @Param("map") Map map);

    IPage<IaasVirtualMachineMonitorVO> getHostRateByVMMVPage(Page<IaasVirtualMachineMonitorVO> page, @Param("map") Map map);
}
