package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.huawei.client.OauthTokenClient;
import com.ecdata.cmp.huawei.client.VmhistorydataClient;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
import com.ecdata.cmp.huawei.dto.response.VmHistoryDataResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.iaas.service.VmhistoryPerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/14 11:41
 * @modified By：
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VmhistoryPerformanceServiceImpl implements VmhistoryPerformanceService{

    private final VmhistorydataClient vmhistorydataClient;
    private final OauthTokenClient oauthTokenClient;

    @Override
    public List<Map<String, Object>>  getVmHistoryData(RequestVO requestVO) throws IOException {
        String authz = AuthContext.getAuthz();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        //获取token
//        TokenInfoResponse tokenResponse = getToken(authz, requestVO.getUsername(), requestVO.getPassword());
        requestVO.setOmToken(null);
        try {
            VmHistoryDataResponse vmHistoryCpu = vmhistorydataClient.getVmHistoryCpu(authz, requestVO);
            map.put("cpu",vmHistoryCpu);
            VmHistoryDataResponse vmHistoryMemory = vmhistorydataClient.getVmHistoryMemory(authz, requestVO);
            map.put("memory",vmHistoryMemory);
            VmHistoryDataResponse vmHistoryNetIn = vmhistorydataClient.getVmHistoryNetIn(authz, requestVO);
            map.put("netin",vmHistoryNetIn);
            VmHistoryDataResponse vmHistoryNetOut = vmhistorydataClient.getVmHistoryNetOut(authz, requestVO);
            map.put("netout",vmHistoryNetOut);
            VmHistoryDataResponse vmHistoryDiskwrite = vmhistorydataClient.getVmHistoryDiskwrite(authz, requestVO);
            map.put("diskwrite",vmHistoryDiskwrite);
            VmHistoryDataResponse vmHistoryDiskread = vmhistorydataClient.getVmHistoryDiskread(authz, requestVO);
            map.put("diskread",vmHistoryDiskread);
            list.add(map);
            log.info("查询虚拟机历史性能 {}",list.toString());
        } catch (Exception e) {
            log.info("getVmHistoryData error");
            e.printStackTrace();
        }


        return list;
    }



    private TokenInfoResponse getToken(String authz, String username, String password) {
        TokenInfoResponse tokenResponse = null;
        try {
            tokenResponse = oauthTokenClient.getTokenByUser(authz,
                    "",
                    "domainName",
                    username,
                    password);

            if (tokenResponse.getCode() != 0 || tokenResponse.getData() == null) {
                return null;
            }
        } catch (Exception e) {
            log.error("获取供应商token错误!", e);
            return null;
        }

        return tokenResponse;
    }


}
