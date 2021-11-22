package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.huawei.dto.response.VpcListResponse;
import com.ecdata.cmp.huawei.dto.token.TokenVdcVO;
import com.ecdata.cmp.huawei.dto.vo.VpcVO;
import com.ecdata.cmp.huawei.service.ManageOneService;
import com.ecdata.cmp.huawei.service.VpcService;
import com.ecdata.cmp.iaas.client.VirtualDataCenterClient;
import com.ecdata.cmp.iaas.entity.dto.response.VirtualDataCenterResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/26 22:14
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/vpc")
@Api(tags = "VPC虚拟私有云api")
public class VpcController {


    @Autowired
    private VpcService vpcService;


    @GetMapping("/get_vpclist")
    @ApiOperation(value = "根据项目id查询vpc列表", notes = "根据项目id查询vpc列表")
    @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "string")
    public ResponseEntity<VpcListResponse> getVpclist(@RequestParam(name = "id",required = false) String id) {
        try {

            List<VpcVO> vpcList = vpcService.getVpcList(id);
            return ResponseEntity.status(HttpStatus.OK).body(new VpcListResponse(vpcList));

        }catch (Exception e){
            log.info("getVpclist error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }



}
