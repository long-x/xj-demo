package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.huawei.client.HosthistorydataClient;
import com.ecdata.cmp.huawei.client.OauthTokenClient;
import com.ecdata.cmp.huawei.client.VmhistorydataClient;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
import com.ecdata.cmp.huawei.dto.response.VmHistoryDataResponse;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.iaas.service.HosthistoryPerformanceService;
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
 * @date ：Created in 2019/12/17 16:41
 * @modified By：
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HosthistoryPerformanceServiceImpl implements HosthistoryPerformanceService {

    private final HosthistorydataClient hosthistorydataClient;

    @Override
    public List<Map<String, Object>>  getHostHistoryData(RequestVO requestVO) throws IOException {
        String authz = AuthContext.getAuthz();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        requestVO.setOmToken(null);
        try {
            VmHistoryDataResponse vmHistoryCpu = hosthistorydataClient.getHostHistoryCpu(authz, requestVO);
            map.put("cpu",vmHistoryCpu);
            VmHistoryDataResponse vmHistoryMemory = hosthistorydataClient.getHostHistoryMemory(authz, requestVO);
            map.put("memory",vmHistoryMemory);
            VmHistoryDataResponse vmHistoryNetIn = hosthistorydataClient.getHostHistoryNetIn(authz, requestVO);
            map.put("netin",vmHistoryNetIn);
            VmHistoryDataResponse vmHistoryNetOut = hosthistorydataClient.getHostHistoryNetOut(authz, requestVO);
            map.put("netout",vmHistoryNetOut);
            VmHistoryDataResponse vmHistoryDiskwrite = hosthistorydataClient.getHostHistoryDiskwrite(authz, requestVO);
            map.put("diskwrite",vmHistoryDiskwrite);
            VmHistoryDataResponse vmHistoryDiskread = hosthistorydataClient.getHostHistoryDiskread(authz, requestVO);
            map.put("diskread",vmHistoryDiskread);
            list.add(map);
            log.info("查询主机历史性能 {}",list.toString());
        } catch (Exception e) {
            log.info("getHostHistoryData error");
            e.printStackTrace();
        }


        return list;
    }




}
