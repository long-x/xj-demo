package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.user.dto.DepartmentVO;
import com.ecdata.cmp.user.entity.SysBusinessGroupDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 18:58
 * @modified By：
 */
@Mapper
@Repository
public interface SysBusinessGroupDepartmentMapper extends BaseMapper<SysBusinessGroupDepartment> {




    /**
     * 业务组 移除/关联 用户
     * @param businessGroupId
     * @return
     */
    int deleteDepartmentByGroupId(Long businessGroupId);

    /**
     * 查询业务组下面所有关联的部门
     * @param id
     * @return
     */
    Page<DepartmentVO> getDepartmentList(Page page, @Param("id") Long id);


    int deleteDepartment( @Param("businessGroupId")Long businessGroupId,@Param("departmentId") Long departmentId);

    /**
     * 根据部门id查找业务组id
     * @param departmentId
     * @return
     */
    SysBusinessGroupDepartment getGroupIdByDepartment(@Param("departmentId")Long departmentId);

}
