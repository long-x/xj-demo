package com.ecdata.cmp.user.mapper;

import com.ecdata.cmp.user.dto.DepartmentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-03-21
 */
@Mapper
@Repository
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 根据用户id查询部门列表
     * @param userId    用户id
     * @return 部门列表
     */
    List<DepartmentVO> qryDepByUserId(Long userId);
    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 查询所有子部门(包含子部门的子部门)
     * @param id    部门id
     * @return  子部门列表
     */
    List<Department> qryAllSubDeployment(Long id);

    /**
     * 查询用户的父部门
     * @param userId    用户id
     * @return  父部门列表
     */
    List<Department> qryParentDeploymentByUserId(Long userId);

    List<Long> qryUserIdByDepartIds(@Param("list") List<Long> ids);


    /**
     * 筛选-去掉已选择的部门
     * @return
     */
    List<DepartmentVO> qryTreeList();

    /**
     * 筛选-已选择的部门
     * @return
     */
    List<DepartmentVO> qryInTreeList();

}
