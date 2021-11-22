package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.alarm.*;
import com.ecdata.cmp.huawei.service.AlarmService;
import com.ecdata.cmp.iaas.client.IaasAlertClient;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("alarmService")
@Slf4j
public class AlarmServiceImpl implements AlarmService {
//
//    @Autowired
//    private SysNotificationClient sysNotificationClient;

    @Autowired
    private IaasAlertClient iaasAlertClient;

    public static final String mediaType = "application/json";
//    /**
//     * json映射
//     */
//    private static final ObjectMapper jsonMapper = new ObjectMapper();

//    @Value("${huawei.ManageOne.mo_url}")
//    private String moUrl;

    @Value("${huawei.ManageOne.om_url}")
    private String omUrl;

    @Override
    public CsnsQueryResult getCurrentAlarmInit(String token) throws IOException {
        String url = omUrl + "/rest/fault/v1/current-alarms/csns";
//        QueryContext queryContext = new QueryContext();
//        QueryContext context = queryContext.setExpression("and");
//        HashMap<String, QueryContext> queryContextMap = new HashMap<>();
//        queryContextMap.put("query",queryContext);

        JSONObject contextItem = new JSONObject();
        contextItem.put("express", "and");
        JSONObject context = new JSONObject();
        context.put("query", contextItem);

//        String queryContextStr = jsonMapper.writeValueAsString(queryContextMap);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token", token);
        String result = BaseOkHttpClientUtil.post(url, context.toJSONString(), headerMap, mediaType);
        return JSONObject.parseObject(result, CsnsQueryResult.class);
    }

    @Override
    public List<Record> getCurrentAlarmByCsns(String token, List<Long> csdses) throws IOException {
        String url = String.format(omUrl + "/rest/fault/v1/current-alarms?csns=%s", StringUtils.join(csdses, "&csns="));
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token", token);
        String result = BaseOkHttpClientUtil.get(url, headerMap);
        return JSONArray.parseArray(result, Record.class);
    }

    @Override
    public List<Record> getCurrentAlarm(String token) throws IOException {
        CsnsQueryResult csnsQueryResult = getCurrentAlarmInit(token);
        return getCurrentAlarmByCsns(token, csnsQueryResult.getCsns());
    }

    @Override
    public BaseResponse addHwAlartToAlart(List<Record> records) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(400);
        List<IaasAlertVO> iaasAlertVOList = new ArrayList<>();
        for (Record record : records) {
            if (record != null && StringUtils.isNotEmpty(record.getAlarmName())) {
                IaasAlertVO iaasAlertVO = new IaasAlertVO();
                iaasAlertVO.setCsn(record.getCsn());
                iaasAlertVO.setResourceName(record.getMeName());
                iaasAlertVO.setAlertLevel(record.getSeverity() + "");
                String alertLevel = iaasAlertVO.getAlertLevel();
                if (StringUtils.isNotEmpty(alertLevel)) {
                    switch (alertLevel) {
                        case "1":
                            iaasAlertVO.setAlertDefinitionName("紧急");
                            break;
                        case "2":
                            iaasAlertVO.setAlertDefinitionName("重要");
                            break;
                        case "3":
                            iaasAlertVO.setAlertDefinitionName("次要");
                            break;
                        case "4":
                            iaasAlertVO.setAlertDefinitionName("提示");
                            break;
                    }
                }
                iaasAlertVO.setSeverity(record.getSeverity());
                iaasAlertVO.setAlarmName(record.getAlarmName());
                iaasAlertVO.setMeName(record.getMeName());
                iaasAlertVO.setMoi(record.getMoi());
                iaasAlertVO.setLatestTime(record.getLatestOccurTime());
                iaasAlertVO.setLogicalRegionName(record.getLogicalRegionName());
                iaasAlertVO.setAdditionalInformation(record.getAdditionalInformation());
                iaasAlertVO.setAlarmId(record.getAlarmId());
                iaasAlertVO.setRuleName(record.getRuleName());
                iaasAlertVO.setAddress(record.getAddress());
                iaasAlertVO.setFirstTime(record.getFirstOccurTime());
                iaasAlertVO.setLogicalRegionId(record.getLogicalRegionId());
                iaasAlertVO.setVdcId(record.getVdcId());
                iaasAlertVO.setVdcName(record.getVdcName());
                iaasAlertVO.setOriginSystem(record.getOriginSystem());
                iaasAlertVO.setOriginSystem(record.getOriginSystemName());
                iaasAlertVOList.add(iaasAlertVO);
            }
        }
        if (CollectionUtils.isNotEmpty(iaasAlertVOList)) {
            baseResponse = iaasAlertClient.addBatch(iaasAlertVOList);
        }
        return baseResponse;
    }

    @Override
    public Response removeAlarms(String token, List<Long> awaitRemoveIds) throws IOException {
        String url = omUrl + "/rest/fault/v1/operation-jobs";
        Long[] arrays = awaitRemoveIds.toArray(new Long[0]);
        CsnsOperate csnsOperate = CsnsOperate.builder()
                .csns(arrays)
                .operator("ecdata_admin")
                .build();
        String jsonString = JSONObject.toJSONString(csnsOperate);
        JSONObject cLearItem = new JSONObject();
        cLearItem.put("CLEAR", jsonString);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("params", cLearItem);
        JSONArray paramArray = new JSONArray();
        paramArray.add(jsonObject);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token", token);
        return BaseOkHttpClientUtil.postResponse(url, paramArray.toJSONString(), headerMap, mediaType);
    }

    @Override
    public List<Long> removeAlarmsResponse(String token, List<Long> awaitRemoveIds) throws Exception {

        ArrayList<Long> failAralmIds = new ArrayList<>();
//        DelayQueue<Message> queue = new DelayQueue<>();
        Response response = removeAlarms(token, awaitRemoveIds);
        if (202==response.code()){
            Thread.sleep(1000);
            CsnsQueryResult currentAlarmInit = getCurrentAlarmInit(token);
            List<Long> csns = currentAlarmInit.getCsns();
            if (CollectionUtils.isNotEmpty(csns)) {
                for (Long removeId : awaitRemoveIds) {
                    if(csns.contains(removeId)){
                        failAralmIds.add(removeId);
                    }
                }
            }
        }
        return failAralmIds;
    }

    @Override
    public List<Record> getHistoryAlarm(String token,Long id) throws IOException {
        String url = omUrl + "/rest/fault/v1/history-alarms?csns="+id;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token", token);
        String result = BaseOkHttpClientUtil.get(url, headerMap);
        return JSONArray.parseArray(result, Record.class);
    }
}
