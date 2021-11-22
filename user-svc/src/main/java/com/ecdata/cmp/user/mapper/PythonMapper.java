package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.Python;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author xuxinsheng
 * @since 2019-11-22
 */
@Mapper
@Repository
public interface PythonMapper extends BaseMapper<Python> {
    /**
     * 修改更新记录
     *
     * @param id         用户id
     * @param updateUser 更新用户id
     */
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);
}
