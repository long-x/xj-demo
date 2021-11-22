package com.ecdata.cmp.iaas.mapper.component;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.component.IaasComponent;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ComponentMapper extends BaseMapper<IaasComponent> {

    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    @Select("update iaas_component as comp" +
            "        set comp.update_user = #{updateUser}, update_time = NOW()" +
            "        WHERE comp.id = #{id}")
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 建模使用
     * @param componentId
     * @return
     */
    List<IaasComponentVO> qryComponentInfo(@Param("componentId") Long componentId);

    /**
     * 参数下拉列表jsonArray
     * @param id
     * @return
     */
    IaasComponentVO qryComponentById(Long id);

    //没用到
//    List<IaasComponentVO> qryComponents();

    //没用到
//    IaasComponentVO qryOneComponent(@Param("id") Long id);

    /**
     * 查询一个组件VO，包含所有参数脚本操作，key
     * 全连接兼容冗余脚本和冗余操作，方便更新
     * @param id
     * @return
     */
    IaasComponentVO qryUnionComponent(@Param("id") Long id);


    /**
     * 带uname用户名的分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("select u.display_name as uname ,iaas.* from iaas_component iaas  " +
            "left join sys_user u on iaas.create_user=u.id where iaas.is_deleted=0")
    IPage<IaasComponentVO> queryPage(Page page,Wrapper<IaasComponent> queryWrapper);





}
