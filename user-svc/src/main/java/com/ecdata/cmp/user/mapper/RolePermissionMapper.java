package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xuxinsheng
 * @since 2019-04-26
*/
@Mapper
@Repository
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

}
