package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.NetworkSegment;
import com.ecdata.cmp.iaas.entity.dto.NetworkSegmentVO;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-08-08
*/
public interface INetworkSegmentService extends IService<NetworkSegment> {

    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * 通过ip查询网段信息
     * @param ip    ip地址
     * @return  网段
     */
    NetworkSegment getSegmentByIp(String ip);

    List<NetworkSegmentVO> getRelationSegmentList(Long clusterId, Long segmentId);

    IPage<NetworkSegmentVO> getRelationSegmentList(Page page, Long clusterId, Long segmentId);
}
