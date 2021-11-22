package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.VdcsListResponse;
import com.ecdata.cmp.huawei.dto.token.TokenDTO;
import com.ecdata.cmp.huawei.dto.vo.VdcsVO;
import com.ecdata.cmp.huawei.service.VdcsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/9 16:36
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/vdcs")
@Api(tags = "VDC-API")
public class VdcsController {

    @Autowired
    private VdcsService vdcsService;

    @PostMapping("/get_vdcs_list")
    @ApiOperation(value = "查询vdc列表",notes = "查询vdc列表")
    public ResponseEntity<VdcsListResponse> getVdcsList(@RequestBody TokenDTO tokenInfos){

        Map map = new HashMap();
        map.put("X-Auth-Token",tokenInfos.getOcToken());
        try {
            List<VdcsVO> voList = vdcsService.getVdcsList(map);
            return ResponseEntity.status(HttpStatus.OK).body(new VdcsListResponse(voList));
        }catch (Exception e){
            log.info("getVdcsList error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }




}
