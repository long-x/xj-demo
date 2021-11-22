package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.huawei.dto.alarm.CsnsQueryResult;
import com.ecdata.cmp.huawei.dto.alarm.Record;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public interface AlarmService {

    /**
     * 当前告警初始查询
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    CsnsQueryResult getCurrentAlarmInit(String token) throws IOException;

//    /**
//     * 根据OccurUtcTime当前告警初始查询
//     *
//     * @param token token信息
//     * @return TokenInfoVO
//     * @throws IOException io异常
//     */
//    CsnsQueryResult getCurrentAlarmByOccurUtcTime(String token,Long occurUtcTime) throws IOException;

    /**
     * 根据csds查询告警信息
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<Record> getCurrentAlarmByCsns(String token, List<Long> csdses) throws IOException;

    /**
     * 查询当前告警信息
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<Record> getCurrentAlarm(String token) throws IOException;

//    /**
//     * 当前告警信息插入notification
//     *
//     * @param record token信息
//     * @return TokenInfoVO
//     * @throws IOException io异常
//     */
//    void addHwAlartToNotify(List<Record> record) throws IOException;
    /**
     * 当前告警信息插入alert
     *
     * @param record token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    BaseResponse addHwAlartToAlart(List<Record> record) throws IOException;

    /**
     * 清除指定告警信息
     *
     * @param awaitRemoveIds token信息
     * @return Response
     * @throws IOException io异常
     */
    Response removeAlarms(String token, List<Long> awaitRemoveIds) throws IOException;

    /**
     * 清除指定告警，返回清除未成功的告警
     *
     * @param awaitRemoveIds token信息
     * @return Response
     * @throws IOException io异常
     */
    List<Long> removeAlarmsResponse(String token, List<Long> awaitRemoveIds) throws Exception;

    /**
     * 根据历史告警id查询告警
     *
     * @param token token信息
     * @return TokenInfoVO
     * @throws IOException io异常
     */
    List<Record> getHistoryAlarm(String token,Long id) throws IOException;
}
