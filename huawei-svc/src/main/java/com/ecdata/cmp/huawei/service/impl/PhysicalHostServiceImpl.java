package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.PhysicalHostVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.service.PhysicalHostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 14:26
 * @modified By：
 */
@Service
@Slf4j
public class PhysicalHostServiceImpl implements PhysicalHostService {

    @Value("${huawei.ManageOne.om_url}")
    private String omUrl;

    @Override
    public List<PhysicalHostVO> getPhysicalHostList(RequestVO requestVO) throws Exception {
        String url = omUrl + "/rest/cmdb/v1/instances/SYS_PhysicalHost?pageNo=1&pageSize=1000";
        List<PhysicalHostVO> list = new ArrayList<>();
        Map param = new HashMap();
        param.put("X-Auth-Token", requestVO.getOcToken());
        String result = BaseOkHttpClientUtil.get(url, param);
        JSONObject object = JSON.parseObject(result);
        JSONArray jsonArray = object.getJSONArray("objList");//获取数组
        for (int i = 0; i < jsonArray.size(); i++) {
            PhysicalHostVO physicalHostVO = new PhysicalHostVO();
            physicalHostVO.setOwnerType(jsonArray.getJSONObject(i).getString("ownerType"));
            physicalHostVO.setAllocatedDisk(jsonArray.getJSONObject(i).getString("allocatedDisk"));
            physicalHostVO.setOwnerId(jsonArray.getJSONObject(i).getString("ownerId"));
            physicalHostVO.setDeviceName(jsonArray.getJSONObject(i).getString("deviceName"));
            physicalHostVO.setFreeVcpuCores(jsonArray.getJSONObject(i).getString("freeVcpuCores"));
            physicalHostVO.setTotalDisk(jsonArray.getJSONObject(i).getString("totalDisk"));
            physicalHostVO.setAllocatedCpu(jsonArray.getJSONObject(i).getString("allocatedCpu"));
            physicalHostVO.setTotalCpu(jsonArray.getJSONObject(i).getString("totalCpu"));
            physicalHostVO.setConfirmStatus(jsonArray.getJSONObject(i).getString("confirmStatus"));
            physicalHostVO.setId(jsonArray.getJSONObject(i).getString("id"));
            physicalHostVO.setAllocatedVcpuCores(jsonArray.getJSONObject(i).getString("allocatedVcpuCores"));
            physicalHostVO.setAllocatedMemory(jsonArray.getJSONObject(i).getString("allocatedMemory"));
            physicalHostVO.setTotalVmemoryMB(jsonArray.getJSONObject(i).getString("totalVmemoryMB"));
            physicalHostVO.setUsedCpu(jsonArray.getJSONObject(i).getString("usedCpu"));
            physicalHostVO.setUsedMemory(jsonArray.getJSONObject(i).getString("usedMemory"));
            physicalHostVO.setSerialNumber(jsonArray.getJSONObject(i).getString("serialNumber"));
            physicalHostVO.setClassId(jsonArray.getJSONObject(i).getString("class_Id"));
            physicalHostVO.setBmcIp(jsonArray.getJSONObject(i).getString("bmcIp"));
            physicalHostVO.setIpAddress(jsonArray.getJSONObject(i).getString("ipAddress"));
            physicalHostVO.setUsedDisk(jsonArray.getJSONObject(i).getString("usedDisk"));
            physicalHostVO.setHypervisorType(jsonArray.getJSONObject(i).getString("hypervisorType"));
            physicalHostVO.setResId(jsonArray.getJSONObject(i).getString("resId"));
            physicalHostVO.setAllocatedDiskSizeMB(jsonArray.getJSONObject(i).getString("allocatedDiskSizeMB"));
            physicalHostVO.setAllocatedVmemoryMB(jsonArray.getJSONObject(i).getString("allocatedVmemoryMB"));
            physicalHostVO.setClassName(jsonArray.getJSONObject(i).getString("class_Name"));
            physicalHostVO.setAzoneId(jsonArray.getJSONObject(i).getString("azoneId"));
            physicalHostVO.setResourcePoolId(jsonArray.getJSONObject(i).getString("resourcePoolId"));
            physicalHostVO.setTotalVcpuCores(jsonArray.getJSONObject(i).getString("totalVcpuCores"));
            physicalHostVO.setName(jsonArray.getJSONObject(i).getString("name"));
            physicalHostVO.setVirtual(jsonArray.getJSONObject(i).getBoolean("isVirtual"));
            physicalHostVO.setStatus(jsonArray.getJSONObject(i).getString("status"));
            physicalHostVO.setLogicalRegionName(jsonArray.getJSONObject(i).getString("logicalRegionName"));
            physicalHostVO.setAzoneName(jsonArray.getJSONObject(i).getString("azoneName"));
            physicalHostVO.setRegionName(jsonArray.getJSONObject(i).getString("regionName"));
            physicalHostVO.setFreeVmemoryMB(jsonArray.getJSONObject(i).getString("freeVmemoryMB"));
            physicalHostVO.setCpuQuantityForVirtualization(jsonArray.getJSONObject(i).getString("cpuQuantityForVirtualization"));
            physicalHostVO.setHypervisorEnable(jsonArray.getJSONObject(i).getBoolean("hypervisorEnable"));
            physicalHostVO.setOwnerName(jsonArray.getJSONObject(i).getString("ownerName"));
            physicalHostVO.setAllocatedRamSizeMB(jsonArray.getJSONObject(i).getString("allocatedRamSizeMB"));
            physicalHostVO.setLastModified(jsonArray.getJSONObject(i).getString("last_Modified"));
            physicalHostVO.setCpuRatio(jsonArray.getJSONObject(i).getString("cpuRatio"));
            physicalHostVO.setTotalRamSizeMB(jsonArray.getJSONObject(i).getString("totalRamSizeMB"));
            physicalHostVO.setTrustLvl(jsonArray.getJSONObject(i).getString("trustLvl"));
            physicalHostVO.setResourcePoolName(jsonArray.getJSONObject(i).getString("resourcePoolName"));
            physicalHostVO.setRamAllocationRatio(jsonArray.getJSONObject(i).getString("ramAllocationRatio"));
            physicalHostVO.setTotalDiskSizeMB(jsonArray.getJSONObject(i).getString("totalDiskSizeMB"));
            physicalHostVO.setKeystoneId(jsonArray.getJSONObject(i).getString("keystoneId"));
            physicalHostVO.setLogicalRegionId(jsonArray.getJSONObject(i).getString("logicalRegionId"));
            physicalHostVO.setTotalMemory(jsonArray.getJSONObject(i).getString("totalMemory"));
            physicalHostVO.setRegionId(jsonArray.getJSONObject(i).getString("regionId"));
            physicalHostVO.setService(jsonArray.getJSONObject(i).getString("service"));
            physicalHostVO.setNativeId(jsonArray.getJSONObject(i).getString("nativeId"));
            physicalHostVO.setFreeDiskSizeMB(jsonArray.getJSONObject(i).getString("freeDiskSizeMB"));
            list.add(physicalHostVO);
        }

        return list;
    }
}
