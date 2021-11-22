package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.DepartmentVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.UserDepartment;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-04-19
 */
public interface IUserDepartmentService extends IService<UserDepartment> {

    /**
     * 批量插入用户部门
     * @param userId            用户id
     * @param departmentList    部门列表
     */
    void insertBatch(Long userId, List<DepartmentVO> departmentList);
    /**
     * 批量更新用户部门
     * @param userId            用户id
     * @param departmentList    部门列表
     */
    void updateUserDep(Long userId, List<DepartmentVO> departmentList);

}
