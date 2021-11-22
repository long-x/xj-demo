package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.huawei.service.OnOffMachineService;
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
 * @date ：Created in 2020/5/15 15:13
 * @modified By：
 */
@Service
@Slf4j
public class OnOffMachineServiceImpl implements OnOffMachineService {

    @Value("${huawei.ManageOne.ecs_url}")
    private String ecsUrl;

    @Value("${huawei.ManageOne.bms_url}")
    private String bmsUrl;


    @Autowired
    private ManageOneService manageOneService;

    @Autowired
    private VirtualDataCenterClient virtualDataCenterClient;

    @Override
    public boolean closeVm(String projectId, String vmId,String type) {

        String authz = AuthContext.getAuthz();
        VirtualDataCenterResponse vdc = virtualDataCenterClient.getVdcById(authz, projectId);
        try {
            String token = manageOneService.getOCToken(vdc.getData().getUsername(), vdc.getData().getPassword(), vdc.getData().getDomainName(), vdc.getData().getProjectKey());
            String url = ecsUrl +"/v2/"+vdc.getData().getProjectKey()+"/servers/"+vmId+"/action";


            Map param = new HashMap();
            param.put("X-Auth-Token",token);
            String body = "";
            if("on".equals(type)){
                body = "{ \n" +
                        "    \"os-stop\": {\n" +
                        "          \"type\": \"SOFT\" \n" +
                        "   } \n" +
                        "}";
            }else {
                body = "{ \n" +
                        "    \"os-start\": {} \n" +
                        "}";
            }

            JSONObject jsonObject = JSONObject.parseObject(body);
            String result = BaseOkHttpClientUtil.post(url, jsonObject, param);

            return true;


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }



    @Override
    public boolean closeBaremetal(String projectId, String BareId,String type) {

        String authz = AuthContext.getAuthz();
        VirtualDataCenterResponse vdc = virtualDataCenterClient.getVdcById(authz, projectId);
        try {
            String token = manageOneService.getOCToken(vdc.getData().getUsername(), vdc.getData().getPassword(), vdc.getData().getDomainName(), vdc.getData().getProjectKey());
            String url = bmsUrl +"/v1/"+vdc.getData().getProjectKey()+"/baremetalservers/action";


            Map param = new HashMap();
            param.put("X-Auth-Token",token);
            String body = "";
            if("on".equals(type)){
                body = "{  \n" +
                        "     \"os-stop\": {  \n" +
                        "         \"servers\": [  \n" +
                        "               \n" +
                        "             {  \n" +
                        "                 \"id\": \""+BareId+"\"  \n" +
                        "             }  \n" +
                        "         ]  \n" +
                        "     }  \n" +
                        " }";
            }else{
                body = "{  \n" +
                        "     \"os-start\": {  \n" +
                        "         \"servers\": [  \n" +
                        "               \n" +
                        "             {  \n" +
                        "                 \"id\": \""+BareId+"\"  \n" +
                        "             }  \n" +
                        "         ]  \n" +
                        "     }  \n" +
                        " }";
            }


            JSONObject jsonObject = JSONObject.parseObject(body);
            String result = BaseOkHttpClientUtil.post(url, jsonObject, param);

            return true;


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
