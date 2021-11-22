package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.SubnetsVO;

import java.io.IOException;
import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/4 15:30
 * @modified By：
 */
public interface SubnetsService {

    /**
     * 查询子网列表
     * @param tenantId 项目id
     * @param tokenId  tokenId
     * @return
     * @throws IOException
     */
    List<SubnetsVO> getSubnets(String tokenId,String tenantId) throws IOException;


    /**
     * 根据vpcId获取子网列表
     * @param id
     * @return
     * @throws IOException
     */
    List<SubnetsVO>getSubnetsByvpc(String id)throws IOException;


}
