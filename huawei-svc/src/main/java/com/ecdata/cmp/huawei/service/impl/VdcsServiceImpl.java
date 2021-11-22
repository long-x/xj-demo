package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.VdcsVO;
import com.ecdata.cmp.huawei.service.VdcsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/9 16:16
 * @modified By：
 */
@Service
@Slf4j
public class VdcsServiceImpl implements VdcsService {

    @Value("${huawei.ManageOne.om_url}")
    private String omUrl;

    @Override
    public List<VdcsVO> getVdcsList(Map param) throws Exception {
        String url = omUrl +"/rest/vdc/v3.0/vdcs";
        List<VdcsVO> list = new ArrayList<>();
        String result = BaseOkHttpClientUtil.get(url, param);
        JSONObject object = JSON.parseObject(result);
        JSONArray jsonArray = object.getJSONArray("vdcs");//获取数组
        for(int i=0;i<jsonArray.size();i++){
            VdcsVO vdcsVO = new VdcsVO();
            vdcsVO.setId(jsonArray.getJSONObject(i).getString("id"));
            vdcsVO.setName(jsonArray.getJSONObject(i).getString("name"));
            vdcsVO.setTag(jsonArray.getJSONObject(i).getString("tag"));
            vdcsVO.setDescription(jsonArray.getJSONObject(i).getString("description"));
            vdcsVO.setUpperVdcId(jsonArray.getJSONObject(i).getString("upper_vdc_id"));
            String enabled = jsonArray.getJSONObject(i).getString("enabled");
            vdcsVO.setEnabled(Boolean.valueOf(enabled));
            vdcsVO.setDomainId(jsonArray.getJSONObject(i).getString("domain_id"));
            vdcsVO.setLevel(jsonArray.getJSONObject(i).getString("level"));
            vdcsVO.setCreateUserId(jsonArray.getJSONObject(i).getString("create_user_id"));
            vdcsVO.setCreateUserName(jsonArray.getJSONObject(i).getString("create_user_name"));
            vdcsVO.setCreateAt(jsonArray.getJSONObject(i).getString("create_at"));
            vdcsVO.setDomainName(jsonArray.getJSONObject(i).getString("domain_name"));
            vdcsVO.setThirdId(jsonArray.getJSONObject(i).getString("third_id"));
            vdcsVO.setIdpName(jsonArray.getJSONObject(i).getString("idp_name"));
            vdcsVO.setRegionId(jsonArray.getJSONObject(i).getString("region_id"));
            vdcsVO.setEnterpriseId(jsonArray.getJSONObject(i).getString("enterprise_id"));
            vdcsVO.setAzId(jsonArray.getJSONObject(i).getString("az_id"));
            vdcsVO.setEnterpriseProjectId(jsonArray.getJSONObject(i).getString("enterprise_project_id"));
            vdcsVO.setThirdType(jsonArray.getJSONObject(i).getString("third_type"));
            list.add(vdcsVO);
        }
        return list;
    }
}
