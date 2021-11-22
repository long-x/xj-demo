package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.dto.ProcessVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.alarm.AlarmRequestDTO;
import com.ecdata.cmp.huawei.dto.alarm.CsnsQueryResult;
import com.ecdata.cmp.huawei.dto.alarm.Record;
import com.ecdata.cmp.huawei.dto.response.AlarmListResponse;
import com.ecdata.cmp.huawei.dto.token.OMToken;
import com.ecdata.cmp.huawei.service.AlarmService;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.iaas.client.IaasAlertClient;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.iaas.entity.dto.response.alert.IaasAlertListResponse;
import com.ecdata.cmp.user.client.SysNotificationClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/alart")
@Api(tags = "告警API")
public class AlarmController implements Serializable {
    @Autowired
    private AlarmService alarmService;


    @Autowired
    private IaasAlertClient iaasAlertClient;

    @Autowired
    private SysNotificationClient sysNotificationClient;

    @Autowired
    private ManageOneService manageOneService;


    @GetMapping("/get_alerms")
    @ApiOperation(value = "查询当前告警", notes = "查询当前告警")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "query", dataType = "String")
    })
    public ResponseEntity<AlarmListResponse> getAlermList(@RequestParam(required = true) String token) {
        try {
            List<Record> currentAlarm = alarmService.getCurrentAlarm(token);
            return ResponseEntity.status(HttpStatus.OK).body(new AlarmListResponse(currentAlarm));
        } catch (Exception e) {
            log.info("get Alarm List error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/remove_alarms")
    @ApiOperation(value = "清除指定告警", notes = "清除指定告警")
    public ResponseEntity<LongListResponse> removeAlarmList(@RequestBody AlarmRequestDTO alarmRequestDTO) {
        try {
            List<Long> awaitRemoveIds = alarmRequestDTO.getAwaitRemoveIds();
            ArrayList<Long> alartOfCurrent = new ArrayList<>();
            LongListResponse longListResponse = new LongListResponse();
            if (CollectionUtils.isEmpty(awaitRemoveIds)) {
                //传入的告警号为空
                longListResponse.setCode(401);
                longListResponse.setMessage("告警号不能为空");
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(longListResponse);
            }
            List<Long> longs = new ArrayList<Long>();
            CsnsQueryResult currentAlarmInit = alarmService.getCurrentAlarmInit(alarmRequestDTO.getOmToken());
            List<Long> csnsList = currentAlarmInit.getCsns();
            if (CollectionUtils.isNotEmpty(csnsList)) {
                for (Long awaitRemoveId : awaitRemoveIds) {
                    //筛选出当前告警
                    if (csnsList.contains(awaitRemoveId)) {
                        alartOfCurrent.add(awaitRemoveId);
                    }
                }
            }
            //筛选出的当前告警不为空再删除告警
            if (CollectionUtils.isNotEmpty(alartOfCurrent)) {
                longs = alarmService.removeAlarmsResponse(alarmRequestDTO.getOmToken(), alartOfCurrent);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new LongListResponse(longs));
        } catch (Exception e) {
            log.info("remove Alarm List error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping("/get_alarm")
    @ApiOperation(value = "获取告警", notes = "获取告警")
    public ResponseEntity<BaseResponse> getAlarm() {
        BaseResponse baseResponse = new BaseResponse();
        try {
            OMToken OMToken = manageOneService.getOMToken("ecdataadmin", "Huawei@2020");
            String manageOneToken = OMToken.getAccessSession();
            CsnsQueryResult currentAlarmInit = alarmService.getCurrentAlarmInit(manageOneToken);
            List<Long> csnsList = new ArrayList<>();  // 当前告警CSN
            Set<Long> unhandledCsnsList = new HashSet<>(); // 当前系统未处理CSN

            if (currentAlarmInit != null
                    && CollectionUtils.isNotEmpty(currentAlarmInit.getCsns())) {
                csnsList.addAll(currentAlarmInit.getCsns());
                log.info("csns={}", csnsList);
            }

            IaasAlertListResponse unhandledResponse = iaasAlertClient.fetchUnhandled();
            if (unhandledResponse != null
                    && CollectionUtils.isNotEmpty(unhandledResponse.getData())) {
                List<IaasAlertVO> voList = unhandledResponse.getData();
                unhandledCsnsList.addAll(voList.stream().map(IaasAlertVO::getCsn).collect(Collectors.toList()));
                log.info("unhandled csns={}", unhandledCsnsList);
            }

            List<Long> noticeCnsList = sysNotificationClient.getAllCns();
            if (CollectionUtils.isNotEmpty(noticeCnsList)) {
                unhandledCsnsList.addAll(noticeCnsList);
            }

            // 未处理的cns 减 当前告警 = 要变处理完成状态的
            Collection<Long> handledCollection = CollectionUtils.subtract(unhandledCsnsList, csnsList);
            if (CollectionUtils.isNotEmpty(handledCollection)) {
                log.info("handled collection={}", handledCollection);
                List<Long> handledList = new ArrayList<>(handledCollection);
                //调用iaas接口
                ProcessVO processVO = new ProcessVO();
                processVO.setCsns(handledList);
                processVO.setStatus(2);
                try {
                    iaasAlertClient.updateProcess(processVO);
                    log.info("调用iaas接口更新成功清除的告警");
                } catch (Exception e) {
                    log.info("iaasAlertClient =" + e);
                }

                //调用user接口
                try {
                    sysNotificationClient.updateProcess(processVO);
                    log.info("调用user接口更新成功清除的告警");
                } catch (Exception e) {
                    log.info("sysNotificationClient =" + e);
                }

            }

            // 当前告警 减 未处理的cns = 要新增的
            Collection<Long> addCollection = CollectionUtils.subtract(csnsList, unhandledCsnsList);
            if (CollectionUtils.isNotEmpty(addCollection)) {
                log.info("add collection={}", addCollection);
                List<Long> addList = new ArrayList<>(addCollection);
                // 取详细告警信息
                List<Record> currentAlarmByCsns = alarmService.getCurrentAlarmByCsns(manageOneToken, addList);
                alarmService.addHwAlartToAlart(currentAlarmByCsns);
            }

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("获取华为警告成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } catch (Exception e) {
            log.info("schedule task error" + e);
        }
        baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
        baseResponse.setMessage("获取华为警告失败");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }
}

