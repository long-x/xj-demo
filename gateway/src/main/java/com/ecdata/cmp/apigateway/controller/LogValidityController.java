package com.ecdata.cmp.apigateway.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.apigateway.entity.LogValidity;
import com.ecdata.cmp.apigateway.entity.response.LogValidityVO;
import com.ecdata.cmp.apigateway.service.ILogValidity;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/13 14:53
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/log_validity")
@Api(tags = "管理token时效")
public class LogValidityController {

    @Autowired
    private ILogValidity iLogValidity;


    @PostMapping("/add")
    @ApiOperation(value = "新增记录", notes = "新增记录")
    public ResponseEntity<BaseResponse> add(@RequestBody LogValidityVO logValidityVO){
        //响应基础对象
        BaseResponse baseResponse = new BaseResponse();

        LogValidity logValidity = new LogValidity();

        BeanUtils.copyProperties(logValidityVO, logValidity);

        logValidity.setId(SnowFlakeIdGenerator.getInstance().nextId());
        logValidity.setCreateTime(new Date());
        logValidity.setUpdateTime(new Date());
        logValidity.setValidityTime(300l);

        if (iLogValidity.insert(logValidity)==1) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }


    @GetMapping("/update/{accessToken}")
    @ApiOperation(value = "修改记录", notes = "修改记录")
    @ApiImplicitParam(name = "accessToken", value = "accessToken", paramType = "query", dataType = "String")
    public ResponseEntity<BaseResponse> update(@PathVariable("accessToken") String accessToken){
        //响应基础对象
        BaseResponse baseResponse = new BaseResponse();

        LogValidity logValidity = new LogValidity();


        logValidity.setValidityTime(System.currentTimeMillis() / 1000);
        logValidity.setUpdateTime(new Date());
        logValidity.setAccessToken(accessToken);
        QueryWrapper<LogValidity> query = new QueryWrapper<>();
        query.eq("access_token", logValidity.getAccessToken());

        if (iLogValidity.update(logValidity,query)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("修改成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改成功");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }



    @GetMapping("/getOne")
    @ApiOperation(value = "查询记录", notes = "查询记录")
    @ApiImplicitParam(name = "userId", value = "userId", paramType = "query", dataType = "Long")
    public ResponseEntity<BaseResponse> update(@RequestParam(value = "userId") Long userId){
        //响应基础对象
        BaseResponse baseResponse = new BaseResponse();
        Long exist = iLogValidity.isExist(userId);
        if (exist >= 1){
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);

    }




}
