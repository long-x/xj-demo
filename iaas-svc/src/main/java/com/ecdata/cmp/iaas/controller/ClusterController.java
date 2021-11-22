package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.IaasCluster;
import com.ecdata.cmp.iaas.entity.IaasProvider;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasClusterListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasClusterPageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasProviderListResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasProviderPageResponse;
import com.ecdata.cmp.iaas.service.IClusterService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: ClusterController controller
 * @Author: shig
 * @description: 集群 控制层
 * @Date: 2019/11/18 4:01 下午
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/cluster")
@Api(tags = "🦅集群 相关的API")
public class ClusterController {
    /**
     * 集群(cluster) service
     */
    @Autowired
    private IClusterService clusterService;

    /**
     * 根据集群id获取集群名称
     *
     * @param providerId
     * @return
     */
    @GetMapping("/getClusterNameByProviderId")
    @ApiOperation(value = "根据供应商id获取集群名称", notes = "根据供应商id获取集群名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "providerId", value = "供应商id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "clusterName", value = "集群名称", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasClusterListResponse> getClusterNameByProviderId(@RequestParam(required = false) Long providerId, @RequestParam(required = false) String clusterName) {
        IaasClusterVo iaasClusterVo = new IaasClusterVo();
        iaasClusterVo.setProviderId(providerId);
        if (clusterName != null) {
            iaasClusterVo.setClusterName(clusterName);
        }
        //去重查询
        List<IaasCluster> iaasClusterList = clusterService.getClusterNameByProviderId(iaasClusterVo);
        //响应对象
        List<IaasClusterVo> iaasClusterVOList = new ArrayList<>();
        //不为空，复制到拓展类
        if (iaasClusterList != null && iaasClusterList.size() > 0) {
            for (IaasCluster iaasCluster : iaasClusterList) {
                IaasClusterVo iaasClusterVO = new IaasClusterVo();
                BeanUtils.copyProperties(iaasCluster, iaasClusterVO);
                iaasClusterVOList.add(iaasClusterVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IaasClusterListResponse(iaasClusterVOList));
    }


    @GetMapping("/list")
    @ApiOperation(value = "❌（废弃) 获取集群使用趋势列表", notes = "获取集群使用趋势列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeRange", value = "时间范围", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "usageRate", value = "使用率类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "集群id", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasClusterListResponse> list(@RequestParam(required = true) String timeRange,
                                                        @RequestParam(required = true) String usageRate,
                                                        @RequestParam(required = false) String id) {
        IaasClusterVo iaasClusterVO = new IaasClusterVo();

        //小心坑点：查询时候，id不能为0
        if(id!=null){
            iaasClusterVO.setId(Long.parseLong(id));
        }
        //查询
        return ResponseEntity.status(HttpStatus.OK).body(new IaasClusterListResponse(clusterService.getInfoByClusterVO(iaasClusterVO)));
    }

    @GetMapping("/page")
    @ApiOperation(value = "❌（废弃)分页查看集群利用率 ", notes = "分页查看集群利用率 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "timeRange", value = "时间范围", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "usageRate", value = "使用率类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "集群id", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasClusterPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                        @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                        @RequestParam(required = true) String timeRange,
                                                        @RequestParam(required = true) String usageRate,
                                                        @RequestParam(required = false) String id) {

        Page<IaasClusterVo> page = new Page<>(pageNo, pageSize);
       Map<String,Object> map=new HashMap<>();
        map.put("timeRange",timeRange);
        map.put("usageRate",usageRate);
        if(id!=null){
            map.put("id",id);
        }
        //调用分页查询方法
        IPage<IaasClusterVo> result = clusterService.queryClusterVoPage(page,map);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasClusterPageResponse(new PageVO<>(result)));
    }

    @GetMapping("/get_list")
    @ApiOperation(value = "集群查询", notes = "集群查询")
    public ResponseEntity<List<IaasCluster>> page() {
        List<IaasCluster> iaasClusterVos = clusterService.getClusterNameByProviderId(null);
        return ResponseEntity.status(HttpStatus.OK).body(iaasClusterVos);
    }

}