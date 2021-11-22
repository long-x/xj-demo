package com.ecdata.cmp.iaas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.iaas.entity.NetworkSegment;
import com.ecdata.cmp.iaas.entity.dto.NetworkSegmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-08-08
*/
@Mapper
@Repository
public interface NetworkSegmentMapper extends BaseMapper<NetworkSegment> {

    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    @Update(" update iaas_network_segment set update_user = #{updateUser}, update_time = NOW() WHERE id = #{id}")
    void modifyUpdateRecord(@Param("id") Long id, @Param("updateUser") Long updateUser);

    /**
     * 通过ip查询网段信息
     * @param ip    ip地址
     * @return  网段
     */
    @Select("SELECT s.* FROM iaas_network_segment s INNER JOIN iaas_network_ip i ON i.segment_id = s.id WHERE i.ip_address = #{ip}")
    NetworkSegment getSegmentByIp(String ip);

    List<NetworkSegmentVO> getRelationSegmentList(@Param("clusterId") Long clusterId, @Param("segmentId") Long segmentId);

    IPage<NetworkSegmentVO> getRelationSegmentList(Page page,@Param("clusterId") Long clusterId, @Param("segmentId") Long segmentId);
}
