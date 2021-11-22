package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.LongListResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.dto.ProcessVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import com.ecdata.cmp.user.dto.SysNotificationVO;
import com.ecdata.cmp.user.dto.request.SysNoteSangforRequest;
import com.ecdata.cmp.user.dto.request.SysNotificationRequest;
import com.ecdata.cmp.user.dto.response.SysNotificationPageResponse;
import com.ecdata.cmp.user.dto.response.SysNotificationResponse;
import com.ecdata.cmp.user.entity.SysNotification;
import com.ecdata.cmp.user.entity.SysNotificationReceiver;
import com.ecdata.cmp.user.entity.SysNotifyManage;
import com.ecdata.cmp.user.entity.User;
import com.ecdata.cmp.user.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-05 17:01
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/sysNotification")
@Api(tags = "消息相关接口")
public class SysNotificationController {

    @Value("${role.name.tenant_admin}")
    private String tenantAdminName;

    @Autowired
    private SysNotificationService sysNotificationService;


    @Autowired
    private SysNotifyManageService sysNotifyManageService;

    @Autowired
    private SysNotificationReceiverService sysNotificationReceiverService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;
/*
    @Autowired
    ImageOfHarborClient imageOfHarborClient;*/

    @PostMapping("/add/notification_bytpye")
    @ApiOperation(value = "根据类型给多用户下发通知", notes = "根据类型给多用户下发通知")
    public ResponseEntity<BaseResponse> addNotifToUserByType(@Valid @RequestBody SysNotificationRequest notiRequest) {
        if (notiRequest.getCreateUser() == null) {
            notiRequest.setCreateUser(Sign.getUserId());
        }

        BaseResponse baseResponse = sysNotificationService.addNotifToUserByType(notiRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/add/batch_notification_bytpye")
    @ApiOperation(value = "批量发送华为警告", notes = "批量发送华为警告")
    public ResponseEntity<BaseResponse> addBatchNotifToUserByType(@Valid @RequestBody List<SysNotificationVO> notifiVOList) {

        try {
            Sign.setCurrentTenantId(10000L);
            /*
            读取配置表，按照告警级别决定以哪种方式发送给哪些人
            从manage表里面读规则，写入notify表
            铃铛站内配的人找不到的话   才用 List<Long> ids = roleService.qryUserIdByRole(Arrays.asList(tenantAdminName));，其它邮箱和短信找不到人不发
             平台   告警级别    去查方式和通知人   1人可能多方式
             */
            Set<Long> set = new HashSet<>();
            //本地租户  默认通知人
            List<Long> ids = roleService.qryUserIdByRole(Arrays.asList(tenantAdminName));

            log.info("size "+notifiVOList.size());
            for (SysNotificationVO notifiVO : notifiVOList) {
                List<Long> userIds = sysNotificationService.addNotifToBell(notifiVO);

//                SysNotificationRequest notiRequest = new SysNotificationRequest();
//                notiRequest.setUserIds(userIds);
//                BeanUtils.copyProperties(notifiVO, notiRequest);
//                BaseResponse baseResponse = sysNotificationService.addNotificationToUser(notiRequest);

//                if (baseResponse.getCode() != ResultEnum.DEFAULT_SUCCESS.getCode()) {
//                    set.add(notiRequest.getCsn());
//                }
            }
//            if (set.size() > 0) {
//                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),
//                        "共有" + set.size() + "条数据发送失败,华为id:" + set.toString()));
//            }

        } catch (Exception e) {
            log.debug("forgetPassword异常" + e);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new BaseResponse(ResultEnum.DEFAULT_FAIL));
        } finally {
            Sign.removeCurrentTenantId();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS));
    }


    @PostMapping("/add/notification")
    @ApiOperation(value = "给多用户下发通知", notes = "给多用户下发通知")
    public ResponseEntity<BaseResponse> addNotificationToUser(@Valid @RequestBody SysNoteSangforRequest sysNoteSangforRequest) {
        sysNoteSangforRequest.setCreateUser(Sign.getUserId());
        BaseResponse baseResponse = sysNotificationService.addNotificationToUser(sysNoteSangforRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增通知", notes = "新增通知")
    public ResponseEntity<BaseResponse> add(@RequestBody SysNotificationVO messageVO) {
        BaseResponse baseResponse = new BaseResponse();

        SysNotification saveMessage = new SysNotification();
        BeanUtils.copyProperties(messageVO, saveMessage);

        saveMessage.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow());
        if (sysNotificationService.save(saveMessage)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加消息成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加消息失败");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseResponse);
        }
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询通知信息", notes = "分页查询通知信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<SysNotificationPageResponse> page(
            @RequestParam(defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(defaultValue = "20", required = false) Integer pageSize,
            @RequestParam(required = false) String createTimeStart,
            @RequestParam(required = false) String createTimeEnd,
            @RequestParam(required = false) String keyword) {
        QueryWrapper<SysNotification> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(createTimeStart)) {
            queryWrapper.lambda().ge(SysNotification::getCreateTime, createTimeStart);
        }

        if (StringUtils.isNotEmpty(createTimeEnd)) {
            queryWrapper.lambda().le(SysNotification::getCreateTime, createTimeEnd);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(SysNotification::getMessage, keyword);
        }
        queryWrapper.lambda().eq(SysNotification::getStatus, 1);
        Page<SysNotification> page = new Page<>(pageNo, pageSize);
        IPage<SysNotification> result = sysNotificationService.page(page, queryWrapper);
        List<SysNotificationVO> sysNoVOList = new ArrayList<>();
        List<SysNotification> notificationList = result.getRecords();

        if (notificationList != null && notificationList.size() > 0) {
            for (SysNotification sysno : notificationList) {

                SysNotificationVO sysnoVO = new SysNotificationVO();
                BeanUtils.copyProperties(sysno, sysnoVO);
                sysNoVOList.add(sysnoVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SysNotificationPageResponse(new PageVO<>(result, sysNoVOList)));

    }

    @PutMapping("/update")
    @ApiOperation(value = "修改通知", notes = "修改通知")
    public ResponseEntity<BaseResponse> update(@RequestBody SysNotificationVO sysNotificationVO) {
        BaseResponse baseResponse = new BaseResponse();
        SysNotification notifi = new SysNotification();
        BeanUtils.copyProperties(sysNotificationVO, notifi);

        Long id = notifi.getId();
        if (id == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseResponse);
        }
        QueryWrapper<SysNotification> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysNotification::getId, id);

        notifi.setUpdateUser(Sign.getUserId());
        notifi.setUpdateTime(DateUtil.getNow());
        if (sysNotificationService.updateById(notifi)) {

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseResponse);
        }
    }


    @PostMapping("/add/userMessage")
    @ApiOperation(value = "新增个人消息", notes = "新增个人消息")
    public ResponseEntity<BaseResponse> addUserMessage(@RequestBody SysNotificationReceiverVO messageVO) {
        BaseResponse baseResponse = new BaseResponse();

        SysNotificationReceiver saveMessage = new SysNotificationReceiver();
        BeanUtils.copyProperties(messageVO, saveMessage);

        saveMessage.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow());
        if (sysNotificationReceiverService.save(saveMessage)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加消息成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加消息失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }


    @GetMapping("/list/userMessage")
    @ApiOperation(value = "查询个人消息列表", notes = "查询个人消息列表")
    @ApiImplicitParam(name = "status", value = "消息状态(1:未读)", paramType = "query")
    public ResponseEntity<SysNotificationResponse> getUserMessageList(@RequestParam(required = false) Integer status) {
        List<SysNotificationVO> messageVOList = sysNotificationService.getUserNotification(Sign.getUserId(), status);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SysNotificationResponse(messageVOList));

    }


    @GetMapping("/page/userMessage")
    @ApiOperation(value = "分页个人消息列表", notes = "分页个人消息列表")
    public ResponseEntity<SysNotificationPageResponse> getUserMessagePage(
            @RequestParam(defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(defaultValue = "20", required = false) Integer pageSize,
            @RequestParam(required = false) Integer sourceType,
            @RequestParam(required = false) String createTimeStart,
            @RequestParam(required = false) String createTimeEnd,
            @RequestParam(required = false) String keyword) {
        Page<SysNotificationVO> page = new Page<>(pageNo, pageSize);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", Sign.getUserId());
        hashMap.put("keyword", keyword);
        hashMap.put("createTimeStart", createTimeStart);
        hashMap.put("createTimeEnd", createTimeEnd);
        hashMap.put("sourceType", sourceType);
        hashMap.put("status", 1);  // 仅查询未处理的信息
        IPage<SysNotificationVO> messageVOList = sysNotificationService.getUserNotificationPage(page, hashMap);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SysNotificationPageResponse(new PageVO<>(messageVOList)));

    }

    @PutMapping("/remove_batch/userMessage")
    @ApiOperation(value = "删除个人消息", notes = "删除个人消息")
    @ApiImplicitParam(name = "ids", value = "ids  以,拼接", dataType = "string", required = true, paramType = "query")
    public ResponseEntity<BaseResponse> removeBatchUserMessage(@RequestParam(required = true) String ids) {
        Integer count = sysNotificationReceiverService.batchRemove(ids, Sign.getUserId());
        if (count != null && count > 0) {
            BaseResponse fail = new BaseResponse(ResultEnum.DEFAULT_FAIL);
            fail.setMessage("共有" + count + "个删除失败");
            return ResponseEntity.status(HttpStatus.OK).body(fail);
        }
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);

    }

    /**
     * 铃铛发起处理华为云和安全云告警 都会走iaas
     * @param id
     * @param remark
     * @return
     */
    @PutMapping("/one_deal/message")
    @ApiOperation(value = "处理单条消息", notes = "处理单条消息")
    public ResponseEntity<BaseResponse> oneDealMessage(@RequestParam(value="id",required = true) Long id,
                                                       @RequestParam(value="remark") String remark) {
        BaseResponse baseResponse = new BaseResponse();
        if (sysNotificationService.oneDealMessage(id, Sign.getUserId(), remark)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("处理成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("处理失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
        }
    }

    @PutMapping("/batch_deal/message")
    @ApiOperation(value = "处理多条消息", notes = "处理多条消息")
    @ApiImplicitParam(name = "ids", value = "ids  以,拼接", dataType = "string", required = true, paramType = "query")
    public ResponseEntity<BaseResponse> datchBealMessage(@RequestParam(required = true) String ids, String remark) {
        Integer count = sysNotificationService.datchBealMessage(ids, Sign.getUserId(), remark);
        if (count != null && count > 0) {
            BaseResponse fail = new BaseResponse(ResultEnum.DEFAULT_FAIL);
            fail.setMessage("共有" + count + "个处理失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fail);
        }
        BaseResponse baseResponse = new BaseResponse(ResultEnum.DEFAULT_SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }


    @PutMapping("/update_process_new")
    @ApiOperation(value = "通知更新处理(新)", notes = "通知更新处理(新)")
    public ResponseEntity<BaseResponse> updateProcess(@RequestBody ProcessVO processVO) {
        return updateProcess(processVO.getCsns(),processVO.getStatus());
    }

    @PutMapping("/update_process")
    @ApiOperation(value = "通知更新处理", notes = "通知更新处理")
    public ResponseEntity<BaseResponse> updateProcess(@RequestParam(name = "csns") List<Long> ids, Integer status) {
        log.info("批量处理 csns：{}", ids);
        if (ids == null || ids.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LongListResponse(ResultEnum.DEFAULT_FAIL, ids));
        }
        LongListResponse listResponse = new LongListResponse();
        try {
            Sign.setCurrentTenantId(10000L);
            //数据库更新失败的csn
            List<Long> failed = new ArrayList<>();

            QueryWrapper<SysNotification> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(SysNotification::getCsn, ids);
            List<SysNotification> list = sysNotificationService.list(queryWrapper);
            if (list == null || list.size() == 0) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new LongListResponse(ResultEnum.DEFAULT_FAIL, ids));
            }
            list = list.stream().map(sysNoti -> {
                sysNoti.setStatus(status);
                sysNoti.setUpdateTime(DateUtil.getNow());
                return sysNoti;
            }).collect(Collectors.toList());
            ;
            if (!this.sysNotificationService.updateBatchById(list)) {
                listResponse = new LongListResponse(ResultEnum.DEFAULT_FAIL, failed);
            }
        } catch (Exception e) {
            log.info("forgetPassword异常" + e);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new LongListResponse(ResultEnum.DEFAULT_FAIL, new ArrayList<>()));
        } finally {
            Sign.removeCurrentTenantId();
        }

        return ResponseEntity.status(HttpStatus.OK).body(listResponse);
    }


    @PostMapping("/read/userMessage")
    @ApiOperation(value = "标记个人消息", notes = "标记个人消息")
    public ResponseEntity<BaseResponse> readUserMessage(String remark) {
        BaseResponse baseResponse = sysNotificationService.updateBatchUserMessageStatus(Sign.getUserId(), remark);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @GetMapping("/get/all/cns")
    @ApiOperation(value = "获取所有未处理的csn", notes = "获取所有未处理的csn")
    public List<Long> getAllCns() {
        return sysNotificationService.getAllCsn();
    }

    /**
     * 由iaas页面发起的安全云告警处理
     * @param id
     * @param remark
     * @return
     */
    @PutMapping("/one_deal/sangfor")
    @ApiOperation(value = "处理单条安全云告警", notes = "处理单条安全云告警")
    public boolean dealSangforNotify(@RequestParam(value="id") Long id, @RequestParam(value="remark") String remark){
        return sysNotificationService.dealSangforNotify(id,Sign.getUserId(),remark);
    }

}
