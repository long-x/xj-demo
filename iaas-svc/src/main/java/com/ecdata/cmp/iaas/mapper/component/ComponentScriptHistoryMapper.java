package com.ecdata.cmp.iaas.mapper.component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.component.IaasComponentScriptHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ComponentScriptHistoryMapper extends BaseMapper<IaasComponentScriptHistory> {
    @Select("update iaas_component_script_history as sc" +
            "        set sc.update_user = #{updateUser}, update_time = NOW()" +
            "        WHERE sc.component_history_id = #{id} ")
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);
}
