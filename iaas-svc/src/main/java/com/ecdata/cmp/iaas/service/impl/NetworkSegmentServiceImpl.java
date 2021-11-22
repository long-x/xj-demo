package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.NetworkSegment;
import com.ecdata.cmp.iaas.entity.dto.NetworkSegmentVO;
import com.ecdata.cmp.iaas.mapper.NetworkSegmentMapper;
import com.ecdata.cmp.iaas.service.INetworkSegmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-08-08
*/
@Service
public class NetworkSegmentServiceImpl extends ServiceImpl<NetworkSegmentMapper, NetworkSegment> implements INetworkSegmentService {

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public NetworkSegment getSegmentByIp(String ip) {
        return baseMapper.getSegmentByIp(ip);
    }

    @Override
    public List<NetworkSegmentVO> getRelationSegmentList(Long clusterId, Long segmentId){
        return baseMapper.getRelationSegmentList(clusterId,segmentId);
    }

    @Override
    public IPage<NetworkSegmentVO> getRelationSegmentList(Page page, Long clusterId, Long segmentId) {
        return baseMapper.getRelationSegmentList(page, clusterId,segmentId);
    }

}
