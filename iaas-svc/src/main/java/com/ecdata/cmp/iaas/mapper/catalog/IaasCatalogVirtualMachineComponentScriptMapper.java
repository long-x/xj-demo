package com.ecdata.cmp.iaas.mapper.catalog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachineComponentScript;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineComponentScriptVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasCatalogVirtualMachineComponentScriptMapper extends BaseMapper<IaasCatalogVirtualMachineComponentScript> {
    /**
     * 通过组件id删除组件脚本
     *
     * @param vmComponentId
     */
    void deleteByVmComponentId(Long vmComponentId);

    /**
     * 通过组件id查询出组件脚本vo信息
     *
     * @param componentId
     * @return
     */
    List<IaasProcessApplyVirtualMachineComponentScriptVO> queryScripsByComponentId(Long componentId);
}
