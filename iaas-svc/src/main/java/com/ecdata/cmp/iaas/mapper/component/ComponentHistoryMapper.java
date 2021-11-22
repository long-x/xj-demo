package com.ecdata.cmp.iaas.mapper.component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.component.IaasComponentHistory;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ComponentHistoryMapper extends BaseMapper<IaasComponentHistory> {
    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    @Select("update iaas_component_history as comp" +
            "        set comp.update_user = #{updateUser}, update_time = NOW()" +
            "        WHERE comp.id = #{id} ")
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);


    List<IaasComponentHistoryVO> qryVersion(@Param("id") Long id);

    IaasComponentHistoryVO qryCompHisInfo(@Param("id") Long compHisId);

    IaasComponentHistoryVO qryUnionHistory(@Param("id") Long compHisId);

}
