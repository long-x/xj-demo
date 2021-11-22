package com.ecdata.cmp.iaas.mapper.component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.component.IaasComponentOperation;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentOperationVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineComponentOperationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ComponentOperationMapper extends BaseMapper<IaasComponentOperation> {

    @Select("update iaas_component_operation as op" +
            "        set op.update_user = #{updateUser}, update_time = NOW()" +
            "        WHERE op.component_id = #{id} ")
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 根据用户id查询角色列表
     * @param compId    组件id
     * @return 操作列表
     */
    List<IaasComponentOperationVO> queryOpById(Long compId);

    List<IaasTemplateVirtualMachineComponentOperationVO> queryOperationByScriptId(Long scriptId);
}
