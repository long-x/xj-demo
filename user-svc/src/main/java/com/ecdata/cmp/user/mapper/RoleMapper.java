package com.ecdata.cmp.user.mapper;

import com.ecdata.cmp.user.dto.RoleVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-03-21
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查询角色列表
     * @param userId    用户id
     * @return 角色列表
     */
    List<RoleVO> qryRoleByUserId(Long userId);
    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    List<Long> qryUserIdByRole(@Param("list")List<String> roleName);

    List<Long> qryUserIdByRoleIds(@Param("list")List<Long> roleIds);
}
