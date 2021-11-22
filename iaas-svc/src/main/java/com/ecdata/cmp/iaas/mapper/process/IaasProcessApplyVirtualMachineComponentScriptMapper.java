package com.ecdata.cmp.iaas.mapper.process;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineComponentScriptVO;
import com.ecdata.cmp.iaas.entity.process.IaasProcessApplyVirtualMachineComponentScript;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xxj
 */
@Mapper
@Repository
public interface IaasProcessApplyVirtualMachineComponentScriptMapper extends BaseMapper<IaasProcessApplyVirtualMachineComponentScript> {


    /**
     * 通过组件id查询出组件脚本vo信息
     *
     * @param componentId
     * @return
     */
    List<IaasProcessApplyVirtualMachineComponentScriptVO> queryScripsByComponentId(Long componentId);

    /**
     * 通过组件id删除组件脚本
     *
     * @param vmComponentId
     */
    void deleteByVmComponentId(Long vmComponentId);
}
