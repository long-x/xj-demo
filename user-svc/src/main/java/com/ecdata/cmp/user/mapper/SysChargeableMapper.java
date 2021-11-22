package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.dto.UserVO;
import com.ecdata.cmp.user.dto.chargeable.SysChargeableVO;
import com.ecdata.cmp.user.entity.SysChargeable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysChargeableMapper extends BaseMapper<SysChargeable> {

    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);


    @Select("SELECT\n" +
            "a.*,\n" +
            "\tp.id AS proId,\n" +
            "\tp.provider_name,\n" +
            "\trp.id AS poolId,\n" +
            "\trp.provider_id as rp_pro_id,\n" +
            "\trp.pool_name,\n" +
            "\tar.id AS areaId,\n" +
            "\tcl.id AS clustId \n" +
            "FROM\n" +
            "sys_chargeable a \n" +
            "left join\t iaas_provider p on a.provider_id=p.id\n" +
            "\tLEFT JOIN iaas_resource_pool rp ON a.pool_id = rp.id\n" +
            "\tLEFT JOIN iaas_area ar ON ar.id = a.area_id\n" +
            "\tLEFT JOIN iaas_cluster cl ON cl.id = a.cluster_id\n" +
            "\t\n" +
            "\t")
    List<SysChargeableVO> getIaasSelection();

    IPage<SysChargeableVO> qryChargeInfo(Page page,QueryWrapper<SysChargeable> queryWrapper);

}
