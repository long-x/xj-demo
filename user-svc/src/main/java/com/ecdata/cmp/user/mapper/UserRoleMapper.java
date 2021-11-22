package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xuxinsheng
 * @since 2019-04-19
 */
@Mapper
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
