package com.ecdata.cmp.iaas.controller;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.entity.IaasVirtualDataCenter;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasVirtualDataCenterResponse;
import com.ecdata.cmp.iaas.entity.dto.response.VirtualDataCenterListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.VirtualDataCenterResponse;
import com.ecdata.cmp.iaas.service.IVirtualDataCenterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: IaasVirtualDataCenter
 * @Author: shig
 * @description: vdc 控制层
 * @Date: 2019/12/13 5:05 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/virtualDataCenter")
@Api(tags = "🦁虚拟数据中心表(vdc) 相关的API")
public class IaasVirtualDataCenterController {
    @Autowired
    private IVirtualDataCenterService virtualDataCenterService;


    @GetMapping("/getVdcNameByProviderId")
    @ApiOperation(value = "根据供应商id查询vdc名称", notes = "根据供应商id查询vdc名称")
    public ResponseEntity<VirtualDataCenterListResponse> disProviderName(@RequestParam(required = true) Long providerId) {
        //去重查询
        List<IaasVirtualDataCenter> virtualDataCenters = virtualDataCenterService.getVdcNameByProviderId(providerId);
        //响应对象
        List<IaasVirtualDataCenterVO> iaasVirtualDataCenterVOList = new ArrayList<>();
        //不为空，复制到拓展类
        if (virtualDataCenters != null && virtualDataCenters.size() > 0) {
            for (IaasVirtualDataCenter virtualDataCenter : virtualDataCenters) {
                IaasVirtualDataCenterVO virtualDataCenterVO = new IaasVirtualDataCenterVO();
                BeanUtils.copyProperties(virtualDataCenter, virtualDataCenterVO);
                iaasVirtualDataCenterVOList.add(virtualDataCenterVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new VirtualDataCenterListResponse(iaasVirtualDataCenterVOList));
    }

    @GetMapping("/info")
    @ApiOperation(value = "根据vdc id查询", notes = "根据vdc id查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<IaasVirtualDataCenterResponse> info(@RequestParam(required = true) Long id) {
        IaasVirtualDataCenterResponse iaasVirtualDataCenterResponse = new IaasVirtualDataCenterResponse();
        IaasVirtualDataCenterVO virtualDataCenterVO = new IaasVirtualDataCenterVO();

        //查询条件
        IaasVirtualDataCenter iaasVirtualDataCenter = new IaasVirtualDataCenter();
        if (id == null) {
            id = Sign.getUserId();
        }
        iaasVirtualDataCenter.setId(id);
        //查询改id是否存在
        IaasVirtualDataCenter virtualDataCenter = virtualDataCenterService.getById(id);
        if (virtualDataCenter == null) {
            return ResponseEntity.status(HttpStatus.OK).body(iaasVirtualDataCenterResponse);
        }
        BeanUtils.copyProperties(virtualDataCenter, virtualDataCenterVO);
        iaasVirtualDataCenterResponse.setData(virtualDataCenterVO);
        return ResponseEntity.status(HttpStatus.OK).body(iaasVirtualDataCenterResponse);
    }



    @GetMapping("/get_vdc_by_id")
    @ApiOperation(value = "根据供应商id查询vdc名称", notes = "根据供应商id查询vdc名称")
    public ResponseEntity<VirtualDataCenterResponse> getVdcById(@RequestParam(name = "id",required = false) String id) {
        IaasVirtualDataCenterVO dataCenterVO = virtualDataCenterService.selectVdcById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new VirtualDataCenterResponse(dataCenterVO));
    }


}