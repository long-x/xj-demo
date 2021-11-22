package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vm.VmFlavors;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineConditionVO;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.huawei.service.VDCVirtualMachineService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VDCVirtualMachineServiceImpl implements VDCVirtualMachineService {

    @Value("${huawei.ManageOne.web_om_url}")
    private String webUmUrl;

    @Value("${huawei.ManageOne.ecs_url}")
    private String ecs_url;

    private static final String UTF8 = "utf-8";


    @Override
    public VirtualMachineVO VMInfo(RequestVO requestVO) throws Exception {
        log.info("listVM, omUrl={}, requestVO={}", webUmUrl, requestVO);
        Assert.isTrue(StringUtils.isNotBlank(requestVO.getOmToken()), "token cannot be blank");
        Assert.isTrue(StringUtils.isNotBlank(requestVO.getResId()), "resId cannot be both blank");
        String url = webUmUrl + "/rest/tenant-resource/v1/cloud-resource/detail";
        JSONArray condition = new JSONArray();
        //根据虚拟机id获取虚拟机详情
        if (StringUtils.isNotBlank(requestVO.getResId())) {
            JSONObject conditionItem = new JSONObject();
            conditionItem.put("propName", "cloud_vm#resId");
            conditionItem.put("propValue", requestVO.getResId());
            conditionItem.put("caseSensitive", "true");
            conditionItem.put("operator", "equal");
            condition.add(conditionItem);
        }
        Map<String, String> params = ImmutableMap.of("type", "cloud_vm",
                "condition", URLEncoder.encode(condition.toJSONString(), UTF8),
                "attrs", URLEncoder.encode("[\"cloud_vm#name\",\"cloud_vm#nativeId\",\"cloud_vm#privateIps\",\"cloud_vm#projectId\",\"cloud_vm#status\",\"cloud_vm#floatingIp\",\"cloud_vm#bizRegionName\",\"cloud_vm#cpuUsage\",\"cloud_vm#memoryUsage\",\"cloud_vm#diskUsage\",\"cloud_vm#flavor\",\"cloud_vm#osVersion\",\"cloud_vm#vdcName\",\"cloud_vm#bizRegionId\",\"cloud_vm#resId\",\"cloud_vm#azoneId\",\"cloud_vm#azoneName\",\"cloud_vm#physicalHostId\",\"cloud_vm#imageName\"]", UTF8));

        String result = getForCookie(url, requestVO.getOmToken(), params);
        log.info("listVM, omUrl={}, requestVO={}, result={}", webUmUrl, requestVO, result);
        JSONObject object = JSON.parseObject(result);
        JSONObject responseData = object.getJSONObject("responseData");
        String basicInfos = responseData.getString("basicInfo");
        JSONObject basicInfo = JSONObject.parseObject(basicInfos);
        VirtualMachineVO machineVO = new VirtualMachineVO();
        machineVO.setImageName(basicInfo.getString("cloud_vm#imageName"));
        machineVO.setNativeId(basicInfo.getString("cloud_vm#nativeId"));
        machineVO.setStatus(basicInfo.getString("cloud_vm#status"));
        machineVO.setPrivateIps(basicInfo.getString("cloud_vm#privateIps"));
        machineVO.setProjectId(basicInfo.getString("cloud_vm#projectId"));
        machineVO.setCpuUsage(basicInfo.getFloat("cloud_vm#cpuUsage") == null ? 0 : basicInfo.getFloat("cloud_vm#cpuUsage"));
        machineVO.setDiskUsage(basicInfo.getString("cloud_vm#diskUsage"));
        machineVO.setBizRegionName(basicInfo.getString("cloud_vm#bizRegionName"));
        machineVO.setBizRegionId(basicInfo.getString("cloud_vm#bizRegionId"));
        machineVO.setMemoryUsage(basicInfo.getFloat("cloud_vm#memoryUsage") == null ? 0 : basicInfo.getFloat("cloud_vm#memoryUsage"));
        machineVO.setFloatingIp(basicInfo.getString("cloud_vm#floatingIp"));
        machineVO.setFlavor(basicInfo.getString("cloud_vm#flavor"));
        machineVO.setVdcName(basicInfo.getString("cloud_vm#vdcName"));
        machineVO.setOsVersion(basicInfo.getString("cloud_vm#osVersion"));
        String names = basicInfo.getString("cloud_vm#name");
        JSONObject name = JSONObject.parseObject(names);
        Map<String, Object> linkMap = new HashMap<>();
        linkMap.put("value", name.get("value"));
        linkMap.put("type", name.get("type"));
        linkMap.put("view", name.get("view"));
        machineVO.setName(linkMap);
        String conditions = name.get("condition").toString();
        JSONObject conditionsJson = JSONObject.parseObject(conditions);
        VirtualMachineConditionVO conditionVO = new VirtualMachineConditionVO();
        conditionVO.setResId(getString(conditionsJson.get("cloud_vm#resId")));
        conditionVO.setBizRegionId(getString(conditionsJson.get("cloud_vm#regionId")));
        conditionVO.setRegionId(getString(conditionsJson.get("cloud_vm#bizRegionId")));
        machineVO.setCondition(conditionVO);
        machineVO.setAzoneId(basicInfo.getString("cloud_vm#azoneId"));
        machineVO.setAzoneName(basicInfo.getString("cloud_vm#azoneName"));
        machineVO.setPhysicalHostId(basicInfo.getString("cloud_vm#physicalHostId"));
        return machineVO;
    }


    @Override
    public List<VirtualMachineVO> listVM(RequestVO requestVO) throws Exception {
        log.info("listVM, omUrl={}, requestVO={}", webUmUrl, requestVO);
        List<VirtualMachineVO> list = new ArrayList<>();
//        Assert.isTrue(StringUtils.isNotBlank(requestVO.getOmToken()), "token cannot be blank");
//        Assert.isTrue(StringUtils.isNotBlank(requestVO.getVdcId()) || StringUtils.isNotBlank(requestVO.getProjectId()), "vdcId and projectId cannot be both blank");
//
//        String url = webUmUrl + "/rest/tenant-resource/v1/cloud-resource/instance";
//
//        JSONArray condition = new JSONArray();
//        // 以VDC作为查询条件
//        if (StringUtils.isNotBlank(requestVO.getVdcId())) {
//            JSONObject conditionItem = new JSONObject();
//            conditionItem.put("propName", "cloud_vm#vdcId");
//            conditionItem.put("propValue", requestVO.getVdcId());
//            conditionItem.put("caseSensitive", "true");
//            conditionItem.put("operator", "equal");
//            condition.add(conditionItem);
//        }
//        // 以ProjectId作为查询条件
//        if (StringUtils.isNotBlank(requestVO.getProjectId())) {
//            JSONObject conditionItem = new JSONObject();
//            conditionItem.put("propName", "cloud_vm#projectId");
//            conditionItem.put("propValue", requestVO.getProjectId());
//            conditionItem.put("caseSensitive", "true");
//            conditionItem.put("operator", "equal");
//            condition.add(conditionItem);
//        }
//        log.info("listVM {}",condition);
//        Map<String, String> params = ImmutableMap.of("type", "cloud_vm",
//                "condition", URLEncoder.encode(condition.toJSONString(), UTF8),
//                "pageNo", "1",
//                "pageSize", "1000",//todo 注意：分页查询
//                "attrs", URLEncoder.encode("[\"cloud_vm#name\",\"cloud_vm#nativeId\",\"cloud_vm#privateIps\",\"cloud_vm#projectId\",\"cloud_vm#status\",\"cloud_vm#floatingIp\",\"cloud_vm#bizRegionName\",\"cloud_vm#cpuUsage\",\"cloud_vm#memoryUsage\",\"cloud_vm#diskUsage\",\"cloud_vm#flavor\",\"cloud_vm#osVersion\",\"cloud_vm#vdcName\",\"cloud_vm#bizRegionId\",\"cloud_vm#resId\",\"cloud_vm#azoneId\",\"cloud_vm#azoneName\",\"cloud_vm#physicalHostId\",\"cloud_vm#imageName\"]", UTF8));
//
//        String result = getForCookie(url, requestVO.getOmToken(), params);
//        log.info("listVM, omUrl={}, requestVO={}, result={}", webUmUrl, requestVO, result);


        //读取本地json
        String path = "/json/vm.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
//            System.out.println(object);
        }

//        JSONObject object = JSON.parseObject(result);

        JSONObject responseData = object.getJSONObject("responseData");
        JSONArray jsonArray = responseData.getJSONArray("data");//获取数组

        for (int i = 0; i < jsonArray.size(); i++) {
            VirtualMachineVO machineVO = new VirtualMachineVO();
            machineVO.setImageName(jsonArray.getJSONObject(i).getString("cloud_vm#imageName"));
            machineVO.setNativeId(jsonArray.getJSONObject(i).getString("cloud_vm#nativeId"));
            machineVO.setStatus(jsonArray.getJSONObject(i).getString("cloud_vm#status"));
            machineVO.setPrivateIps(jsonArray.getJSONObject(i).getString("cloud_vm#privateIps"));
            machineVO.setProjectId(jsonArray.getJSONObject(i).getString("cloud_vm#projectId"));
            machineVO.setCpuUsage(jsonArray.getJSONObject(i).getFloat("cloud_vm#cpuUsage") == null ? 0 : jsonArray.getJSONObject(i).getFloat("cloud_vm#cpuUsage"));
            machineVO.setDiskUsage(jsonArray.getJSONObject(i).getString("cloud_vm#diskUsage") == null ? "0" : jsonArray.getJSONObject(i).getString("cloud_vm#diskUsage"));
            machineVO.setBizRegionName(jsonArray.getJSONObject(i).getString("cloud_vm#bizRegionName"));
            machineVO.setBizRegionId(jsonArray.getJSONObject(i).getString("cloud_vm#bizRegionId"));
            machineVO.setMemoryUsage(jsonArray.getJSONObject(i).getFloat("cloud_vm#memoryUsage") == null ? 0 : jsonArray.getJSONObject(i).getFloat("cloud_vm#memoryUsage"));
            machineVO.setFloatingIp(jsonArray.getJSONObject(i).getString("cloud_vm#floatingIp"));
            String perf = jsonArray.getJSONObject(i).getString("supportHistoryPerf");
            machineVO.setSupportHistoryPerf(Boolean.valueOf(perf));
            machineVO.setFlavor(jsonArray.getJSONObject(i).getString("cloud_vm#flavor"));
            machineVO.setVdcName(jsonArray.getJSONObject(i).getString("cloud_vm#vdcName"));
            machineVO.setOsVersion(jsonArray.getJSONObject(i).getString("cloud_vm#osVersion"));
            //名称
            String names = jsonArray.getJSONObject(i).getString("cloud_vm#name");
            JSONObject linkJson = JSONObject.parseObject(names);
            Map<String, Object> linkMap = new HashMap<>();
            linkMap.put("value", linkJson.get("value"));
            linkMap.put("type", linkJson.get("type"));
            linkMap.put("view", linkJson.get("view"));
            machineVO.setName(linkMap);
            //condition
            String conditions = linkJson.get("condition").toString();
            JSONObject conditionsJson = JSONObject.parseObject(conditions);
            VirtualMachineConditionVO conditionVO = new VirtualMachineConditionVO();
            conditionVO.setResId(getString(conditionsJson.get("cloud_vm#resId")));
            conditionVO.setBizRegionId(getString(conditionsJson.get("cloud_vm#regionId")));
            conditionVO.setRegionId(getString(conditionsJson.get("cloud_vm#bizRegionId")));
            machineVO.setCondition(conditionVO);
            machineVO.setAzoneId(jsonArray.getJSONObject(i).getString("cloud_vm#azoneId"));
            machineVO.setAzoneName(jsonArray.getJSONObject(i).getString("cloud_vm#azoneName"));
            machineVO.setPhysicalHostId(jsonArray.getJSONObject(i).getString("cloud_vm#physicalHostId"));
            list.add(machineVO);
        }
        return list;

    }


    public String getString(Object value) {
        if (value == null) {
            return "";
        }

        return value.toString();
    }

    /**
     * get方法
     *
     * @param url    url
     * @param params 参数
     * @return 返回结果
     * @throws IOException io异常
     */
    private static String getForCookie(String url, final String token, Map<String, String> params) throws Exception {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 5);

        return BaseOkHttpClientUtil.getWithCookie(url, params, new CookieJar() {

            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                // nothing to do
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                Cookie cookie = new Cookie.Builder()
                        .name("bspsession").value(token).expiresAt(now.getTime().getTime()).domain(httpUrl.host()).httpOnly().secure().build();
                return Lists.newArrayList(cookie);
            }
        });
    }

    @Override
    public List<VmFlavors> VmFlavorsList(String token,String projectId) throws Exception {
        List<VmFlavors> vmFlavors = new ArrayList<>();
        String url = ecs_url + "/v2/"+projectId+"/flavors/detail";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token", token);
        String result = BaseOkHttpClientUtil.get(url, headerMap);
        // 字符串转换为JSON
        JSONObject jsonObject = JSON.parseObject(result);
        String tempObj = jsonObject.getString("flavors");
        if(StringUtils.isNotEmpty(tempObj)){
            vmFlavors=JSONArray.parseArray(tempObj, VmFlavors.class);
        }
        return vmFlavors;
    }
}
