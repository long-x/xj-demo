package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.RegionsVO;
import com.ecdata.cmp.huawei.service.RegionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/9 11:12
 * @modified By：
 */
@Service
@Slf4j
public class RegionsServiceImpl implements RegionsService {

    @Value("${huawei.ManageOne.mo_url}")
    private String moUrl;

    @Override
    public List<RegionsVO> gitRegionsList(Map param) throws Exception {
        String url = moUrl +"/v3/regions";
        List<RegionsVO> list = new ArrayList<>();
        String result = BaseOkHttpClientUtil.get(url, param);
        JSONObject object = JSON.parseObject(result);
        JSONArray jsonArray = object.getJSONArray("regions");//获取数组
        for(int i=0;i<jsonArray.size();i++){
            RegionsVO regionsVO = new RegionsVO();
            regionsVO.setId(jsonArray.getJSONObject(i).getString("id"));
            regionsVO.setParentRegionId(jsonArray.getJSONObject(i).getString("parent_region_id"));
            regionsVO.setDescription(jsonArray.getJSONObject(i).getString("description"));
            //区域的链接
            String link = jsonArray.getJSONObject(i).getString("links");
            JSONObject linkJson = JSONObject.parseObject(link);
            Map<String,Object> linkMap = new HashMap();
            linkMap.put("self",linkJson.get("self"));
            linkMap.put("previous",linkJson.get("previous"));
            linkMap.put("next",linkJson.get("next"));
            regionsVO.setLink(linkMap);
            //区域名
            String locales = jsonArray.getJSONObject(i).getString("locales");
            JSONObject localesJson = JSONObject.parseObject(locales);
            Map<String,Object> localesMap = new HashMap();
            localesMap.put("zh-cn",localesJson.get("zh-cn"));
            localesMap.put("en-us",localesJson.get("en-us"));
            localesMap.put("es-es",localesJson.get("es-es"));
            localesMap.put("pt-br",localesJson.get("pt-br"));
            regionsVO.setLocales(localesMap);
            //cloud_infras
            String cloudInfras = jsonArray.getJSONObject(i).getString("cloud_infras");
            List<String> cloudInfrasList=JSON.parseArray(cloudInfras, String.class);
            regionsVO.setCloudInfras(cloudInfrasList);
            list.add(regionsVO);
        }
        return list;
    }
}
