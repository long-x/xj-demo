package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.SubnetsVO;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.huawei.service.SubnetsService;
import com.ecdata.cmp.iaas.client.VirtualDataCenterClient;
import com.ecdata.cmp.iaas.entity.dto.response.VirtualDataCenterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/4 15:33
 * @modified By：
 */
@Service
@Slf4j
public class SubnetsServiceImpl implements SubnetsService {

    @Value("${huawei.ManageOne.vpc_url}")
    private String vcpUrl;


    @Autowired
    private ManageOneService manageOneService;


    @Autowired
    private VirtualDataCenterClient virtualDataCenterClient;


    @Override
    public List<SubnetsVO> getSubnets(String tokenId,String tenantId) throws IOException {
        List<SubnetsVO> list = new ArrayList<>();
        //读取本地文件
        String path = "/json/subnets.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取子网文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
        }

        JSONArray jsonArray = object.getJSONArray("subnets");//获取数组
        for(int i=0;i<jsonArray.size();i++){
            SubnetsVO subnetsVO = new SubnetsVO();
            subnetsVO.setId(jsonArray.getJSONObject(i).getString("id"));
            subnetsVO.setName(jsonArray.getJSONObject(i).getString("name"));
            subnetsVO.setCidr(jsonArray.getJSONObject(i).getString("cidr"));
            subnetsVO.setDnsList(jsonArray.getJSONObject(i).getString("dnsList"));
            subnetsVO.setStatus(jsonArray.getJSONObject(i).getString("status"));
            subnetsVO.setExternal(Boolean.getBoolean(jsonArray.getJSONObject(i).getString("external")));
            subnetsVO.setRouted(Boolean.getBoolean(jsonArray.getJSONObject(i).getString("routed")));
            subnetsVO.setVpcId(jsonArray.getJSONObject(i).getString("vpc_id"));
            subnetsVO.setProjectId(jsonArray.getJSONObject(i).getString("project_id"));
            subnetsVO.setGatewayIp(jsonArray.getJSONObject(i).getString("gateway_ip"));
            subnetsVO.setDhcpEnable(Boolean.getBoolean(jsonArray.getJSONObject(i).getString("dhcp_enable")));
            subnetsVO.setPrimaryDns(jsonArray.getJSONObject(i).getString("primary_dns"));
            subnetsVO.setSecondaryDns(jsonArray.getJSONObject(i).getString("secondary_dns"));
            //subnetsVO.setHostRoutes();
            String poolList = jsonArray.getJSONObject(i).getString("allocation_pools");
            List<Map<String,Object>> listm = new ArrayList<>();
            Map<String,Object> map = new HashMap();
            poolList=poolList.substring(1,poolList.lastIndexOf("]"));
            JSONObject jsonObject = JSONObject.parseObject(poolList);
            map.put("start",jsonObject.get("start"));
            map.put("end",jsonObject.get("end"));
            listm.add(map);
            subnetsVO.setAllocation_pools(listm);
            subnetsVO.setSegmentation_id(jsonArray.getJSONObject(i).getString("segmentation_id"));
            subnetsVO.setNeutron_subnet_id(jsonArray.getJSONObject(i).getString("neutron_subnet_id"));
            subnetsVO.setIp_version(jsonArray.getJSONObject(i).getString("ip_version"));
            subnetsVO.setNeutron_network_id(jsonArray.getJSONObject(i).getString("neutron_network_id"));
            list.add(subnetsVO);
        }

        return list;
    }


    @Override
    public List<SubnetsVO> getSubnetsByvpc(String id) throws IOException {

        List<SubnetsVO> list = new ArrayList<>();

        //读取本地文件
        String path = "/json/subnets.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取子网文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
        }

        JSONArray jsonArray = object.getJSONArray("subnets");//获取数组
        for(int i=0;i<jsonArray.size();i++){
            SubnetsVO subnetsVO = new SubnetsVO();
            subnetsVO.setId(jsonArray.getJSONObject(i).getString("id"));
            subnetsVO.setName(jsonArray.getJSONObject(i).getString("name"));
            subnetsVO.setCidr(jsonArray.getJSONObject(i).getString("cidr"));
            subnetsVO.setDnsList(jsonArray.getJSONObject(i).getString("dnsList"));
            subnetsVO.setStatus(jsonArray.getJSONObject(i).getString("status"));
            subnetsVO.setExternal(Boolean.getBoolean(jsonArray.getJSONObject(i).getString("external")));
            subnetsVO.setRouted(Boolean.getBoolean(jsonArray.getJSONObject(i).getString("routed")));
            subnetsVO.setVpcId(jsonArray.getJSONObject(i).getString("vpc_id"));
            subnetsVO.setProjectId(jsonArray.getJSONObject(i).getString("project_id"));
            subnetsVO.setGatewayIp(jsonArray.getJSONObject(i).getString("gateway_ip"));
            subnetsVO.setDhcpEnable(Boolean.getBoolean(jsonArray.getJSONObject(i).getString("dhcp_enable")));
            subnetsVO.setPrimaryDns(jsonArray.getJSONObject(i).getString("primary_dns"));
            subnetsVO.setSecondaryDns(jsonArray.getJSONObject(i).getString("secondary_dns"));
            //subnetsVO.setHostRoutes();
            String poolList = jsonArray.getJSONObject(i).getString("allocation_pools");
            List<Map<String,Object>> listm = new ArrayList<>();
            Map<String,Object> map = new HashMap();
            poolList=poolList.substring(1,poolList.lastIndexOf("]"));
            JSONObject jsonObject = JSONObject.parseObject(poolList);
            map.put("start",jsonObject.get("start"));
            map.put("end",jsonObject.get("end"));
            listm.add(map);
            subnetsVO.setAllocation_pools(listm);
            subnetsVO.setSegmentation_id(jsonArray.getJSONObject(i).getString("segmentation_id"));
            subnetsVO.setNeutron_subnet_id(jsonArray.getJSONObject(i).getString("neutron_subnet_id"));
            subnetsVO.setIp_version(jsonArray.getJSONObject(i).getString("ip_version"));
            subnetsVO.setNeutron_network_id(jsonArray.getJSONObject(i).getString("neutron_network_id"));
            list.add(subnetsVO);
        }

        return list;

    }


}
