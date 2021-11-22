package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.UserProject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xuxinsheng
 * @since 2019-05-08
*/
@Mapper
@Repository
public interface UserProjectMapper extends BaseMapper<UserProject> {

}
