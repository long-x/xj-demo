package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.SecurityGroupsVO;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/4 17:16
 * @modified By：
 */
public interface SecurityGroupsService {

    /**
     * 查询安全组列表
     * @param map
     * @return
     */
    List<SecurityGroupsVO> getSecurityGroupsList(Map map) throws Exception;


    /**
     * 前端-查询安全组列表
     * @param id
     * @return
     */
    List<SecurityGroupsVO> getSecurityGroupsList(String id) throws Exception;



}
