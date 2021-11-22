package com.ecdata.cmp.user.mapper;

import com.ecdata.cmp.user.dto.PermissionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-04-26
 */
@Mapper
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 查询用户菜单权限
     *
     * @param userId 用户id
     * @return List<Permission>
     */
    List<Permission> queryByUser(@Param("userId") Long userId);

    /**
     * 查询用户按钮权限
     *
     * @param userId 用户id
     * @return List<Permission>
     */
    List<PermissionVO> queryButtonByUser(@Param("userId") Long userId);

    /**
     * 修改菜单是否子节点状态
     *
     * @param id     菜单id
     * @param isLeaf 是否叶子节点: 1:是; 0:不是
     */
    @Update("update sys_permission set is_leaf=#{isLeaf} where id = #{id}")
    void setMenuLeaf(@Param("id") Long id, @Param("isLeaf") int isLeaf);

    /**
     * 获取有权限的角色名
     *
     * @param permissionId 菜单权限id
     * @return List<String>
     */
    List<String> getRoleNameByPermissionId(Long permissionId);

    /**
     * 获取初始化时候的菜单信息
     *
     * @param type 类型 1：顶级租户(包含子租户内容)   2：子租户
     * @return List<Permission>
     */
    @Select("SELECT * FROM `sys_permission` WHERE init_type >= #{type}")
    List<Permission> getInitPermission(Integer type);
}
