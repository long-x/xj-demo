package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.huawei.dto.vo.EipVO;
import com.ecdata.cmp.huawei.service.EipService;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.iaas.client.VirtualDataCenterClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/10/19 16:32
 * @modified By：
 */
@Service
@Slf4j
public class EipServiceImpl implements EipService {



    @Override
    public List<EipVO> findEip(String id) throws IOException {

        List<EipVO> list = new ArrayList<>();


        //读取本地文件
        String path = "/json/publicips.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取eip文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
        }

        log.info("Eip列表{}", object);
        JSONArray jsonArray = object.getJSONArray("publicips");//获取数组
        for (int i = 0; i < jsonArray.size(); i++) {
            String status = jsonArray.getJSONObject(i).getString("status");
            if ("DOWN".equals(status)) {
                EipVO vo = new EipVO();
                vo.setId(jsonArray.getJSONObject(i).getString("id"));
                vo.setType(jsonArray.getJSONObject(i).getString("type"));
                vo.setExternal_net_id(jsonArray.getJSONObject(i).getString("external_net_id"));
                vo.setPublic_ip_address(jsonArray.getJSONObject(i).getString("public_ip_address"));
                vo.setPrivate_ip_address(jsonArray.getJSONObject(i).getString("private_ip_address"));
                vo.setStatus(jsonArray.getJSONObject(i).getString("status"));
                vo.setTenant_id(jsonArray.getJSONObject(i).getString("tenant_id"));
                vo.setProject_id(jsonArray.getJSONObject(i).getString("project_id"));
                list.add(vo);
            }
        }
        return list;


    }


}
