package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineMonitor;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineMonitorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface IaasVirtualMachineMonitorMapper extends BaseMapper<IaasVirtualMachineMonitor> {
    List<IaasVirtualMachineMonitorVO> getClusterRateByVMMV(IaasVirtualMachineMonitorVO iaasVirtualMachineMonitorVO);

    List<IaasVirtualMachineMonitorVO> getHostRateByVMMV(IaasVirtualMachineMonitorVO iaasVirtualMachineMonitorVO);

    IPage<IaasVirtualMachineMonitorVO> getClusterRateByVMMVPage(Page<IaasVirtualMachineMonitorVO> page, @Param("map") Map map);

    IPage<IaasVirtualMachineMonitorVO> getHostRateByVMMVPage(Page<IaasVirtualMachineMonitorVO> page, @Param("map") Map map);

    List<IaasVirtualMachineMonitor> queryVMMVoInfoByType(@Param("type") Long type);
}