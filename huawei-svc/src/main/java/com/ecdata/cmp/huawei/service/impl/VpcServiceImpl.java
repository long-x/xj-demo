package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.token.TokenVdcVO;
import com.ecdata.cmp.huawei.dto.vo.VpcVO;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.huawei.service.VpcService;
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
 * @date ：Created in 2020/3/26 22:01
 * @modified By：
 */
@Service
@Slf4j
public class VpcServiceImpl  implements VpcService {

    @Value("${huawei.ManageOne.vpc_url}")
    private String vcpUrl;

    @Autowired
    private ManageOneService manageOneService;


    @Autowired
    private VirtualDataCenterClient virtualDataCenterClient;



    /**
     * 获取vpc列表
     * @param id
     * @return
     */
    @Override
    public List<VpcVO> getVpcList(String id) throws IOException {

        List<VpcVO> list = new ArrayList<>();
        //读取本地文件
        String path = "/json/vpc.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取vpc文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
        }

        log.info("vpc列表{}",object);
        JSONArray jsonArray = object.getJSONArray("vpcs");//获取数组
        for(int i=0;i<jsonArray.size();i++){
            VpcVO vpcVO = new VpcVO();

            vpcVO.setId(jsonArray.getJSONObject(i).getString("id"));
            vpcVO.setName(jsonArray.getJSONObject(i).getString("name"));
            vpcVO.setStatus(jsonArray.getJSONObject(i).getString("status"));
            vpcVO.setExpiry(jsonArray.getJSONObject(i).getString("expiry"));
            vpcVO.setOp_status(jsonArray.getJSONObject(i).getString("op_status"));
            vpcVO.setProject_id(jsonArray.getJSONObject(i).getString("project_id"));

            String external_gateway_info = jsonArray.getJSONObject(i).getString("external_gateway_info");

            JSONObject parseObject = JSON.parseObject(external_gateway_info);
            vpcVO.setNetwork_id(parseObject.get("network_id").toString());
            vpcVO.setEnable_snat(parseObject.get("enable_snat").toString());

            list.add(vpcVO);
        }

        return list;

    }


}
