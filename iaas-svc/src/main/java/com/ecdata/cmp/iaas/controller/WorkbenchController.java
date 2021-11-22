package com.ecdata.cmp.iaas.controller;

import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupResourceCapacityVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupUserVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostApplyCountVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostDistributionPieVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.UserBusinessGroupResourceVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.VirtualMachineCapacityVO;
import com.ecdata.cmp.iaas.service.WorkbenchService;
import com.ecdata.cmp.user.client.SysBusinessGroupClient;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import com.ecdata.cmp.user.dto.response.SysBusinessGroupListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/workbench")
@Api(tags = "工作台相关的API")
public class WorkbenchController {
    @Autowired
    private SysBusinessGroupClient sysBusinessGroupClient;

    @Autowired
    private WorkbenchService workbenchService;

    /**
     * 业务组云主机分布统计
     *
     * @return
     */
    @GetMapping("/host_distribution_pie")
    @ApiOperation(value = "查询业务组云主机分布统计", notes = "查询业务组云主机分布统计")
    public ResponseEntity<HostDistributionPieVO> hostDistributionPie() {
        List<Long> userBusinessGroup = getUserBusinessGroup();
        HostDistributionPieVO pieVO = workbenchService.hostDistributionPieDataMapNew(userBusinessGroup);
        return ResponseEntity.status(HttpStatus.OK).body(pieVO);
    }

    /**
     * 业务组成员数量
     *
     * @return
     */
    @GetMapping("/business_group_user")
    @ApiOperation(value = "查询业务组成员数量", notes = "查询业务组成员数量")
    public ResponseEntity<BusinessGroupUserVO> queryBusinessGroupUserCount() {
        List<Long> userBusinessGroup = getUserBusinessGroup();
        BusinessGroupUserVO userVO = workbenchService.queryBusinessGroupUserCount(userBusinessGroup);
        return ResponseEntity.status(HttpStatus.OK).body(userVO);
    }

    /**
     * 业务组成员数量
     *
     * @return
     */
    @GetMapping("/business_apply_count")
    @ApiOperation(value = "查询业务组成员数量", notes = "查询业务组成员数量")
    public ResponseEntity<List<HostApplyCountVO>> queryBusinessApplyCount() {
        List<Long> userBusinessGroup = getUserBusinessGroup();
        List<HostApplyCountVO> userVO = workbenchService.queryBusinessApplyCount(userBusinessGroup);
        return ResponseEntity.status(HttpStatus.OK).body(userVO);
    }

    /**
     * 业务组资源概况
     *
     * @return
     */
    @GetMapping("/business_group_resource_statistics")
    @ApiOperation(value = "业务组资源概况", notes = "业务组资源概况")
    public ResponseEntity<List<BusinessGroupResourceStatisticsVO>> queryBusinessGroupResourceStatistics() {
        List<Long> userBusinessGroup = getUserBusinessGroup();
        List<BusinessGroupResourceStatisticsVO> userVO = workbenchService.queryBusinessGroupResourceStatistics(userBusinessGroup);
        return ResponseEntity.status(HttpStatus.OK).body(userVO);
    }

    /**
     * 业务组人员资源分布
     *
     * @return
     */
    @GetMapping("/user_business_group_resource_statistics")
    @ApiOperation(value = "业务组人员资源分布", notes = "业务组人员资源分布")
    public ResponseEntity<List<UserBusinessGroupResourceVO>> queryUserBusinessGroupResourceStatistics() {
        List<Long> userBusinessGroup = getUserBusinessGroup();
        List<UserBusinessGroupResourceVO> userVO = workbenchService.queryUserBusinessGroupResourceStatisticsNew(userBusinessGroup);
        return ResponseEntity.status(HttpStatus.OK).body(userVO);
    }

    /**
     * 虚拟化服务器容量统计
     *
     * @return
     */
    @GetMapping("/virtual_machine_capacity")
    @ApiOperation(value = "虚拟化服务器容量统计", notes = "虚拟化服务器容量统计")
    public ResponseEntity<List<VirtualMachineCapacityVO>> queryVirtualMachineCapacity() {
        List<Long> userBusinessGroup = getUserBusinessGroup();
        List<VirtualMachineCapacityVO> userVO = workbenchService.queryVirtualMachineCapacity(userBusinessGroup);
        return ResponseEntity.status(HttpStatus.OK).body(userVO);
    }

    /**
     * 业务资源容量占用最大TOP5
     *
     * @return
     */
    @GetMapping("/business_group_resource_capacity")
    @ApiOperation(value = "业务资源容量占用最大TOP5", notes = "业务资源容量占用最大TOP5")
    public ResponseEntity<BusinessGroupResourceCapacityVO> queryBusinessGroupResourceCapacity() {
        List<Long> userBusinessGroup = getUserBusinessGroup();
        BusinessGroupResourceCapacityVO userVO = workbenchService.queryBusinessGroupResourceCapacity(userBusinessGroup);
        return ResponseEntity.status(HttpStatus.OK).body(userVO);
    }

    private List<Long> getUserBusinessGroup() {
        List<Long> result = new ArrayList<>();
        SysBusinessGroupListResponse response = sysBusinessGroupClient.getBusinessGroupByUser(AuthContext.getAuthz(), String.valueOf(Sign.getUserId()));

        if (response == null || response.getCode() != 0 || CollectionUtils.isEmpty(response.getData())) {
            return result;
        }

        List<SysBusinessGroupVO> list = response.getData();

        result.addAll(list.stream().map(SysBusinessGroupVO::getId).collect(Collectors.toList()));

        return result;
    }
}
