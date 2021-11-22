package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import com.ecdata.cmp.user.entity.ResWorkorder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ResWorkorderMapper extends BaseMapper<ResWorkorder> {

    @Select("select d.id,ud.user_id, d.department_name from sys_department d left join " +
            "sys_user_department ud  on d.id=ud.department_id " +
            "where ud.user_id is not null and ud.user_id=#{uid}")
     List<Map<String,Object>> queryDepartments(Long uid);


    @Select("Select g.id,g.business_group_name,m.user_id from sys_business_group g left join" +
            " sys_business_group_member m on g.id=m.business_group_id " +
            "where m.user_id is not null and m.user_id=#{uid}")
    List<Map<String,Object>> queryBusinessGroups(Long uid);


    @Select("select temp.*,ivdc.vdc_name from (\n" +
            "select id as pro_id ,project_id, project_key,pool_id,iaas_project.vdc_id as vdc,inner_pool.vdc_id," +
            "pool_name,business_group_id \n" +
            "from iaas_project left join(\n" +
            "select iaas_resource_pool.id as pool_id,vdc_id,pool_name,project_id," +
            "sys_business_group_resource_pool.business_group_id \n" +
            "from iaas_resource_pool \n" +
            "right join sys_business_group_resource_pool on " +
            "iaas_resource_pool.id=sys_business_group_resource_pool.pool_id "+   //and " +"business_group_id = #{businessGroupId}\n" +

            "where iaas_resource_pool.id is not null ) inner_pool \n" +
            "on iaas_project.id=inner_pool.project_id where project_id is not null \n" +
            "and iaas_project.id=#{proId} ) as temp \n" +
            "left join iaas_virtual_data_center ivdc on vdc=ivdc.id and vdc is not null")
    Map<String,Object> queryVdcAndProject(Long proId);

    @Select("select * from res_workorder wo left join sys_business_group_resource_pool sp " +
            "on sp.business_group_id = wo.business_group_id where sp.pool_id=#{poolId}")
    ResWorkorder qryWorkOrderByPoolId(Long poolId);


    @Select("SELECT * FROM `iaas_virtual_data_center`")
    List<Map<String,Object>> qryAllVDCs();

    @Select("SELECT * FROM `iaas_virtual_data_center` where id = #{id}")
    Map<String,Object> qryVDCById(Long id);

    @Select("SELECT * FROM `iaas_virtual_data_center` where vdc_key = #{key}")
    Map<String,Object> qryVDCByKey(String key);

    @Select("select * from iaas_project WHERE vdc_id=#{vdcId}")
    List<Map<String,Object>> qryProjectsByVDCId(Long vdcId);

    @Select("select * from iaas_project where id = #{poolId}")
    Map<String,Object> qryOneProject(Long poolId);

    @Select("select * from iaas_project where project_key = #{key}")
    Map<String,Object> qryOneProjectByKey(String key);

}
