package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.SysNotifyManageVO;
import com.ecdata.cmp.user.dto.request.SysNotifyManageRequest;
import com.ecdata.cmp.user.dto.request.SysNotifyManageRequest;
import com.ecdata.cmp.user.dto.response.SysNotifyManagePageResponse;
import com.ecdata.cmp.user.dto.response.SysNotifyManageResponse;
import com.ecdata.cmp.user.entity.SysNotifyManage;
import com.ecdata.cmp.user.service.SysNotifyManageService;
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
@RequestMapping("/v1/sysNotifyManage")
@Api(tags = "下发消息相关接口")
public class SysNotifyManageController {

    @Autowired
    SysNotifyManageService sysNotifyManageService;

    /**
     * 平台/email/短信通知
     * @param notiRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/add/notifymanage_bytpye")
    @ApiOperation(value = "下发消息", notes = "下发消息")
    public ResponseEntity<BaseResponse> addBulletinToUserByType(@Valid @RequestBody SysNotifyManageRequest notiRequest)throws Exception{
        BaseResponse baseResponse = sysNotifyManageService.addSysNotifyManageByType(notiRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }




    @GetMapping("/{id}")
    @ApiOperation(value = "根据id单个通知", notes = "单个通知")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    public ResponseEntity<SysNotifyManageResponse> getSegmentById(@PathVariable(name = "id") Long id) {
        SysNotifyManage notifyManage = sysNotifyManageService.getById(id);

        SysNotifyManageVO notifyManageVO = new SysNotifyManageVO();
        BeanUtils.copyProperties(notifyManage,notifyManageVO);
        return ResponseEntity.status(HttpStatus.OK).body(new SysNotifyManageResponse(notifyManageVO));
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询通知信息", notes = "分页查询通知信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<SysNotifyManagePageResponse> page(
            @RequestParam(defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(defaultValue = "20", required = false) Integer pageSize,
            @RequestParam(required = false) String createTimeStart,
            @RequestParam(required = false) String createTimeEnd,
            @RequestParam(required = false) String keyword){
        Page<SysNotifyManageVO> page = new Page<>(pageNo, pageSize);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId",Sign.getUserId());
        hashMap.put("keyword",keyword);
        hashMap.put("createTimeStart",createTimeStart);
        hashMap.put("createTimeEnd",createTimeEnd);
        //危险 高风险  低风险
        IPage<SysNotifyManageVO> messageVOList = sysNotifyManageService.getNotifyManagePage(page, hashMap);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SysNotifyManagePageResponse(new PageVO<>(messageVOList)));

    }


    @PutMapping("/remove_batch")
    @ApiOperation(value = "批量删除", notes = "批量删除通知")
    public ResponseEntity<BaseResponse> removeRelationBatch(@RequestParam(name = "ids") String ids) {
        log.info("批量删除通知 ids：{}", ids);
        if (StringUtils.isEmpty(ids)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"参数不识别"));
        } else {
            String[] idArray = ids.split(",");
            this.sysNotifyManageService.removeByIds(Arrays.asList(idArray));
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"删除消息成功"));
        }

    }



}
