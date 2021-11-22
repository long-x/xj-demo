package com.ecdata.cmp.iaas.mapper.catalog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalogVirtualMachineComponentOperation;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineComponentOperationVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface IaasCatalogVirtualMachineComponentOperationMapper extends BaseMapper<IaasCatalogVirtualMachineComponentOperation> {
    void deleteByVmComponentId(Long vmComponentId);

    List<IaasProcessApplyVirtualMachineComponentOperationVO> queryOperationByScriptId(Long componentId);
}
