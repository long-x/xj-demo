package com.ecdata.cmp.huawei.client;

import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.huawei.ManageOneConstant;
import com.ecdata.cmp.huawei.dto.alarm.AlarmRequestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@FeignClient(value = ManageOneConstant.SERVICE_NAME, path = "/v1/alart")
public interface AlarmClient {

    @PostMapping(path = "/remove_alarms")
    @ApiOperation(value = "根据告警ID清除告警", notes = "根据告警ID清除告警")
    LongListResponse removeAlarmList(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String authorization,
                                     @RequestBody AlarmRequestDTO alarmRequestDTO) throws IOException;
}
