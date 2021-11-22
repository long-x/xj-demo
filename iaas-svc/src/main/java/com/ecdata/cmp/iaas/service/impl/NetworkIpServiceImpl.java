package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.common.constant.Constants;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.NetworkUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.NetworkIp;
import com.ecdata.cmp.iaas.entity.dto.NetworkIpVO;
import com.ecdata.cmp.iaas.mapper.NetworkIpMapper;
import com.ecdata.cmp.iaas.service.INetworkIpService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-08-08
*/
@Service
public class NetworkIpServiceImpl extends ServiceImpl<NetworkIpMapper, NetworkIp> implements INetworkIpService {
    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id, updateUser);
    }

    @Override
    public void addIpBatch(Long segmentId, long startIpValue, long endIpValue, long gatewayValue) {
        List<NetworkIp> networkIpList = new ArrayList<>();
        long end = endIpValue-startIpValue;
        if(end > 255){
            endIpValue = startIpValue + 254;
        }else if(end <0){
            return;
        }
        for (long ipValue = startIpValue; ipValue < endIpValue; ipValue++) {
            NetworkIp networkIp = new NetworkIp();
            networkIp.setId(SnowFlakeIdGenerator.getInstance().nextId());
            networkIp.setSegmentId(segmentId);
            networkIp.setIpAddress(NetworkUtil.valueConvertIp(ipValue));
            networkIp.setIpValue(ipValue);
            if (ipValue == gatewayValue) {
                networkIp.setType(Constants.THREE);
                networkIp.setUsed(1);
            }
            networkIp.setCreateUser(Sign.getUserId());
            networkIpList.add(networkIp);

        }
        final int batchSize = 500;
        this.saveBatch(networkIpList, batchSize);
    }

    @Override
    public void addIpBatch(Long segmentId, List<NetworkIp> networkIpList) {
        for (NetworkIp networkIp : networkIpList) {
            networkIp.setSegmentId(segmentId);
            String ipAddress = networkIp.getIpAddress();
            networkIp.setIpValue(NetworkUtil.ipConvertValue(ipAddress));
            networkIp.setCreateUser(Sign.getUserId());
        }
        final int batchSize = 500;
        this.saveBatch(networkIpList, batchSize);
    }

    @Override
    public List<NetworkIpVO> getIpListBySegmentId(Long segmentId, Integer type, Integer used){
        LambdaQueryWrapper<NetworkIp> queryWrapper = new LambdaQueryWrapper<>();
        if (segmentId != null) {
            queryWrapper.eq(NetworkIp::getSegmentId, segmentId);
        }
        if (type != null) {
            queryWrapper.eq(NetworkIp::getType, type);
        }
        if (used != null) {
            queryWrapper.eq(NetworkIp::getUsed, used);
        }
        queryWrapper.orderByAsc(NetworkIp::getIpValue);
        List<NetworkIp> result = baseMapper.selectList(queryWrapper);
        List<NetworkIpVO> workIpList = new ArrayList<>();

        if (result != null && result.size() > 0) {
            for (NetworkIp workIp : result) {

                NetworkIpVO networkIpVO = new NetworkIpVO();
                BeanUtils.copyProperties(workIp, networkIpVO);
                workIpList.add(networkIpVO);
            }
        }
        return  workIpList;
    }
}
