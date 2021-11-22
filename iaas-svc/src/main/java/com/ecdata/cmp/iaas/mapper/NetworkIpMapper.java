package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.iaas.entity.NetworkIp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author xuxinsheng
 * @since 2019-08-08
*/
@Mapper
@Repository
public interface NetworkIpMapper extends BaseMapper<NetworkIp> {
    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    @Update(" update iaas_network_ip set update_user = #{updateUser}, update_time = NOW() WHERE id = #{id}")
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);
}
