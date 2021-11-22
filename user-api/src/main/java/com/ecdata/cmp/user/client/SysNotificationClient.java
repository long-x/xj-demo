package com.ecdata.cmp.user.client;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.common.dto.ProcessVO;
import com.ecdata.cmp.user.UserConstant;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import com.ecdata.cmp.user.dto.SysNotificationVO;
import com.ecdata.cmp.user.dto.request.SysNoteSangforRequest;
import com.ecdata.cmp.user.dto.request.SysNotificationRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-09-25
 */
@FeignClient(value = UserConstant.SERVICE_NAME, path = "/v1/sysNotification")
public interface SysNotificationClient {

    @PostMapping(path = "/add")
    BaseResponse add(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                            @RequestBody SysNotificationVO messageVO);

    @PostMapping("/add/userMessage")
    BaseResponse addUserMessage(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
            @RequestBody SysNotificationReceiverVO messageVO);

    @PostMapping("/add/notification")
    BaseResponse addNotificationToUser(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                              @RequestBody SysNoteSangforRequest sysNoteSangforRequest);

    @PostMapping("/add/notification_bytpye")
    BaseResponse addNotifToUserByType(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) String token,
                                       @RequestBody SysNotificationRequest sysNotificationRequest);

    @PostMapping("/add/batch_notification_bytpye")
    BaseResponse addBatchNotifToUserByType(@RequestBody List<SysNotificationVO> notifiVOList);

    @PutMapping("/update_process")
    @ApiOperation(value = "通知更新处理", notes = "通知更新处理")
    ResponseEntity<BaseResponse> updateProcess(@RequestParam(name = "csns") List<Long> ids,
                                                      @RequestParam(name ="status")Integer status);


    @PutMapping("/update_process_new")
    @ApiOperation(value = "通知更新处理", notes = "通知更新处理")
    ResponseEntity<BaseResponse> updateProcess(@RequestBody ProcessVO processVO);

    @GetMapping("/get/all/cns")
    @ApiOperation(value = "获取所有未处理的csn", notes = "获取所有未处理的csn")
    List<Long> getAllCns();


    @PutMapping("/one_deal/message")
    @ApiOperation(value = "处理单条消息", notes = "处理单条消息")
    ResponseEntity<BaseResponse> oneDealMessage(@RequestParam(value="id",required = true) Long id
            ,@RequestParam(value="remark") String remark);

    @PutMapping("/one_deal/sangfor")
    @ApiOperation(value = "处理单条安全云告警", notes = "处理单条安全云告警")
    boolean dealSangforNotify(@RequestParam(value="id") Long id, @RequestParam(value="remark") String remark);
}
