package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.ProjectsVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.service.ProjectsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/10 19:46
 * @modified By：
 */
@Service
@Slf4j
public class ProjectsServiceImpl implements ProjectsService {

    @Value("${huawei.ManageOne.om_url}")
    private String omUrl;

    @Override
    public List<ProjectsVO> getProjectList(RequestVO requestVO) throws Exception {
        String url = omUrl+"/rest/vdc/v3.1/vdcs/"+requestVO.getVdcId()+"/projects";
        List<ProjectsVO> list = new ArrayList<>();
        Map param = new HashMap();
        param.put("X-Auth-Token",requestVO.getOcToken());
        String result = BaseOkHttpClientUtil.get(url, param);
        JSONObject object = JSON.parseObject(result);
        JSONArray jsonArray = object.getJSONArray("projects");//获取数组
        for (int i = 0; i <jsonArray.size() ; i++) {
            ProjectsVO projectsVO = new ProjectsVO();
            projectsVO.setId(jsonArray.getJSONObject(i).getString("id"));
            projectsVO.setName(jsonArray.getJSONObject(i).getString("name"));
            projectsVO.setDescription(jsonArray.getJSONObject(i).getString("description"));
            projectsVO.setDomainId(jsonArray.getJSONObject(i).getString("domain_id"));
            projectsVO.setEnabled(jsonArray.getJSONObject(i).getBoolean("enabled"));
            projectsVO.setTenantId(jsonArray.getJSONObject(i).getString("tenant_id"));
            projectsVO.setShared(jsonArray.getJSONObject(i).getBoolean("is_shared"));
            projectsVO.setTenantName(jsonArray.getJSONObject(i).getString("tenant_name"));
            projectsVO.setCreateUserId(jsonArray.getJSONObject(i).getString("create_user_id"));
            projectsVO.setCreateUserName(jsonArray.getJSONObject(i).getString("create_user_name"));
            //获取regions数组
            String regions = jsonArray.getJSONObject(i).getString("regions");
            JSONArray jsonArray1 = JSONObject.parseArray(regions);
            List<Object> list1 = new ArrayList<>();
            for (int j = 0; j <jsonArray1.size() ; j++) {
                list1.add(jsonArray1.getJSONObject(j).getString("region_id"));
                list1.add(jsonArray1.getJSONObject(j).getString("region_type"));
                list1.add(jsonArray1.getJSONObject(j).getString("region_status"));
                String regionName = jsonArray1.getJSONObject(j).getString("region_name");
                JSONObject regionNameJson = JSONObject.parseObject(regionName);
                list1.add(regionNameJson.get("zh_cn"));
                list1.add(regionNameJson.get("en_us"));
            }
            projectsVO.setRegions(list1);
            System.out.println(jsonArray1);
            list.add(projectsVO);
        }
        return list;
    }
}
