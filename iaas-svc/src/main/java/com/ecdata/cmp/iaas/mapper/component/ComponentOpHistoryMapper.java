package com.ecdata.cmp.iaas.mapper.component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.component.IaasComponentOpHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ComponentOpHistoryMapper extends BaseMapper<IaasComponentOpHistory> {
    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    @Select("update iaas_component_operation_history as op" +
            "        set op.update_user = #{updateUser}, update_time = NOW()" +
            "        WHERE op.component_history_id = #{id} ")
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);
}
