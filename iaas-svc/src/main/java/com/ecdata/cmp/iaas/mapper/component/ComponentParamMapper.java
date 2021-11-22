package com.ecdata.cmp.iaas.mapper.component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.component.IaasComponentParam;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentParamVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ComponentParamMapper extends BaseMapper<IaasComponentParam> {

    @Select("update iaas_component_param as pm " +
            "        set pm.update_user = #{updateUser}, update_time = NOW()" +
            "        WHERE pm.component_id = #{id} ")
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 根据用户id查询角色列表
     * @param compId    组件id
     * @return 参数列表
     */
//    List<IaasComponentParamVO> queryParamsById(Long compId);

    List<IaasComponentParamVO> qryComponentParamInfo(Long compId);
}
