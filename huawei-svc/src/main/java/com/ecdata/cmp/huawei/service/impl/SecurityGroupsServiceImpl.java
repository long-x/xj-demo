package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.SecurityGroupRulesVO;
import com.ecdata.cmp.huawei.dto.vo.SecurityGroupsVO;
import com.ecdata.cmp.huawei.dto.vo.VpcVO;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.huawei.service.SecurityGroupsService;
import com.ecdata.cmp.iaas.client.VirtualDataCenterClient;
import com.ecdata.cmp.iaas.entity.dto.response.VirtualDataCenterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/4 17:17
 * @modified By：
 */
@Service
@Slf4j
public class SecurityGroupsServiceImpl implements SecurityGroupsService {

    @Value("${huawei.ManageOne.vpc_url}")
    private String vcpUrl;

    @Autowired
    private ManageOneService manageOneService;

    @Autowired
    private VirtualDataCenterClient virtualDataCenterClient;


    /**
     * 查询安全组列表
     * @param param
     * @return
     */
    @Override
    public List<SecurityGroupsVO> getSecurityGroupsList(Map param) throws Exception{

        List<SecurityGroupsVO> list = new ArrayList<>();

        //读取本地文件
        String path = "/json/security_groups.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取安全组文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
        }

        JSONArray jsonArray = object.getJSONArray("security_groups");//获取数组
        for (int i = 0; i < jsonArray.size(); i++) {
            SecurityGroupsVO groupsVO = new SecurityGroupsVO();
            groupsVO.setId(jsonArray.getJSONObject(i).getString("id"));
            groupsVO.setName(jsonArray.getJSONObject(i).getString("name"));
            groupsVO.setDescription(jsonArray.getJSONObject(i).getString("description"));
            groupsVO.setTenantId(jsonArray.getJSONObject(i).getString("tenant_id"));
            groupsVO.setCreatedAt(jsonArray.getJSONObject(i).getString("created_at"));
            groupsVO.setUpdatedAt(jsonArray.getJSONObject(i).getString("updated_at"));
            groupsVO.setProjectId(jsonArray.getJSONObject(i).getString("project_id"));
            //获取security_group_rules数组
            String rules = jsonArray.getJSONObject(i).getString("security_group_rules");
            JSONArray jsonArray1 = JSONObject.parseArray(rules);
            List<SecurityGroupRulesVO> rulesVOList = new ArrayList<>();
            for (int j = 0; j < jsonArray1.size();j++) {
                SecurityGroupRulesVO groupRulesVO = new SecurityGroupRulesVO();
                groupRulesVO.setId(jsonArray1.getJSONObject(i).getString("id"));
                groupRulesVO.setPriority(jsonArray1.getJSONObject(i).getString("priority"));
                groupRulesVO.setAction(jsonArray1.getJSONObject(i).getString("action"));
                groupRulesVO.setDirection(jsonArray1.getJSONObject(i).getString("direction"));
                groupRulesVO.setProtocol(jsonArray1.getJSONObject(i).getString("protocol"));
                groupRulesVO.setEthertype(jsonArray1.getJSONObject(i).getString("ethertype"));
                groupRulesVO.setDirection(jsonArray1.getJSONObject(i).getString("description"));
                groupRulesVO.setRemoteGroupId(jsonArray1.getJSONObject(i).getString("remote_group_id"));
                groupRulesVO.setRemoteIpPrefix(jsonArray1.getJSONObject(i).getString("remote_ip_prefix"));
                groupRulesVO.setTenantId(jsonArray1.getJSONObject(i).getString("tenant_id"));
                groupRulesVO.setPortRangeMax(jsonArray1.getJSONObject(i).getString("port_range_max"));
                groupRulesVO.setPortRangeMin(jsonArray1.getJSONObject(i).getString("port_range_min"));
                groupRulesVO.setSecurityGroupId(jsonArray1.getJSONObject(i).getString("security_group_id"));
                groupRulesVO.setCreatedAt(jsonArray1.getJSONObject(i).getString("created_at"));
                groupRulesVO.setUpdatedAt(jsonArray1.getJSONObject(i).getString("updated_at"));
                groupRulesVO.setProjectId(jsonArray1.getJSONObject(i).getString("project_id"));
                rulesVOList.add(groupRulesVO);
            }
            groupsVO.setSecurityGroupRulesVOS(rulesVOList);
            list.add(groupsVO);
        }
        return list;

    }



    /**
     * 前端-查询安全组列表
     * @param id
     * @return
     */
    @Override
    public List<SecurityGroupsVO> getSecurityGroupsList(String id) throws Exception{

//        String authz = AuthContext.getAuthz();
//        VirtualDataCenterResponse vdc = virtualDataCenterClient.getVdcById(authz, id);
//        String token = manageOneService.getOCToken(vdc.getData().getUsername(), vdc.getData().getPassword(), vdc.getData().getDomainName(), vdc.getData().getProjectKey());
//
//        String url = vcpUrl + "/v2.0/security-groups";
//        Map param = new HashMap();
//        param.put("X-Auth-Token",token);
//
//
        List<SecurityGroupsVO> list = new ArrayList<>();
//        String result = BaseOkHttpClientUtil.get(url, param);


        //读取本地文件
        String path = "/json/security_groups.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取安全组文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
        }

        JSONArray jsonArray = object.getJSONArray("security_groups");//获取数组
        for (int i = 0; i < jsonArray.size(); i++) {
            SecurityGroupsVO groupsVO = new SecurityGroupsVO();
            groupsVO.setId(jsonArray.getJSONObject(i).getString("id"));
            groupsVO.setName(jsonArray.getJSONObject(i).getString("name"));
            groupsVO.setDescription(jsonArray.getJSONObject(i).getString("description"));
            groupsVO.setTenantId(jsonArray.getJSONObject(i).getString("tenant_id"));
            groupsVO.setCreatedAt(jsonArray.getJSONObject(i).getString("created_at"));
            groupsVO.setUpdatedAt(jsonArray.getJSONObject(i).getString("updated_at"));
            groupsVO.setProjectId(jsonArray.getJSONObject(i).getString("project_id"));
            //获取security_group_rules数组
            String rules = jsonArray.getJSONObject(i).getString("security_group_rules");
            JSONArray jsonArray1 = JSONObject.parseArray(rules);
            List<SecurityGroupRulesVO> rulesVOList = new ArrayList<>();
            for (int j = 0; j < jsonArray1.size();j++) {
                SecurityGroupRulesVO groupRulesVO = new SecurityGroupRulesVO();
                groupRulesVO.setId(jsonArray1.getJSONObject(i).getString("id"));
                groupRulesVO.setPriority(jsonArray1.getJSONObject(i).getString("priority"));
                groupRulesVO.setAction(jsonArray1.getJSONObject(i).getString("action"));
                groupRulesVO.setDirection(jsonArray1.getJSONObject(i).getString("direction"));
                groupRulesVO.setProtocol(jsonArray1.getJSONObject(i).getString("protocol"));
                groupRulesVO.setEthertype(jsonArray1.getJSONObject(i).getString("ethertype"));
                groupRulesVO.setDirection(jsonArray1.getJSONObject(i).getString("description"));
                groupRulesVO.setRemoteGroupId(jsonArray1.getJSONObject(i).getString("remote_group_id"));
                groupRulesVO.setRemoteIpPrefix(jsonArray1.getJSONObject(i).getString("remote_ip_prefix"));
                groupRulesVO.setTenantId(jsonArray1.getJSONObject(i).getString("tenant_id"));
                groupRulesVO.setPortRangeMax(jsonArray1.getJSONObject(i).getString("port_range_max"));
                groupRulesVO.setPortRangeMin(jsonArray1.getJSONObject(i).getString("port_range_min"));
                groupRulesVO.setSecurityGroupId(jsonArray1.getJSONObject(i).getString("security_group_id"));
                groupRulesVO.setCreatedAt(jsonArray1.getJSONObject(i).getString("created_at"));
                groupRulesVO.setUpdatedAt(jsonArray1.getJSONObject(i).getString("updated_at"));
                groupRulesVO.setProjectId(jsonArray1.getJSONObject(i).getString("project_id"));
                rulesVOList.add(groupRulesVO);
            }
            groupsVO.setSecurityGroupRulesVOS(rulesVOList);
            list.add(groupsVO);
        }
        return list;

    }




}
