package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
//import com.ecdata.cmp.huawei.dto.vo.BareMetalVO;
import com.ecdata.cmp.huawei.dto.vo.BaremetalServersFlavorsVO;
import com.ecdata.cmp.huawei.dto.vo.BmsInfoVO;
import com.ecdata.cmp.huawei.dto.vo.CloudServersFlavorsVO;
import com.ecdata.cmp.huawei.service.AvailableZoneService;
import com.ecdata.cmp.huawei.service.BaremetalServersFlavorsService;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.iaas.client.VirtualDataCenterClient;
import com.ecdata.cmp.iaas.entity.dto.response.VirtualDataCenterResponse;
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
 * @date ：Created in 2020/4/1 15:42
 * @modified By：
 */
@Service
@Slf4j
public class BaremetalServersFlavorsServiceImpl implements BaremetalServersFlavorsService{

    @Value("${huawei.ManageOne.bms_url}")
    private String bmsUrl;

    @Value("${huawei.ManageOne.ecs_url}")
    private String ecsUrl;

    @Autowired
    private ManageOneService manageOneService;

    @Autowired
    private AvailableZoneService availableZoneService;

    @Autowired
    private VirtualDataCenterClient virtualDataCenterClient;

    @Override
    public List<BaremetalServersFlavorsVO> getBaremetalServersFlavors(String id) throws IOException {

        List<BaremetalServersFlavorsVO> list = new ArrayList<>();

        //读取本地文件
        String path = "/json/bmsflavors.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取裸金属文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
        }

        log.info("裸金属规格 {}",object);
        JSONArray jsonArray = object.getJSONArray("flavors");//获取数组
        for(int i=0;i<jsonArray.size();i++){
            BaremetalServersFlavorsVO flavorsVO = new BaremetalServersFlavorsVO();

            flavorsVO.setId(jsonArray.getJSONObject(i).getString("id"));
            flavorsVO.setName(jsonArray.getJSONObject(i).getString("name"));
            flavorsVO.setVcpus(jsonArray.getJSONObject(i).getString("vcpus"));

            String ram = jsonArray.getJSONObject(i).getString("ram");
            if(!"".equals(ram) && ram!=null){
                ram = String.valueOf(Integer.parseInt(ram)/1024);
            }
            flavorsVO.setRam(ram);

            flavorsVO.setDisk(jsonArray.getJSONObject(i).getString("disk"));

            //详细信息
            String os_extra_specs = jsonArray.getJSONObject(i).getString("os_extra_specs");
            JSONObject oes = JSON.parseObject(os_extra_specs);
            flavorsVO.setDiskDetail(oes.get("baremetal:disk_detail").toString());
            flavorsVO.setDeploytype(oes.get("huawei:deploytype").toString());
            flavorsVO.setHypervisorType(oes.get("capabilities:hypervisor_type").toString());
            flavorsVO.setCpuArch(oes.get("capabilities:cpu_info:arch").toString());
            flavorsVO.setBoardType(oes.get("capabilities:board_type").toString());
            flavorsVO.setExtBootType(oes.get("huawei:extBootType").toString());
            flavorsVO.setNetNum(oes.get("baremetal:net_num").toString());
            flavorsVO.setNetcardDetail(oes.get("baremetal:netcard_detail").toString());
            flavorsVO.setCpuDetail(oes.get("baremetal:cpu_detail").toString());
            flavorsVO.setResourceType(oes.get("resource_type").toString());
            flavorsVO.setMemoryDetail(oes.get("baremetal:memory_detail").toString());

            list.add(flavorsVO);
        }

        return list;


    }

    @Override
    public List<CloudServersFlavorsVO> getCloudServersFlavors(String id) throws IOException {
        List<CloudServersFlavorsVO> list = new ArrayList<>();

        //读取本地文件
        String path = "/json/vmflavors.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject object = null;
        if (config == null) {
            throw new RuntimeException("读取裸金属文件失败");
        } else {
            object = JSON.parseObject(config, JSONObject.class);
        }

        log.info("弹性云规格 {}",object);
        JSONArray jsonArray = object.getJSONArray("flavors");//获取数组
        for(int i=0;i<jsonArray.size();i++){
            CloudServersFlavorsVO flavorsVO = new CloudServersFlavorsVO();

            flavorsVO.setId(jsonArray.getJSONObject(i).getString("id"));
            flavorsVO.setName(jsonArray.getJSONObject(i).getString("name"));
            flavorsVO.setVcpus(jsonArray.getJSONObject(i).getString("vcpus"));

            String ram = jsonArray.getJSONObject(i).getString("ram");
            if(!"".equals(ram) && ram!=null){
                ram = String.valueOf(Integer.parseInt(ram)/1024);
            }
            flavorsVO.setRam(ram);

            //详细信息
            String os_extra_specs = jsonArray.getJSONObject(i).getString("os_extra_specs");
            JSONObject oes = JSON.parseObject(os_extra_specs);
            flavorsVO.setPerformanceType(oes.get("ecs:performancetype").toString());
            flavorsVO.setResourceType(oes.get("resource_type").toString());
            flavorsVO.setExtBootType(oes.get("resource_type").toString());

            list.add(flavorsVO);
        }

        return list;
    }

    @Override
    public List<BareMetalVO> getBareMetalAll(String projectId) throws IOException {

        List<BareMetalVO> list = new ArrayList<>();

        //1.页面接口1 getBmsList(String vdcKey) 需要vdckey查询条件
        String authz = AuthContext.getAuthz();
        VirtualDataCenterResponse vdc = virtualDataCenterClient.getVdcById(authz, projectId);
        List<BmsInfoVO> bmsList = availableZoneService.getBmsList(vdc.getData().getVdcKey());
        //2.页面接口2 getBmsListByProjectId(String projectId)
        List<BmsInfoVO> bmsListByProjectId = availableZoneService.getBmsListByProjectId(projectId);
        //3.规格列表 getBaremetalServersFlavors(String id) 项目id
        List<BaremetalServersFlavorsVO> baremetalServersFlavors = getBaremetalServersFlavors(projectId);
        //4.组装
        for (int i =0; i<bmsList.size();i++){
            BareMetalVO bareMetalVO = new BareMetalVO();
            BmsInfoVO bmsInfoVO = bmsList.get(i);
            bareMetalVO.setNativieId(bmsInfoVO.getNativeId());
            bareMetalVO.setValue(bmsInfoVO.getValue());
            bareMetalVO.setStatus(bmsInfoVO.getStatus());
            bareMetalVO.setPrivateIps(bmsInfoVO.getPrivateIps());
            bareMetalVO.setFloatingIp(bmsInfoVO.getFloatingIp());
            bareMetalVO.setResourcePoolName(bmsInfoVO.getResourcePoolName());
            bareMetalVO.setBizRegionName(bmsInfoVO.getBizRegionName());
            bareMetalVO.setRegionId(bmsInfoVO.getRegionId());
            bareMetalVO.setAzoneInfo(bmsInfoVO.getAzoneInfo());
            bareMetalVO.setName(bmsInfoVO.getValue());
            for (int j =0; j<bmsListByProjectId.size();j++){
                BmsInfoVO bmsInfoVO1 = bmsListByProjectId.get(j);
                if(bmsInfoVO.getNativeId().equals(bmsInfoVO1.getNativeId())){
                    bareMetalVO.setDetailId(bmsInfoVO1.getId());
                    bareMetalVO.setTenantId(bmsInfoVO1.getTenantId());
                    for (int k =0; k<baremetalServersFlavors.size();k++){
                        BaremetalServersFlavorsVO flavorsVO = baremetalServersFlavors.get(k);
                        if (bmsInfoVO1.getId().equals(flavorsVO.getId())){
                            bareMetalVO.setDetailName(flavorsVO.getName());
                            bareMetalVO.setVcpus(flavorsVO.getVcpus());
                            bareMetalVO.setRam(flavorsVO.getRam());
                            bareMetalVO.setDisk(flavorsVO.getDisk());
                        }
                    }
                }
            }
            list.add(bareMetalVO);
        }
        log.info("getBareMetalAll-{}",list);
        return list;
    }


}
