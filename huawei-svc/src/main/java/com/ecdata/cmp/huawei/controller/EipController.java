package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.EipListResponse;
import com.ecdata.cmp.huawei.dto.vo.EipVO;
import com.ecdata.cmp.huawei.service.EipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/10/19 16:46
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/eip")
@Api(tags = "Eip相关api")
public class EipController {


    @Autowired
    private EipService eipService;

    @GetMapping("/get_eiplist")
    @ApiOperation(value = "根据项目id查询可用弹性云ip", notes = "根据项目id查询可用弹性云ip")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "string")
    public ResponseEntity<EipListResponse> getVpclist(@RequestParam(name = "id",required = false) String id) {
        try {
            List<EipVO> eipList = eipService.findEip(id);
            return ResponseEntity.status(HttpStatus.OK).body(new EipListResponse(eipList));
        }catch (Exception e){
            log.info("getEiplist error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}
