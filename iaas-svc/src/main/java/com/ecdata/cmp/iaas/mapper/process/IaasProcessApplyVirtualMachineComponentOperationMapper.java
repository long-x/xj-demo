package com.ecdata.cmp.iaas.mapper.process;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineComponentOperationVO;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachineComponentOperation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasProcessApplyVirtualMachineComponentOperationMapper extends BaseMapper<IaasProcessApplyVirtualMachineComponentOperation> {

    /**
     * 根据组件id查出组件操作信息
     *
     * @param componentId
     * @return
     */
    List<IaasProcessApplyVirtualMachineComponentOperationVO> queryOperationByComponentId(Long componentId);

    void deleteByVmComponentId(Long vmComponentId);
}
