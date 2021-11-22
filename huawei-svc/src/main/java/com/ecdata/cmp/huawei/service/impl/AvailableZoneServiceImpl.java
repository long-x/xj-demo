package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.availablezone.*;
import com.ecdata.cmp.huawei.dto.token.OMToken;
import com.ecdata.cmp.huawei.dto.vo.BmsInfoVO;
import com.ecdata.cmp.huawei.service.AvailableZoneService;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.iaas.client.VirtualDataCenterClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class AvailableZoneServiceImpl implements AvailableZoneService {
    public static final String mediaType = "application/json";
    /**
     * json映射
     */
    private ObjectMapper jsonMapper = new ObjectMapper();

    @Value("${huawei.ManageOne.mo_url}")
    private String moUrl;

    @Value("${huawei.ManageOne.om_url}")
    private String omUrl;

    @Value("${huawei.ManageOne.evs_url}")
    private String evs_url;

    @Value("${huawei.ManageOne.web_om_url}")
    private String web_om_url;

    @Value("${huawei.ManageOne.ecs_url}")
    private String ecs_url;

    @Autowired
    private VirtualDataCenterClient virtualDataCenterClient;

    @Autowired
    private ManageOneService manageOneService;


    @Override
    public List<AvailableZone> getAvailableZoneByInfraId(String token, String infraId) throws IOException {
        String url =  moUrl+ "/rest/serviceaccess/v3.0/available-zones?cloud_infra_id="+"FUSION_CLOUD_sa-fb-1";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String instanceData = BaseOkHttpClientUtil.get(url, headerMap);
        // 字符串转换为JSON
        JSONObject jsonObject = JSON.parseObject(instanceData);
        String tempObj = jsonObject.getString("records");
        return JSONArray.parseArray(tempObj, AvailableZone.class);
    }

    @Override
    public List<SysAzone> getAvailableZoneById(String token, String nativeId) throws IOException {
        String url =  omUrl+ "/rest/tenant-resource/v1/instances/SYS_Azone";
        String requestUrl=url+"?condition={\"constraint\":[{\"simple\":{\"name\":\"nativeId\",\"value\":\""+nativeId+"\"}}]}";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String instanceData = BaseOkHttpClientUtil.get(requestUrl, headerMap);
        // 字符串转换为JSON
        JSONObject jsonObject = JSON.parseObject(instanceData);
        String tempObj = jsonObject.getString("objList");
        return JSONArray.parseArray(tempObj, SysAzone.class);
    }

    @Override
    public Map<String,WholeCapacity> getAvailableZoneCapacity(String token, List<SysAzone> SysAzoneList) throws IOException {
        Map<String,WholeCapacity> capacityMap = new HashMap<String,WholeCapacity>();
        if (!CollectionUtils.isEmpty(SysAzoneList)){
            SysAzone sysAzone = SysAzoneList.get(0);
            String resId = sysAzone.getResid();
            WholeCapacity cpuCapicity = getCapicityByQuota(token, resId, ManageOneConstant.CPU, ManageOneConstant.CPU);
            WholeCapacity memoryCapicity = getCapicityByQuota(token, resId, ManageOneConstant.MEMORY, ManageOneConstant.MEMORY);
            capacityMap.put(ManageOneConstant.CPU,cpuCapicity);
            capacityMap.put(ManageOneConstant.MEMORY,memoryCapicity);
        }
        return capacityMap;
    }

    private WholeCapacity getCapicityByQuota(String token,String resId,String QuotaIn,String QuotaOut) throws IOException {
        String url =  omUrl+ "/rest/capacity/v1/capbase/azones/"+resId+"/resource-types/"+QuotaIn+"/current-capacities";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String instanceData = BaseOkHttpClientUtil.get(url, headerMap);
        // 字符串转换为JSON
        JSONObject jsonObject = JSON.parseObject(instanceData);
        String tempObj = jsonObject.getString(QuotaOut);
        return JSONObject.parseObject(tempObj, WholeCapacity.class);
    }

    @Override
    public List<WholeDimensionCapacity> getAvailableZoneStoragePoolCapacity(String token, List<SysAzone> SysAzoneList, String dimensionType) throws IOException {
        List<WholeDimensionCapacity> wholeDimensionCapacities=new ArrayList<WholeDimensionCapacity>();
        if (!CollectionUtils.isEmpty(SysAzoneList)){
            SysAzone sysAzone = SysAzoneList.get(0);
            String resId = sysAzone.getResid();
            String url =  omUrl+ "/rest/capacity/v1/capbase/azones/"+resId+"/resource-types/storage-pool/dimension-types/volumeTypeName/capacities";
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("X-Auth-Token",token);
            String instanceData = BaseOkHttpClientUtil.get(url, headerMap);
            // 字符串转换为JSON
            JSONObject jsonObject = JSON.parseObject(instanceData);
            String tempObj = jsonObject.getString("capacityList");
            wholeDimensionCapacities = JSONArray.parseArray(tempObj, WholeDimensionCapacity.class);
        }
        return wholeDimensionCapacities;
    }
    @Override
    public AvailableZoneStatistics getAvailableZoneDetail(String token, String azoneId) throws IOException {
        String url =  omUrl+ "/rest/capacity/v1/capbase/azones/"+azoneId+"/statistics";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token",token);
        String regionData = BaseOkHttpClientUtil.get(url, headerMap);
        return jsonMapper.readValue(regionData, AvailableZoneStatistics.class);
    }

    @Override
    public AvailableZoneResource getAvailableZoneResource(String token, String azoneId) throws IOException {
        AvailableZoneResource availableZoneResource = new AvailableZoneResource();
        List<SysAzone> sysAzones = getAvailableZoneById(token, azoneId);
        if(!CollectionUtils.isEmpty(sysAzones)){
            String resId = sysAzones.get(0).getResid();
            Map<String, WholeCapacity> availableZoneCapacity = getAvailableZoneCapacity(token, sysAzones);
            AvailableZoneStatistics StatisticsDetail = getAvailableZoneDetail(token, resId);
            List<WholeDimensionCapacity> volumeCapatity = getAvailableZoneStoragePoolCapacity(token, sysAzones, "volumeTypeName");
            availableZoneResource.setAvailableZoneStatistics(StatisticsDetail);
            availableZoneResource.setVCPU(availableZoneCapacity.get(ManageOneConstant.CPU));
            availableZoneResource.setMemory(availableZoneCapacity.get(ManageOneConstant.MEMORY));
            availableZoneResource.setCapacityList(volumeCapatity);
        }
        return availableZoneResource;
    }

    @Override
    public List<ProjectAviailableZone> getAllAvailableZoneByProjectId(String token, String projectId) throws IOException {
        List<ProjectAviailableZone> projectAviailableZones = new ArrayList<>();
        String url = evs_url + "/v2/"+projectId+"/os-availability-zone";
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token", token);
        String result = BaseOkHttpClientUtil.get(url, headerMap);
        // 字符串转换为JSON
        JSONObject jsonObject = JSON.parseObject(result);
        String tempObj = jsonObject.getString("availabilityZoneInfo");
        if(StringUtils.isNotEmpty(tempObj)){
            projectAviailableZones=JSONArray.parseArray(tempObj, ProjectAviailableZone.class);
        }
        return projectAviailableZones;
    }

    @Override
    public List<AvailableZone> getAvailableZoneByProjectId(String token, String projectId) throws IOException {
        ArrayList<AvailableZone> availableZones = new ArrayList<>();
        //获取项目下所有可用分区
        List<ProjectAviailableZone> allAvailableZoneByProjectId = getAllAvailableZoneByProjectId(token, projectId);
        //获取资源池下所有可用分区
        List<AvailableZone> availableZoneByInfraId = getAvailableZoneByInfraId(token, "FUSION_CLOUD_sa-fb-1");
        remove(availableZoneByInfraId);
        if(CollectionUtils.isEmpty(allAvailableZoneByProjectId)){
            return availableZones;
        }
        for (int i = 0; i <allAvailableZoneByProjectId.size() ; i++) {
            for (int j = 0; j < availableZoneByInfraId.size(); j++) {
                ProjectAviailableZone projectAviailableZone = allAvailableZoneByProjectId.get(i);
                AvailableZone availableZone = availableZoneByInfraId.get(j);
                if(projectAviailableZone.getZoneName().equals(availableZone.getAz_id())){
                    availableZones.add(availableZone);
                }
            }
        }
        return availableZones;
    }





    /**
     * 页面接口 查询vdc的裸金属 页面接口1
     * @param vdcKey
     * @return
     * @throws IOException
     */
    @Override
    public List<BmsInfoVO> getBmsList(String vdcKey) throws IOException {

        //读取本地文件
        String path = "/json/bms1.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject jsonObject = null;
        if (config == null) {
            throw new RuntimeException("读取裸金属文件失败");
        } else {
            jsonObject = JSON.parseObject(config, JSONObject.class);
        }

        List<BmsInfoVO> list = new ArrayList<>();
        JSONObject responseData = jsonObject.getJSONObject("responseData");
        JSONArray jsonArray = responseData.getJSONArray("data");//获取数组
        for (int i = 0; i < jsonArray.size(); i++) {
            BmsInfoVO bmsInfoVO = new BmsInfoVO();
            bmsInfoVO.setFloatingIp(jsonArray.getJSONObject(i).getString("cloud_bms#floatingIp"));
            bmsInfoVO.setResourcePoolName(jsonArray.getJSONObject(i).getString("cloud_bms#resourcePoolName"));
            bmsInfoVO.setResId(jsonArray.getJSONObject(i).getString("cloud_bms#resId"));
            bmsInfoVO.setBizRegionName(jsonArray.getJSONObject(i).getString("cloud_bms#bizRegionName"));
            bmsInfoVO.setStatus(jsonArray.getJSONObject(i).getString("cloud_bms#status"));
            bmsInfoVO.setVdcName(jsonArray.getJSONObject(i).getString("cloud_bms#vdcName"));
            bmsInfoVO.setNativeId(jsonArray.getJSONObject(i).getString("cloud_bms#nativeId"));
            bmsInfoVO.setSupportHistoryPerf(jsonArray.getJSONObject(i).getString("supportHistoryPerf"));
            bmsInfoVO.setAzoneInfo(jsonArray.getJSONObject(i).getString("cloud_bms#azoneInfo"));
            bmsInfoVO.setPrivateIps(jsonArray.getJSONObject(i).getString("cloud_bms#privateIps"));
            bmsInfoVO.setFlavorCpu(jsonArray.getJSONObject(i).getString("cloud_bms#flavorCpu"));
            bmsInfoVO.setFlavorDisk(jsonArray.getJSONObject(i).getString("cloud_bms#flavorDisk"));
            bmsInfoVO.setFlavorMemory(jsonArray.getJSONObject(i).getString("cloud_bms#flavorMemory"));
            bmsInfoVO.setBizRegionId(jsonArray.getJSONObject(i).getString("cloud_bms#bizRegionId"));
            bmsInfoVO.setSupportRealPerf(jsonArray.getJSONObject(i).getString("supportRealPerf"));
            String names = jsonArray.getJSONObject(i).getString("cloud_bms#name");
            JSONObject name = JSON.parseObject(names);
            JSONObject condition = name.getJSONObject("condition");
            String regionId = (String) condition.get("cloud_bms#regionId");
            bmsInfoVO.setRegionId(regionId);
            bmsInfoVO.setType(name.get("type").toString());
            bmsInfoVO.setValue(name.get("value").toString());

            list.add(bmsInfoVO);
        }
        return list;

    }

    /**
     * 页面接口2
     * @param projectId
     * @return
     * @throws IOException
     */
    @Override
    public List<BmsInfoVO> getBmsListByProjectId(String projectId) throws IOException {

        //读取本地文件
        String path = "/json/bms2.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject jsonObject = null;
        if (config == null) {
            throw new RuntimeException("读取裸金属文件失败");
        } else {
            jsonObject = JSON.parseObject(config, JSONObject.class);
        }


        // 字符串转换为JSON
//        JSONObject jsonObject = JSON.parseObject(result);
        List<BmsInfoVO> list = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("servers");
        for (int i = 0; i < jsonArray.size(); i++) {
            BmsInfoVO bmsInfoVO = new BmsInfoVO();
            bmsInfoVO.setValue(jsonArray.getJSONObject(i).getString("name"));
            bmsInfoVO.setNativeId(jsonArray.getJSONObject(i).getString("id"));//裸金属id
            bmsInfoVO.setStatus(jsonArray.getJSONObject(i).getString("status"));
            bmsInfoVO.setTenantId(jsonArray.getJSONObject(i).getString("tenant_id"));
            String flavor = jsonArray.getJSONObject(i).getString("flavor");

            JSONObject object = JSON.parseObject(flavor);
            bmsInfoVO.setId(object.get("id").toString());

            list.add(bmsInfoVO);
        }

        return list;
    }


    @Override
    public List<SysAzone> getAzAllList(String type) throws IOException {

        //读取本地文件
        String path = "/json/az.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject jsonObject = null;
        if (config == null) {
            throw new RuntimeException("读取区域文件失败");
        } else {
            jsonObject = JSON.parseObject(config, JSONObject.class);
        }

        // 字符串转换为JSON
        List<SysAzone> list = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("objList");//获取数组
        for (int i = 0; i < jsonArray.size(); i++) {
            SysAzone sysAzone = new SysAzone();
            String globalName = jsonArray.getJSONObject(i).getString("globalName");
            if(globalName!=null && !"".equals(globalName)){
                String virtualizeType = jsonArray.getJSONObject(i).getString("virtualizeType");
                if(virtualizeType.equals(type)){
                    sysAzone.setId(jsonArray.getJSONObject(i).getString("id"));
                    sysAzone.setClassName(jsonArray.getJSONObject(i).getString("SYS_Azone"));
                    sysAzone.setVirtualizeType(jsonArray.getJSONObject(i).getString("virtualizeType"));
                    sysAzone.setLastModified(Long.parseLong(jsonArray.getJSONObject(i).getString("last_Modified")));
//                    sysAzone.setResid(jsonArray.getJSONObject(i).getString("resId"));
                    sysAzone.setResid(jsonArray.getJSONObject(i).getString("nativeId"));
                    sysAzone.setName(jsonArray.getJSONObject(i).getString("name"));
                    sysAzone.setGlobalname(jsonArray.getJSONObject(i).getString("globalName"));
                    list.add(sysAzone);
                }else if("".equals(type)|| type == null){
                    sysAzone.setId(jsonArray.getJSONObject(i).getString("id"));
                    sysAzone.setClassName(jsonArray.getJSONObject(i).getString("SYS_Azone"));
                    sysAzone.setVirtualizeType(jsonArray.getJSONObject(i).getString("virtualizeType"));
                    sysAzone.setLastModified(Long.parseLong(jsonArray.getJSONObject(i).getString("last_Modified")));
//                    sysAzone.setResid(jsonArray.getJSONObject(i).getString("resId"));
                    sysAzone.setResid(jsonArray.getJSONObject(i).getString("nativeId"));
                    sysAzone.setName(jsonArray.getJSONObject(i).getString("name"));
                    sysAzone.setGlobalname(jsonArray.getJSONObject(i).getString("globalName"));
                    list.add(sysAzone);
                }
            }
        }
        return list;

    }

    @Override
    public List<DimensionInfo> getDiskName(String azId) throws IOException {
        List<DimensionInfo> list = new ArrayList<>();

        //读取本地文件
        String path = "/json/diskName.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject jsonObject = null;
        if (config == null) {
            throw new RuntimeException("读取磁盘文件失败");
        } else {
            jsonObject = JSON.parseObject(config, JSONObject.class);
        }

        JSONArray jsonArray = jsonObject.getJSONArray("capacityList");//获取数组

        for (int i = 0; i < jsonArray.size(); i++) {
            DimensionInfo dimensionInfo = new DimensionInfo();
            String dimensions = jsonArray.getJSONObject(i).getString("dimensions");
            JSONArray objects = JSONArray.parseArray(dimensions);
            dimensionInfo.setDimensionType(objects.getJSONObject(0).getString("dimensionType"));
            dimensionInfo.setDimensionValue(objects.getJSONObject(0).getString("dimensionValue"));
            dimensionInfo.setVolumeTypeOriginal(objects.getJSONObject(0).getString("volumeTypeOriginal"));
            list.add(dimensionInfo);
        }

        return list;
    }


    public static void remove(List<AvailableZone> list) {
        Iterator<AvailableZone> sListIterator = list.iterator();
        while (sListIterator.hasNext()) {
            AvailableZone availableZone = sListIterator.next();
            if ("Ironic".equals(availableZone.getType())) {
                sListIterator.remove();
            }
        }
    }
}
