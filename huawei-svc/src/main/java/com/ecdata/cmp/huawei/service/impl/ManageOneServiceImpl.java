package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZoneStatistics;
import com.ecdata.cmp.huawei.dto.region.ActualCapacity;
import com.ecdata.cmp.huawei.dto.region.RegionInfo;
import com.ecdata.cmp.huawei.dto.token.*;
import com.ecdata.cmp.huawei.dto.vm.ObjectTypeBody;
import com.ecdata.cmp.huawei.dto.vm.SimpleIndicator;
import com.ecdata.cmp.huawei.dto.vo.CiInstanceVO;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author ty
 * @description
 * @date 2019/11/20 16:19
 */
@Service
@Slf4j
public class ManageOneServiceImpl implements ManageOneService {

    public static final String mediaType = "application/json";
    /**
     * json映射
     */
    private ObjectMapper jsonMapper = new ObjectMapper();

    @Value("${huawei.ManageOne.mo_url}")
    private String moUrl;

    @Value("${huawei.ManageOne.om_url}")
    private String omUrl;

    @Value("${huawei.ManageOne.web_om_url}")
    private String omUrlWeb;

    @Override
    public String getTokenInfo(TokenInfoDTO tokenInfoDTO,String ipAddress) throws IOException {
        String url =  "https://"+ipAddress+ "/goku/rest/v2.0/token";
        String tokenInfoDTOStr = jsonMapper.writeValueAsString(tokenInfoDTO);
        String result = BaseOkHttpClientUtil.post(url, tokenInfoDTOStr, new HashMap<>(), mediaType);
        Map<String,Map<String,String>> map = jsonMapper.readValue(result, Map.class);
        Map<String, String> jsonMap = map.get("token");
        return jsonMap.get("tokenId");
    }


    @Override
    public OMToken getOMToken(String userName, String value) throws IOException {
        String url =  omUrl + "/rest/plat/smapp/v1/oauth/token";
        TokenInfoDTO tokenInfoDTO = TokenInfoDTO.builder().
                userName(userName).
                value(value).
                grantType("password").
                build();
        String tokenInfoDTOStr = jsonMapper.writeValueAsString(tokenInfoDTO);
        String result = BaseOkHttpClientUtil.put(url, tokenInfoDTOStr, new HashMap<>(), mediaType);
        return jsonMapper.readValue(result, OMToken.class);
    }

    @Override
    public String getOCToken(String userName, String value, String domainName, String projectId) throws IOException {
        String url =  moUrl+ "/v3/auth/tokens";
        Auth auth = new Auth();
        Identity identity = new Identity();
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("password"));
        identity.setMethods(strings);
        Domain domain1 = new Domain(domainName);
        User user1 = User.builder().
                domain(domain1).
                name(userName).
                password(value).build();
        Password password = new Password();
        password.setUser(user1);
        identity.setPassword(password);
        auth.setIdentity(identity);
        //scope
        Project project = new Project();
        Scope scope1 = new Scope();
        if (StringUtils.hasText(projectId)){
            project.setId(projectId);
            scope1.setProject(project);
        }
        auth.setScope(scope1);
        scope1.setDomain(domain1);
        TokenRequestDTO tokenRequestDTO = new TokenRequestDTO();
        tokenRequestDTO.setAuth(auth);
        String tokenRequestDTOStr = jsonMapper.writeValueAsString(tokenRequestDTO);
        Response response = BaseOkHttpClientUtil.post(url, tokenRequestDTOStr, mediaType);
        return response.header("X-Subject-Token");
    }

    @Override
    public OMToken getOMTokenWeb(String userName, String value) throws IOException {
        String url =  omUrlWeb + "/rest/plat/smapp/v1/oauth/token";
        TokenInfoDTO tokenInfoDTO = TokenInfoDTO.builder().
                userName(userName).
                value(value).
                grantType("password").
                build();
        String tokenInfoDTOStr = jsonMapper.writeValueAsString(tokenInfoDTO);
        String result = BaseOkHttpClientUtil.put(url, tokenInfoDTOStr, new HashMap<>(), mediaType);
        return jsonMapper.readValue(result, OMToken.class);
    }

    @Override
    public List<CiInstanceVO> getInstanceDataByType(String token, String nativeId, String projectId, String type) throws IOException {
        String resId="";
        String url =  omUrl+ "/rest/tenant-resource/v1/instances/"+ type;
        String requestUrl=url+"?condition={\"constraint\":[{\"simple\":{\"name\":\"projectId\",\"value\":\""+projectId+"\"}}]}";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String instanceData = BaseOkHttpClientUtil.get(requestUrl, headerMap);
        // 字符串转换为JSON
        JSONObject jsonObject = JSON.parseObject(instanceData);
        String tempObj = jsonObject.getString("objList");
        return JSON.parseArray(tempObj, CiInstanceVO.class);
        /*List<Map<String,String>> listObjectFir = (List<Map<String,String>>) JSONArray.parse(tempObj);
        if(!CollectionUtils.isEmpty(listObjectFir)){
            Map<String, String> map = listObjectFir.get(0);
            resId = map.get("resId");
        }
        return resId;*/
    }

    @Override
    public String getMonitorObjs(String token) throws IOException {
        String objTypeId="";
        String url =  omUrl+ "/rest/performance/v1/mgr-svc/obj-types";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String instanceData = BaseOkHttpClientUtil.get(url, headerMap);
        // 字符串转换为JSON
        JSONObject jsonObject = JSON.parseObject(instanceData);
        String tempObj = jsonObject.getString("data");

        List<ObjectTypeBody> objList = JSONArray.parseArray(tempObj, ObjectTypeBody.class);
        if(!CollectionUtils.isEmpty(objList)){
            for (int i = 0; i < objList.size(); i++) {
                if ("CLOUD_VM".equals(objList.get(i).getResource_category())){
                    objTypeId=objList.get(i).getObj_type_id();
                }
            }
        }
        return objTypeId;
    }

    @Override
    public List<String> getTargetsByObjType(String token, String objTypeId) throws IOException {
        List<String> list=new ArrayList<String>();
        String url =  omUrl+ "/rest/performance/v1/mgr-svc/obj-types/"+objTypeId+"/indicators";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String instanceData = BaseOkHttpClientUtil.get(url, headerMap);
        // 字符串转换为JSON
        JSONObject jsonObject = JSON.parseObject(instanceData);
        String tempObj = jsonObject.getString("data");
       Map<String,List<String>> listObjectFir = (Map<String,List<String>>) JSONArray.parse(tempObj);
       if(!CollectionUtils.isEmpty(listObjectFir)){
           list = listObjectFir.get("indicator_ids");
       }
        return list;
    }

    @Override
    public AvailableZoneStatistics getRegionCapacity(String token, String regionId, String resourceType) throws IOException {

        return null;
    }

    @Override
    public Map<Object, SimpleIndicator> getMonitorQuotaById(String token, List<Double> indicators) throws IOException {
        String url =  omUrl + "/rest/performance/v1/mgr-svc/indicators";
        String indicatorsStr = jsonMapper.writeValueAsString(indicators);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String result = BaseOkHttpClientUtil.post(url, indicatorsStr, headerMap, mediaType);
        JSONObject jsonObject = JSON.parseObject(result);
        String tempObj = jsonObject.getString("data");
        return jsonMapper.readValue(tempObj, Map.class);
    }

    @Override
    public RegionInfo getRegion(String token) throws IOException {
        String url =  moUrl+ "/v3/regions";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String regionData = BaseOkHttpClientUtil.get(url, headerMap);
        return jsonMapper.readValue(regionData,RegionInfo.class);
    }

    @Override
    public String getVdcs(String token) throws IOException {
        String url =  moUrl+ "/rest/vdc/v3.0/vdcs";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        return BaseOkHttpClientUtil.get(url, headerMap);
    }

    @Override
    public String getProjectByVdcId(String token, String vdcId) throws IOException {
        String url =  moUrl+ "/rest/vdc/v3.1/vdcs/"+vdcId+"/projects";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        return BaseOkHttpClientUtil.get(url, headerMap);
    }

    @Override
    public ActualCapacity getRegoinCapacity(String token, String regionId, String type,String dataKey) throws IOException {
        String url =  omUrl+ "/rest/capacity/v1/capbase/regions/"+ regionId+"/resource-types/"+type+"/current-capacities";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String instanceData = BaseOkHttpClientUtil.get(url, headerMap);
        JSONObject jsonObject = JSON.parseObject(instanceData);
        String tempObj = jsonObject.getString(dataKey);
        JSONObject cpuInfo = JSON.parseObject(tempObj);
        String actualCapacity = cpuInfo.getString("actualCapacity");
//        return jsonMapper.readValue(actualCapacity, ActualCapacity.class);
         return JSONObject.parseObject(actualCapacity, ActualCapacity.class);
    }

    @Override
    public Map<String, ActualCapacity> getRegoinAllCapacity(String token, String regionId) throws IOException {
        HashMap<String, ActualCapacity> regoinAllCapacity = new HashMap<>();
        ActualCapacity cpuCapacity = getRegoinCapacity(token, regionId, ManageOneConstant.CPU, ManageOneConstant.CPU);
        regoinAllCapacity.put("cpu",cpuCapacity);
        ActualCapacity memoryCapacity = getRegoinCapacity(token, regionId, ManageOneConstant.MEMORY, ManageOneConstant.MEMORY);
        regoinAllCapacity.put("memory",memoryCapacity);
        ActualCapacity storagePoolCapacity = getRegoinCapacity(token, regionId, ManageOneConstant.STORAGEPOOL_IN, ManageOneConstant.STORAGEPOOL_OUT);
        regoinAllCapacity.put("storage-pool",storagePoolCapacity);
        return regoinAllCapacity;
    }
}
