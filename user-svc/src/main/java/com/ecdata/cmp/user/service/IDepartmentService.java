package com.ecdata.cmp.user.service;

import com.ecdata.cmp.user.dto.DepartmentVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.Department;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-03-21
 */
public interface IDepartmentService extends IService<Department> {
    /**
     * 根据用户id查询部门列表
     * @param userId    用户id
     * @return 部门列表
     */
    List<DepartmentVO> qryDepByUserId(Long userId);

    /**
     * 查询所有树形结构部门
     *  @param keyword  关键字
     * @return 树形结构部门
     */
    List<DepartmentVO> getTreeList(String keyword);
    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 刷新所有父主键字符串
     */
    void refreshParentIdsStr();

    /**
     * 更新子部门的父主键字符串
     * @param department    部门信息
     */
    void updateSubParentIdsStr(Department department);

    /**
     * 查询用户的父部门
     * @param userId    用户id
     * @return  父部门列表
     */
    List<Department> qryParentDeploymentByUserId(Long userId);

    /**
     * 筛选-去掉已选择的部门
     * @return
     */
    List<DepartmentVO> qryTreeList(String keyword);

    //获取已选择部门id
    List<String> qryCheckTreeList();
}
