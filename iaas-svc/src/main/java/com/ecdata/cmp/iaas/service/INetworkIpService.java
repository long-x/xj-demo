package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.NetworkIp;
import com.ecdata.cmp.iaas.entity.dto.NetworkIpVO;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-08-08
*/
public interface INetworkIpService extends IService<NetworkIp> {
    /**
     * 修改更新记录
     * @param id            用户id
     * @param updateUser    更新用户id
     */
    void modifyUpdateRecord(Long id, Long updateUser);

    /**
     * @Param: type类型(1:待定;2:虚拟机;3:网关;)  userd是否被占用(0:可用;1:占用;)
     * @Description:
     * @Date: 2019/12/2 10:51
     */
    List<NetworkIpVO> getIpListBySegmentId(Long segmentId, Integer type, Integer used);

    /**
     * 批量增加ip
     * @param segmentId     网段id
     * @param startIpValue  开始ip值
     * @param endIpValue    结束ip值
     * @param gatewayValue  网关值
     */
    void addIpBatch(Long segmentId, long startIpValue, long endIpValue, long gatewayValue);

    /**
     * 批量增加ip
     * @param segmentId     网段id
     * @param networkIpList ip列表
     */
    void addIpBatch(Long segmentId, List<NetworkIp> networkIpList);
}
