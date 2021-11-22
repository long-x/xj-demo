package com.ecdata.cmp.iaas.mapper.component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.component.IaasComponentScript;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentScriptVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineComponentScriptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ComponentScriptMapper extends BaseMapper<IaasComponentScript> {


    @Select("update iaas_component_script as sc" +
            "        set sc.update_user = #{updateUser}, update_time = NOW()" +
            "        WHERE sc.component_id = #{id} ")
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 根据用户id查询角色列表
     *
     * @param compId 组件id
     * @return 脚本列表
     */
    List<IaasComponentScriptVO> queryScripsById(Long compId);

    List<IaasTemplateVirtualMachineComponentScriptVO> queryScripsByComponentId(Long scripId);
}
