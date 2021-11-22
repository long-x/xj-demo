package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.iaas.entity.*;
import com.ecdata.cmp.iaas.entity.dto.*;
import com.ecdata.cmp.iaas.entity.dto.response.*;
import com.ecdata.cmp.iaas.service.*;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @title: large center
 * @Author: shig
 * @description: 大屏中间 控制层
 * @Date: 2019/12/11 11:37 上午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/large")
@Api(tags = "🐙大屏 相关的API")
public class LargeCenterController {
    /**
     * temp_cloud_data_center (中:上)
     */
    @Autowired
    private ITempCloudDataCenterService tempCloudDataCenterService;

    /**
     * temp_global_resource (中:下)
     */
    @Autowired
    private ITempGlobalResourceService tempGlobalResourceService;

    /**
     * 数据中心资源总览临时表:temp_data_center_resource_overview (左:上)
     */
    @Autowired
    private ITempDataCenterResourceOverviewService tempDataCenterResourceOverviewService;

    /**
     * 宿主机资源使用率情况:temp_host_resource_used (左:中1)
     */
    @Autowired
    private ITempHostResourceUsedService tempHostResourceUsedService;

    /**
     * 业务系统资源使用情况临时表:temp_business_system_resource_used (左:中1)
     */
    @Autowired
    private ITempBusinessSystemResourceUsedService tempBusinessSystemResourceUsedService;

    /**
     * 云服务发放统计临时表:temp_cloud_server_provide_statistics (左:中2)
     */
    @Autowired
    private ITempCloudServerProvideStatisticsService tempCloudServerProvideStatisticsService;

    /**
     * 安全云安全资源一览临时表:temp_security_cloud_security_resource_overview （右:上)
     */
    @Autowired
    private ITempSecurityCloudSecurityResourceOverviewService tempSecurityCloudSecurityResourceOverviewService;

    /**
     * 安全云安全事件概况临时表:temp_security_cloud_security_event_overview （右:中1)
     */
    @Autowired
    private ITempSecurityCloudSecurityEventOverviewService tempSecurityCloudSecurityEventOverviewService;

    /**
     * 集团各组织资源使用情况临时表：temp_group_organization_resource_used （右:中2)
     */
    @Autowired
    private ITempGroupOrganizationResourceUsedService tempGroupOrganizationResourceUsedService;

    /**
     * 华为云及安全云告警情况临时表:temp_huawei_cloud_security_cloud_alert_condition（右:中3)
     */
    @Autowired
    private ITempHuaweiCloudSecurityCloudAlertConditionService tempHuaweiCloudSecurityCloudAlertConditionService;

    /**
     * 安全云30天内已处置的事件临时表 :temp_security_cloud_thirty_days_disposed_event （右:下)
     */
    @Autowired
    private ITempSecurityCloudThirtyDaysDisposedEventService tempSecurityCloudThirtyDaysDisposedEventService;


    @GetMapping("/getTempCloudDataCenterList")
    @ApiOperation(value = "云数据中心临时表列表 (中:上)", notes = "云数据中心临时表列表 (中:上)")
    public ResponseEntity<TempCloudDataCenterListResponse> getTempCloudDataCenterList() {
        int numTotal = 6;
        List<TempCloudDataCenterVO> tempCloudDataCenterVOList = new ArrayList<>();
        List<TempCloudDataCenter> tempCloudDataCenters = tempCloudDataCenterService.list();
        TempCloudDataCenterVO tempCloudDataCenterVO = null;
        if (tempCloudDataCenters.size() > 0) {
            //sort:从小到大排序
            Collections.sort(tempCloudDataCenters, Comparator.comparing(TempCloudDataCenter::getSort));

            for (TempCloudDataCenter cloudDataCenter : tempCloudDataCenters) {
                tempCloudDataCenterVO = new TempCloudDataCenterVO();
                BeanUtils.copyProperties(cloudDataCenter, tempCloudDataCenterVO);
                tempCloudDataCenterVOList.add(tempCloudDataCenterVO);
            }
        }
        if (tempCloudDataCenterVOList.size() < numTotal) {
            for (int i = tempCloudDataCenterVOList.size(); i < numTotal; i++) {
                tempCloudDataCenterVO = new TempCloudDataCenterVO();
                tempCloudDataCenterVO.setId(Long.valueOf(tempCloudDataCenterVOList.size() + 1));
                tempCloudDataCenterVO.setCdcName("暂未开放");
                tempCloudDataCenterVO.setCpuRate(0.0);
                tempCloudDataCenterVO.setMemoryRate(0.0);
                tempCloudDataCenterVO.setStorageRate(0.0);
                tempCloudDataCenterVO.setSort(tempCloudDataCenterVOList.size() + 1);
                tempCloudDataCenterVO.setIsDeleted(false);
                tempCloudDataCenterVOList.add(tempCloudDataCenterVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TempCloudDataCenterListResponse(tempCloudDataCenterVOList));
    }

    @GetMapping("/getTempGlobalResourceInfo")
    @ApiOperation(value = "整体资源临时表信息 (中:下)", notes = "整体资源临时表信息 (中:下)")
    public ResponseEntity<TempGlobalResourceResponse> getTempGlobalResourceInfo() {
        TempGlobalResourceResponse tempGlobalResourceResponse = new TempGlobalResourceResponse();
        TempGlobalResourceVO tempGlobalResourceVO = new TempGlobalResourceVO();
        List<TempGlobalResource> list = tempGlobalResourceService.list();
        if (list.size() == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(tempGlobalResourceResponse);
        }
        BeanUtils.copyProperties(list.get(0), tempGlobalResourceVO);
        tempGlobalResourceResponse.setData(tempGlobalResourceVO);
        return ResponseEntity.status(HttpStatus.OK).body(tempGlobalResourceResponse);
    }


    @GetMapping("/getTempDataCenterResourceOverviewInfo")
    @ApiOperation(value = "数据中心资源总览 (左:上)", notes = "数据中心资源总览 (左:上)")
    public ResponseEntity<TempDataCenterResourceOverviewResponse> getTempDataCenterResourceOverviewInfo() {
        TempDataCenterResourceOverviewResponse tempDataCenterResourceOverviewResponse = new TempDataCenterResourceOverviewResponse();
        TempDataCenterResourceOverviewVO tempDataCenterResourceOverviewVO = new TempDataCenterResourceOverviewVO();
        List<TempDataCenterResourceOverview> list = tempDataCenterResourceOverviewService.list();
        if (list.size() == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(tempDataCenterResourceOverviewResponse);
        }
        BeanUtils.copyProperties(list.get(0), tempDataCenterResourceOverviewVO);
        tempDataCenterResourceOverviewResponse.setData(tempDataCenterResourceOverviewVO);
        return ResponseEntity.status(HttpStatus.OK).body(tempDataCenterResourceOverviewResponse);
    }

    @GetMapping("/getTempHostResourceUsedList")
    @ApiOperation(value = "宿主机资源使用率情况 (左:中1)", notes = "宿主机资源使用率情况 (左:中1)")
    public ResponseEntity<TempHostResourceUsedListResponse> getTempHostResourceUsedList() {
        List<TempHostResourceUsedVO> tempHostResourceUsedVOList = new ArrayList<>();
        List<TempHostResourceUsed> tempHostResourceUseds = tempHostResourceUsedService.list();
        TempHostResourceUsedVO tempHostResourceUsedVO = null;
        if (tempHostResourceUseds.size() > 0) {
            for (TempHostResourceUsed resourceUsed : tempHostResourceUseds) {
                tempHostResourceUsedVO = new TempHostResourceUsedVO();
                BeanUtils.copyProperties(resourceUsed, tempHostResourceUsedVO);
                tempHostResourceUsedVOList.add(tempHostResourceUsedVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TempHostResourceUsedListResponse(tempHostResourceUsedVOList));
    }


    @GetMapping("/getTempBusinessSystemResourceUsedList")
    @ApiOperation(value = "业务系统资源使用情况 (左:中2)", notes = "业务系统资源使用情况 (左:中2)")
    public ResponseEntity<TempBusinessSystemResourceUsedListResponse> getTempBusinessSystemResourceUsedList() {
        List<TempBusinessSystemResourceUsedVO> tempBusinessSystemResourceUsedVOList = new ArrayList<>();
        List<TempBusinessSystemResourceUsed> tempBusinessSystemResourceUseds = tempBusinessSystemResourceUsedService.list();
        TempBusinessSystemResourceUsedVO systemResourceUsedVO = null;
        if (tempBusinessSystemResourceUseds.size() > 0) {
            for (TempBusinessSystemResourceUsed systemResourceUsed : tempBusinessSystemResourceUseds) {
                systemResourceUsedVO = new TempBusinessSystemResourceUsedVO();
                BeanUtils.copyProperties(systemResourceUsed, systemResourceUsedVO);
                tempBusinessSystemResourceUsedVOList.add(systemResourceUsedVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TempBusinessSystemResourceUsedListResponse(tempBusinessSystemResourceUsedVOList));
    }

    @GetMapping("/getTempCloudServerProvideStatisticsList")
    @ApiOperation(value = "云服务和安全类资源 发放统计", notes = "云服务和安全类资源 发放统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataType", value = "数据类型:1:云资源发放统计,2:安全类资源发放统计", paramType = "query", dataType = "int", defaultValue = "1")
    })
    public ResponseEntity<TempCloudServerProvideStatisticsListResponse> getTempCloudServerProvideStatisticsList(@RequestParam(defaultValue = "1", required = true) Integer dataType) {
        List<TempCloudServerProvideStatisticsVO> tempCloudServerProvideStatisticsVOList = new ArrayList<>();
        QueryWrapper<TempCloudServerProvideStatistics> query = new QueryWrapper<>();
        query.eq("data_type", dataType);
        query.eq("is_deleted", 0);
        List<TempCloudServerProvideStatistics> tempCloudServerProvideStatisticss = tempCloudServerProvideStatisticsService.list(query);
        TempCloudServerProvideStatisticsVO tempCloudServerProvideStatisticsVO = null;
        if (tempCloudServerProvideStatisticss.size() > 0) {
            for (TempCloudServerProvideStatistics provideStatistics : tempCloudServerProvideStatisticss) {
                tempCloudServerProvideStatisticsVO = new TempCloudServerProvideStatisticsVO();
                BeanUtils.copyProperties(provideStatistics, tempCloudServerProvideStatisticsVO);
                tempCloudServerProvideStatisticsVOList.add(tempCloudServerProvideStatisticsVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TempCloudServerProvideStatisticsListResponse(tempCloudServerProvideStatisticsVOList));
    }


    @GetMapping("/getTempSecurityCloudSecurityResourceOverviewInfo")
    @ApiOperation(value = "安全云安全资源一览 (右:上)", notes = "数据中心资源总览 (右:上)")
    public ResponseEntity<TempSecurityCloudSecurityResourceOverviewResponse> getTempSecurityCloudSecurityResourceOverviewInfo() {
        TempSecurityCloudSecurityResourceOverviewResponse tempDataCenterResourceOverviewResponse = new TempSecurityCloudSecurityResourceOverviewResponse();
        TempSecurityCloudSecurityResourceOverviewVO tempSecurityCloudSecurityResourceOverviewVO = new TempSecurityCloudSecurityResourceOverviewVO();
        List<TempSecurityCloudSecurityResourceOverview> list = tempSecurityCloudSecurityResourceOverviewService.list();
        if (list.size() == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(tempDataCenterResourceOverviewResponse);
        }
        BeanUtils.copyProperties(list.get(0), tempSecurityCloudSecurityResourceOverviewVO);
        tempDataCenterResourceOverviewResponse.setData(tempSecurityCloudSecurityResourceOverviewVO);
        return ResponseEntity.status(HttpStatus.OK).body(tempDataCenterResourceOverviewResponse);
    }

    @GetMapping("/getTempSecurityCloudSecurityEventOverviewList")
    @ApiOperation(value = "安全云安全事件概况 (右:中1)", notes = "安全云安全事件概况 (右:中1)")
    public ResponseEntity<TempSecurityCloudSecurityEventOverviewListResponse> getTempSecurityCloudSecurityEventOverviewList() {
        List<TempSecurityCloudSecurityEventOverviewVO> tempSecurityCloudSecurityEventOverviewVOList = new ArrayList<>();
        List<TempSecurityCloudSecurityEventOverview> tempSecurityCloudSecurityEventOverviews = tempSecurityCloudSecurityEventOverviewService.list();
        TempSecurityCloudSecurityEventOverviewVO cloudSecurityEventOverviewVO = null;
        if (tempSecurityCloudSecurityEventOverviews.size() > 0) {
            for (TempSecurityCloudSecurityEventOverview provideStatistics : tempSecurityCloudSecurityEventOverviews) {
                cloudSecurityEventOverviewVO = new TempSecurityCloudSecurityEventOverviewVO();
                BeanUtils.copyProperties(provideStatistics, cloudSecurityEventOverviewVO);
                tempSecurityCloudSecurityEventOverviewVOList.add(cloudSecurityEventOverviewVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TempSecurityCloudSecurityEventOverviewListResponse(tempSecurityCloudSecurityEventOverviewVOList));
    }


    @GetMapping("/getTempGroupOrganizationResourceUsedList")
    @ApiOperation(value = "集团各组织资源使用情况 (右:中2)", notes = "集团各组织资源使用情况 (右:中2)")
    public ResponseEntity<TempGroupOrganizationResourceUsedListResponse> getTempGroupOrganizationResourceUsedList() {
        List<TempGroupOrganizationResourceUsedVO> tempSecurityCloudSecurityEventOverviewVOList = new ArrayList<>();
        List<TempGroupOrganizationResourceUsed> tempSecurityCloudSecurityEventOverviews = tempGroupOrganizationResourceUsedService.list();
        TempGroupOrganizationResourceUsedVO tempGroupOrganizationResourceUsedVO = null;
        if (tempSecurityCloudSecurityEventOverviews.size() > 0) {
            for (TempGroupOrganizationResourceUsed organizationResourceUsed : tempSecurityCloudSecurityEventOverviews) {
                tempGroupOrganizationResourceUsedVO = new TempGroupOrganizationResourceUsedVO();
                BeanUtils.copyProperties(organizationResourceUsed, tempGroupOrganizationResourceUsedVO);
                tempSecurityCloudSecurityEventOverviewVOList.add(tempGroupOrganizationResourceUsedVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TempGroupOrganizationResourceUsedListResponse(tempSecurityCloudSecurityEventOverviewVOList));
    }

    @GetMapping("/getHuaweiCloudSecurityCloudAlertList")
    @ApiOperation(value = "华为云告警情况 (右:中3)", notes = "数据中心资源总览 (右:中3)")
    public ResponseEntity<TempHuaweiCloudSecurityCloudAlertConditionListResponse> getHuaweiCloudSecurityCloudAlertList() {
        List<TempHuaweiCloudSecurityCloudAlertConditionVO> tempHuaweiCloudSecurityCloudAlertConditionVOS = new ArrayList<>();
        List<TempHuaweiCloudSecurityCloudAlertCondition> huaweiCloudSecurityCloudAlertConditions = tempHuaweiCloudSecurityCloudAlertConditionService.list();
        TempHuaweiCloudSecurityCloudAlertConditionVO huaweiCloudSecurityCloudAlertConditionVO = null;
        if (huaweiCloudSecurityCloudAlertConditions.size() > 0) {
            for (TempHuaweiCloudSecurityCloudAlertCondition huaweiCloudSecurityCloudAlertCondition : huaweiCloudSecurityCloudAlertConditions) {
                huaweiCloudSecurityCloudAlertConditionVO = new TempHuaweiCloudSecurityCloudAlertConditionVO();
                BeanUtils.copyProperties(huaweiCloudSecurityCloudAlertCondition, huaweiCloudSecurityCloudAlertConditionVO);
                tempHuaweiCloudSecurityCloudAlertConditionVOS.add(huaweiCloudSecurityCloudAlertConditionVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TempHuaweiCloudSecurityCloudAlertConditionListResponse(tempHuaweiCloudSecurityCloudAlertConditionVOS));
    }

    @GetMapping("/getTempSecurityCloudThirtyDaysDisposedEventList")
    @ApiOperation(value = "安全云30天已处置事件 (右:下)", notes = "安全云30天已处置事件 (右:下)")
    public ResponseEntity<TempSecurityCloudThirtyDaysDisposedEventListResponse> getTempSecurityCloudThirtyDaysDisposedEventList() {
        List<TempSecurityCloudThirtyDaysDisposedEventVO> tempSecurityCloudThirtyDaysDisposedEventVOList = new ArrayList<>();
        List<TempSecurityCloudThirtyDaysDisposedEvent> tempSecurityCloudThirtyDaysDisposedEvents = tempSecurityCloudThirtyDaysDisposedEventService.list();
        TempSecurityCloudThirtyDaysDisposedEventVO securityCloudThirtyDaysDisposedEventVO = null;
        if (tempSecurityCloudThirtyDaysDisposedEvents.size() > 0) {
            for (TempSecurityCloudThirtyDaysDisposedEvent thirtyDaysDisposedEvent : tempSecurityCloudThirtyDaysDisposedEvents) {
                securityCloudThirtyDaysDisposedEventVO = new TempSecurityCloudThirtyDaysDisposedEventVO();
                BeanUtils.copyProperties(thirtyDaysDisposedEvent, securityCloudThirtyDaysDisposedEventVO);
                tempSecurityCloudThirtyDaysDisposedEventVOList.add(securityCloudThirtyDaysDisposedEventVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TempSecurityCloudThirtyDaysDisposedEventListResponse(tempSecurityCloudThirtyDaysDisposedEventVOList));
    }

}
