package com.ecdata.cmp.user.controller;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.user.dto.ResWorkorderVO;
import com.ecdata.cmp.user.entity.Dictionary;
import com.ecdata.cmp.user.entity.ResDictionary;
import com.ecdata.cmp.user.service.IResWorkorderService;
import com.ecdata.cmp.user.service.ResDictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;

/**
 * @author ZhaoYX
 * @since 2020/1/13 14:06,
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/dict")
@Api(tags = "字典的API")
public class ResDictionaryController {

    @Autowired
    ResDictionaryService dictionaryService;

    @Autowired
    IResWorkorderService resWorkorderService;

    @PostMapping("/add")
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseEntity<BaseResponse> add(@RequestBody ResWorkorderVO resWorkorderVo) {
        Field[] columns = resWorkorderVo.getClass().getDeclaredFields();
        for(Field col:columns){
            if(!col.isAccessible())
                col.setAccessible(true);
            ApiModelProperty property = col.getAnnotation(ApiModelProperty.class);
//            property.value()

        }
        return null;

    }
}
