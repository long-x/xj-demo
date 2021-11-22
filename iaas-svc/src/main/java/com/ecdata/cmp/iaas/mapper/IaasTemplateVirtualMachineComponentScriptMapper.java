package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.IaasTemplateVirtualMachineComponentScript;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVirtualMachineComponentScriptVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-22 9:35
 */
@Mapper
@Repository
public interface IaasTemplateVirtualMachineComponentScriptMapper extends BaseMapper<IaasTemplateVirtualMachineComponentScript> {

    void deleteByVmComponentId(Long vmComponentId);

    List<IaasCatalogVirtualMachineComponentScriptVO> queryTemplateScripsByComponentId(Long componentId);
}
