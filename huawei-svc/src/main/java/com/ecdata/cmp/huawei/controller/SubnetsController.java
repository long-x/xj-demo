package com.ecdata.cmp.huawei.controller;

import com.ecdata.cmp.huawei.dto.response.SubnetsListResponse;
import com.ecdata.cmp.huawei.dto.vo.SubnetsVO;
import com.ecdata.cmp.huawei.service.SubnetsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/4 16:23
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/subnets")
@Api(tags = "子网api")
public class SubnetsController {


    @Autowired
    private SubnetsService subnetsService;


    @GetMapping("/get_subnets")
    @ApiOperation(value = "根据项目ID查询子网列表", notes = "根据项目ID查询子网列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenId", value = "token", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tenantId", value = "项目ID", paramType = "query", dataType = "String")
    })
    public ResponseEntity<SubnetsListResponse> getSubnets(@RequestParam(required = true) String tokenId,
                                                              @RequestParam(required = true) String tenantId) {
        try {
            List<SubnetsVO> images = subnetsService.getSubnets(tokenId,tenantId);
            return ResponseEntity.status(HttpStatus.OK).body(new SubnetsListResponse(images));
        }catch (Exception e){
            log.info("getSubnets error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }



    @GetMapping("/get_subnets_by_vpc_id")
    @ApiOperation(value = "根据vpcID查询子网列表", notes = "根据vpcID查询子网列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "vpcId", value = "vpcId", paramType = "query", dataType = "String")
    })
    public ResponseEntity<SubnetsListResponse> getSubnetsByVpcId(@RequestParam(name = "id",required = false) String id ,
                                                                 @RequestParam(name = "vpcId",required = false) String vpcId ) {
        try {
            List<SubnetsVO> vpcList = subnetsService.getSubnetsByvpc(id);
            //根据vpcId筛选子网列表
            log.info("vpcId ={}",vpcId);
            if(vpcId != null && !"".equals(vpcId)){
                vpcList = vpcList.stream().filter(e -> vpcId.equals(e.getVpcId())).collect(Collectors.toList());
            }
            return ResponseEntity.status(HttpStatus.OK).body(new SubnetsListResponse(vpcList));
        }catch (Exception e){
            log.info("getSubnetsByVpcId error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

}
