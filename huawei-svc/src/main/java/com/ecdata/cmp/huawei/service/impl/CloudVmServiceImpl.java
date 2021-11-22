package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.CloudVmVO;
import com.ecdata.cmp.huawei.service.CloudVmService;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.iaas.client.VirtualDataCenterClient;
import com.ecdata.cmp.iaas.entity.dto.response.VirtualDataCenterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author ：xuj
 * @date ：Created in 2020/5/9 10:43
 * @modified By：
 */
@Service
@Slf4j
public class CloudVmServiceImpl implements CloudVmService {


    @Value("${huawei.ManageOne.ecs_url}")
    private String ecs_url;

    @Autowired
    private VirtualDataCenterClient virtualDataCenterClient;

    @Autowired
    private ManageOneService manageOneService;

    @Override
    public boolean createVm(CloudVmVO vo) {

        String authz = AuthContext.getAuthz();
        VirtualDataCenterResponse vdc = virtualDataCenterClient.getVdcById(authz, vo.getProjectId());
        try {
            String token = manageOneService.getOCToken(vdc.getData().getUsername(), vdc.getData().getPassword(), vdc.getData().getDomainName(), vdc.getData().getProjectKey());
            String url = ecs_url + "/v1/" + vdc.getData().getProjectKey() + "/cloudservers";
            Map param = new HashMap();
            param.put("X-Auth-Token", token);
            JSONObject server = new JSONObject();
            JSONObject object = new JSONObject();
            object.put("availability_zone", vo.getAvailabilityZone());
            object.put("name", vo.getName());
            object.put("imageRef", vo.getImageRef());
            JSONObject root_volume = new JSONObject();
            root_volume.put("size", vo.getSize());
            root_volume.put("volumetype", vo.getVolumetype());
            object.put("root_volume", root_volume);
            object.put("flavorRef", vo.getFlavorRef());
            object.put("vpcid", vo.getVpcid());
            JSONArray security_groups = new JSONArray();
            JSONObject id = new JSONObject();
            id.put("id", vo.getId());
            security_groups.add(id);
            object.put("security_groups", security_groups);
            JSONArray nics = new JSONArray();
            JSONObject subnet_id = new JSONObject();
            subnet_id.put("subnet_id", vo.getSubnetId());
            subnet_id.put("ip_address",vo.getIpAddress());
            nics.add(subnet_id);
            object.put("nics", nics);
            JSONObject publicip = new JSONObject();
            publicip.put("id", vo.getEipId());
            object.put("publicip", publicip);
            object.put("adminPass", vo.getAdminPass());
            object.put("count", vo.getCount());
            server.put("server", object);

            String result = BaseOkHttpClientUtil.post(url, server, param);
            log.info("result createVm {}", result);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }
}
