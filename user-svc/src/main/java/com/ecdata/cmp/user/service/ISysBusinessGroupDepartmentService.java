package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.dto.DepartmentVO;
import com.ecdata.cmp.user.dto.SysBusinessMemberAndUserAndPoolVO;
import com.ecdata.cmp.user.dto.SysBusinessMemberVO;
import com.ecdata.cmp.user.entity.SysBusinessGroupDepartment;
import org.apache.ibatis.annotations.Param;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 18:51
 * @modified By：
 */
public interface ISysBusinessGroupDepartmentService extends IService<SysBusinessGroupDepartment> {


    /**
     * 业务组 移除/关联 部门
     * @param memberAndUserVO
     * @return
     */
    boolean updateCorrelationDepartment(SysBusinessMemberAndUserAndPoolVO memberAndUserVO);


    /**
     * 查询业务组下面所有关联的部门
     * @param id
     * @return
     */
    IPage<DepartmentVO> getDepartmentList(Page<DepartmentVO> page, Long id);

    /**
     * 移除部门
     * @return
     */
    boolean deleteDepartment(SysBusinessMemberAndUserAndPoolVO vo);


    SysBusinessGroupDepartment getGroupIdByDepartment(Long departmentId);
}
