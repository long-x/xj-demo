package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.NetworkUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.IaasNetworkSegmentRelationship;
import com.ecdata.cmp.iaas.entity.NetworkIp;
import com.ecdata.cmp.iaas.entity.NetworkSegment;
import com.ecdata.cmp.iaas.entity.dto.IaasNetworkSegmentRelationshipVO;
import com.ecdata.cmp.iaas.entity.dto.NetworkIpVO;
import com.ecdata.cmp.iaas.entity.dto.NetworkSegmentVO;
import com.ecdata.cmp.iaas.entity.dto.response.*;
import com.ecdata.cmp.iaas.service.IIaasNetworkSegmentRelationshipService;
import com.ecdata.cmp.iaas.service.INetworkIpService;
import com.ecdata.cmp.iaas.service.INetworkSegmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-08-08
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/network")
@Api(tags = "网络相关接口")
public class NetworkController {

    /**
     * 网段Service
     */
    @Autowired
    private INetworkSegmentService networkSegmentService;

    @Autowired
    private IIaasNetworkSegmentRelationshipService relationshipService;
    /**
     * 网络ip Service
     */
    @Autowired
    private INetworkIpService networkIpService;


    @PostMapping("/relation/add")
    @ApiOperation(value = "集群与网络关系", notes = "集群与网络关系")
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody IaasNetworkSegmentRelationshipVO relationVO) {
        BaseResponse baseResponse = new BaseResponse();
        QueryWrapper<IaasNetworkSegmentRelationship> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IaasNetworkSegmentRelationship::getNetworkSegmentId, relationVO.getNetworkSegmentId());
        queryWrapper.lambda().eq(IaasNetworkSegmentRelationship::getClusterNetworkId, relationVO.getClusterNetworkId());
        if (relationshipService.count(queryWrapper) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"集群网段关系已存在"));
        }


        IaasNetworkSegmentRelationship relation = new IaasNetworkSegmentRelationship();
        BeanUtils.copyProperties(relationVO, relation);

        relation.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow());

        if (relationshipService.save(relation)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }



    @GetMapping("/relation/page")
    @ApiOperation(value = "分页查看网段", notes = "分页查看网段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasNetworkSegmentRelationshipPageResponse> pageRelation(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                                                   @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                                                   @RequestParam(required = false) Long clusterNetworkId,
                                                                                   @RequestParam(required = false) Long networkSegmentId) {
        Page<IaasNetworkSegmentRelationship> page = new Page<>(pageNo, pageSize);
        QueryWrapper<IaasNetworkSegmentRelationship> queryWrapper = new QueryWrapper<>();
        if (clusterNetworkId != null) {
            queryWrapper.lambda().eq(IaasNetworkSegmentRelationship::getClusterNetworkId, clusterNetworkId);
        }
        if (networkSegmentId != null) {
            queryWrapper.lambda().eq(IaasNetworkSegmentRelationship::getNetworkSegmentId, networkSegmentId);
        }
        IPage<IaasNetworkSegmentRelationship> result = relationshipService.page(page, queryWrapper);
        List<IaasNetworkSegmentRelationshipVO> relationList= new ArrayList<>();
        List<IaasNetworkSegmentRelationship> relations = result.getRecords();

        if (relations != null && relations.size() > 0) {
            for (IaasNetworkSegmentRelationship segment : relations) {
                IaasNetworkSegmentRelationshipVO relationVO = new IaasNetworkSegmentRelationshipVO();
                BeanUtils.copyProperties(segment,relationVO);
                relationList.add(relationVO);
            }
        }
        return  ResponseEntity.status(HttpStatus.OK).body(new IaasNetworkSegmentRelationshipPageResponse(new PageVO<>(result,relationList)));
    }

    @GetMapping("/relation/list")
    @ApiOperation(value = "获取网段关系列表", notes = "获取网段关系列表")
    public ResponseEntity<IaasNetworkSegmentRelationshipListResponse> listRelation(@RequestParam(required = false) Long clusterNetworkId,
                                                                                   @RequestParam(required = false) Long networkSegmentId) {
        QueryWrapper<IaasNetworkSegmentRelationship> queryWrapper = new QueryWrapper<>();
        if (clusterNetworkId != null) {
            queryWrapper.lambda().eq(IaasNetworkSegmentRelationship::getClusterNetworkId, clusterNetworkId);
        }
        if (networkSegmentId != null) {
            queryWrapper.lambda().eq(IaasNetworkSegmentRelationship::getNetworkSegmentId, networkSegmentId);
        }
        List<IaasNetworkSegmentRelationship> result = relationshipService.list(queryWrapper);
        List<IaasNetworkSegmentRelationshipVO> relationList = new ArrayList<>();

        if (result != null && result.size() > 0) {
            for (IaasNetworkSegmentRelationship segment : result) {

                IaasNetworkSegmentRelationshipVO relationVO = new IaasNetworkSegmentRelationshipVO();
                BeanUtils.copyProperties(segment, relationVO);
                relationList.add(relationVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new IaasNetworkSegmentRelationshipListResponse(relationList));
    }


    @GetMapping("/relation/segment_list")
    @ApiOperation(value = "关系网段列表", notes = "关系网段列表")
    public ResponseEntity<NetworkSegmentListResponse> listRelationSegment(@RequestParam(required = false) Long clusterId,
                                                                                   @RequestParam(required = false) Long segmentId) {

        List<NetworkSegmentVO> resultList = networkSegmentService.getRelationSegmentList(clusterId,segmentId);

        return ResponseEntity.status(HttpStatus.OK).body(new NetworkSegmentListResponse(resultList));
    }

    @DeleteMapping("/relation/remove")
    @ApiOperation(value = "删除", notes = "删除网络关系")
    public ResponseEntity<BaseResponse> removeRelation(@RequestParam Long id) {
        log.info("删除网络关系id ：{}", id);
        if (relationshipService.removeById(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"删除成功"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"删除失败"));
        }
    }



    @PutMapping("/relation/remove_batch")
    @ApiOperation(value = "批量删除", notes = "批量删除网络关系")
    public ResponseEntity<BaseResponse> removeRelationBatch(@RequestParam(name = "ids") String ids) {
        log.info("删除网络关系 ids：{}", ids);
        if (StringUtils.isEmpty(ids)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"参数不识别"));
        } else {
            String[] idArray = ids.split(",");
            this.relationshipService.removeByIds(Arrays.asList(idArray));
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"删除网络地址成功"));
        }

    }



    @GetMapping("/segment/{id}")
    @ApiOperation(value = "根据id查询网段", notes = "根据id查询网段")
    @ApiImplicitParam(name = "id", value = "网段id", required = true, paramType = "path")
    public ResponseEntity<NetworkSegmentResponse> getSegmentById(@PathVariable(name = "id") Long id) {
        NetworkSegment networkSegment = networkSegmentService.getById(id);
        List<NetworkIpVO> workIpList = networkIpService.getIpListBySegmentId(id, null, null);

        NetworkSegmentVO netVO = new NetworkSegmentVO();
        BeanUtils.copyProperties(networkSegment,netVO);
        netVO.setNetworkIpList(workIpList);
        return ResponseEntity.status(HttpStatus.OK).body(new NetworkSegmentResponse(netVO));
    }



    @GetMapping("/segment/page")
    @ApiOperation(value = "分页查看网段", notes = "分页查看网段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<NetworkSegmentPageResponse> pageSegment(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                                  @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                                  @RequestParam(required = false) String keyword) {
            Page<NetworkSegment> page = new Page<>(pageNo, pageSize);
            QueryWrapper<NetworkSegment> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(keyword)) {
                queryWrapper.lambda().like(NetworkSegment::getSegmentName, keyword)
                        .or().like(NetworkSegment::getDns, keyword)
                        .or().like(NetworkSegment::getGateway, keyword)
                        .or().like(NetworkSegment::getNetmask, keyword)
                        .or().like(NetworkSegment::getSegment, keyword);
            }
            IPage<NetworkSegment> result = networkSegmentService.page(page, queryWrapper);
            List<NetworkSegmentVO> networkSegmenList= new ArrayList<>();
            List<NetworkSegment> networkSegmens = result.getRecords();

            if (networkSegmens != null && networkSegmens.size() > 0) {
                for (NetworkSegment segment : networkSegmens) {
                    NetworkSegmentVO networkSegmentVO = new NetworkSegmentVO();
                    BeanUtils.copyProperties(segment,networkSegmentVO);
                    networkSegmenList.add(networkSegmentVO);
                }
            }
            return  ResponseEntity.status(HttpStatus.OK).body(new NetworkSegmentPageResponse(new PageVO<>(result,networkSegmenList)));
    }

    @GetMapping("/segment/list")
    @ApiOperation(value = "获取网段列表", notes = "获取网段列表")
    @ApiImplicitParam(name = "enable", value = "是否启用(0:不启用;1:启用;)", paramType = "query", dataType = "int")
    public ResponseEntity<NetworkSegmentListResponse> listSegment(@RequestParam(required = false) Integer enable) {
            QueryWrapper<NetworkSegment> queryWrapper = new QueryWrapper<>();
            if (enable != null) {
                queryWrapper.lambda().eq(NetworkSegment::getEnable, enable);
            }
            queryWrapper.orderByDesc("create_time");

            List<NetworkSegment> result = networkSegmentService.list(queryWrapper);
            List<NetworkSegmentVO> segmanetList = new ArrayList<>();

            if (result != null && result.size() > 0) {
                for (NetworkSegment segment : result) {

                    NetworkSegmentVO networkSegmentVO = new NetworkSegmentVO();
                    BeanUtils.copyProperties(segment, networkSegmentVO);
                    segmanetList.add(networkSegmentVO);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new NetworkSegmentListResponse(segmanetList));
    }

    @PostMapping("/segment/add")
    @ApiOperation(value = "新增网段", notes = "新增网段")
    public ResponseEntity<BaseResponse> addSegment(@RequestBody NetworkSegmentVO networkSegmentVO) {

            String gateway = networkSegmentVO.getGateway();
            if (!NetworkUtil.checkIp(gateway)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败,网关参数错误"));
            }
            QueryWrapper<NetworkSegment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(NetworkSegment::getGateway, gateway);
            if (networkSegmentService.count(queryWrapper) > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败,网关已存在"));
            }

            if(StringUtils.isNotBlank(networkSegmentVO.getDns())){
                if (!NetworkUtil.checkIp(networkSegmentVO.getDns())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败,域名服务器参数错误"));
                }
            }

            String netmask = networkSegmentVO.getNetmask();
            if (!NetworkUtil.checkIp(netmask)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败,子网掩码参数错误"));
            }
            String segment = networkSegmentVO.getSegment();
            String segmentIp = null;
            if (StringUtils.isNotEmpty(segment)) {
                String[] arr = segment.split("/");
                segmentIp = arr[0];
                if (!NetworkUtil.checkIp(segmentIp)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败,网段参数错误"));
                }
                if(arr.length>1){
                    Integer num = Integer.valueOf(arr[1]);
                    if (!netmask.equals(NetworkUtil.getNetmask(num))) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败,子网掩码与掩码位不一致"));
                    }
                }

            }

            List<NetworkIpVO> networkIpList = networkSegmentVO.getNetworkIpList();
            if (networkIpList != null && networkIpList.size() > 0) {
                for (NetworkIpVO networkIp : networkIpList) {
                    String ipAddress = networkIp.getIpAddress();
                    if (!NetworkUtil.checkIp(ipAddress)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败，自定义ip列表参数错误，错误ip:" + ipAddress));
                    }
                }
            }

            NetworkSegment networkSegment = new NetworkSegment();
            BeanUtils.copyProperties(networkSegmentVO, networkSegment);
            networkSegment.setId(SnowFlakeIdGenerator.getInstance().nextId())
                    .setCreateUser(Sign.getUserId())
                    .setCreateTime(DateUtil.getNow());
            if (networkSegmentService.save(networkSegment)) {
                Long segmentId = networkSegment.getId();
                if (networkIpList != null && networkIpList.size() > 0) {
                    List<NetworkIp> ipList = new ArrayList<>();
                    for(NetworkIpVO workIp :networkIpList){
                        NetworkIp networkIp = new NetworkIp();
                        BeanUtils.copyProperties(workIp,networkIp);
                        ipList.add(networkIp);
                    }
                    networkIpService.addIpBatch(segmentId, ipList);
                } else {
                    String ipParam = StringUtils.isEmpty(segmentIp) ? gateway : segmentIp;
                    long startIpValue = NetworkUtil.getStartIpValue(ipParam, netmask);
                    long endIpValue = NetworkUtil.getEndIpValue(ipParam, netmask);
                    long gatewayValue = NetworkUtil.ipConvertValue(gateway);
                    networkIpService.addIpBatch(segmentId, startIpValue, endIpValue, gatewayValue);
                }

                return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"添加网段成功"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败"));
            }
    }

    @PutMapping("/segment/update")
    @ApiOperation(value = "修改网段", notes = "修改网段")
    public ResponseEntity<BaseResponse> updateSegment(@RequestBody NetworkSegmentVO networkSegment) {
            Long id = networkSegment.getId();
            if (id == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"缺少主键"));
            }
            String gateway = networkSegment.getGateway();
            if (!NetworkUtil.checkIp(gateway)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"修改网段失败,网关参数错误"));
            }
            QueryWrapper<NetworkSegment> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(NetworkSegment::getGateway, gateway).
                    ne(NetworkSegment::getId, id);
            if (networkSegmentService.count(queryWrapper) > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"修改网段失败,网关已存在"));
            }

            if(StringUtils.isNotBlank(networkSegment.getDns())){
                if (!NetworkUtil.checkIp(networkSegment.getDns())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败,域名服务器参数错误"));
                }
            }

            String netmask = networkSegment.getNetmask();
            if (!NetworkUtil.checkIp(netmask)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"修改网段失败,子网掩码参数错误"));
            }
            String segment = networkSegment.getSegment();
            String segmentIp = null;
            if (StringUtils.isNotEmpty(segment)) {
                String[] arr = segment.split("/");
                segmentIp = arr[0];
                if (!NetworkUtil.checkIp(segmentIp)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败,网段参数错误"));
                }
                if(arr.length>1){
                    Integer num = Integer.valueOf(arr[1]);
                    if (!netmask.equals(NetworkUtil.getNetmask(num))) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网段失败,子网掩码与掩码位不一致"));
                    }
                }

            }
            networkSegment.setUpdateUser(Sign.getUserId())
                          .setUpdateTime(DateUtil.getNow());
        NetworkSegment workSegment = new NetworkSegment();
        BeanUtils.copyProperties(networkSegment,workSegment);
        if (networkSegmentService.updateById(workSegment)) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"修改网段成功"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"修改网段失败"));
            }
    }

    @PutMapping("/segment/remove")
    @ApiOperation(value = "删除", notes = "删除网段")
    public ResponseEntity<BaseResponse> removeSegment(@RequestParam Long id) {
        log.info("删除网段 networkSegment id：{}", id);
        try {
            if (networkSegmentService.removeById(id)) {
                networkSegmentService.modifyUpdateRecord(id, Sign.getUserId());
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"删除网段成功"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"删除网段失败"));
            }
        } catch (Exception e) {
            log.info("remove networkSegment error :{}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/segment/remove_batch")
    @ApiOperation(value = "批量删除", notes = "批量删除网段")
    public ResponseEntity<BaseResponse> removeSegmentBatch(@RequestParam(name = "ids") String ids) {
        log.info("删除网段 ids：{}", ids);
            String[] idArray = ids.split(",");
            if (StringUtils.isEmpty(ids)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"参数不识别"));
            } else {
                this.networkSegmentService.removeByIds(Arrays.asList(idArray));
                for (String id : idArray) {
                    this.networkSegmentService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                }
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"删除网段成功"));
            }
    }

    @GetMapping("/ip/{id}")
    @ApiOperation(value = "根据id查询网络地址", notes = "根据id查询网络地址")
    @ApiImplicitParam(name = "id", value = "网络地址id", required = true, paramType = "path")
    public ResponseEntity<NetworkIpResponse> getIpById(@PathVariable(name = "id") Long id) {
        NetworkIp netWorkIp = networkIpService.getById(id);

        NetworkIpVO netVO = new NetworkIpVO();
        BeanUtils.copyProperties(netWorkIp,netVO);
        return ResponseEntity.status(HttpStatus.OK).body(new NetworkIpResponse(netVO));
    }

    @GetMapping("/ip/page")
    @ApiOperation(value = "分页查看网络地址", notes = "分页查看网络地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<NetworkIpPageResponse> pageIp(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                              @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                              @RequestParam(required = false) Long segmentId,
                                                              @RequestParam(required = false) String keyword) {
            Page<NetworkIp> page = new Page<>(pageNo, pageSize);
            QueryWrapper<NetworkIp> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(keyword)) {
                queryWrapper.lambda().like(NetworkIp::getIpAddress, keyword);
            }
            if (segmentId !=null) {
                queryWrapper.lambda().eq(NetworkIp::getSegmentId, segmentId);
            }
            queryWrapper.orderByAsc("ip_value");
            IPage<NetworkIp> result = networkIpService.page(page, queryWrapper);
            List<NetworkIp> networkIps = result.getRecords();

            List<NetworkIpVO> workIpList = new ArrayList<>();
            if (networkIps != null && networkIps.size() > 0) {
                for (NetworkIp workIp : networkIps) {
                    NetworkIpVO networkIpVO = new NetworkIpVO();
                    BeanUtils.copyProperties(workIp, networkIpVO);
                    workIpList.add(networkIpVO);
                }
            }
            return  ResponseEntity.status(HttpStatus.OK).body(new NetworkIpPageResponse(new PageVO<>(result,workIpList)));
    }

    @GetMapping("/ip/list")
    @ApiOperation(value = "获取网络地址列表", notes = "获取网络地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "segmentId", value = "网段id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "type", value = "类型(1:待定;2:虚拟机;3:网关;)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "used", value = "是否被占用(0:可用;1:占用;)", paramType = "query", dataType = "int")
    })
    public ResponseEntity<NetworkIpListResponse> listIp(@RequestParam(required = false) Long segmentId,
                                                        @RequestParam(required = false) Integer type,
                                                        @RequestParam(required = false) Integer used) {
        List<NetworkIpVO> workIpList = networkIpService.getIpListBySegmentId(segmentId, type, used);
            return ResponseEntity.status(HttpStatus.OK).body(new NetworkIpListResponse(workIpList));

        }

    @PostMapping("/ip/add")
    @ApiOperation(value = "新增网络地址", notes = "新增网络地址")
    public ResponseEntity<BaseResponse> addIp(@RequestBody NetworkIpVO networkIp) {
            String ipAddress = networkIp.getIpAddress();
            if (!NetworkUtil.checkIp(ipAddress)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网络地址失败, ip错误"));
            }
        NetworkIp workIp = new NetworkIp();
        BeanUtils.copyProperties(networkIp,workIp);
        workIp.setIpValue(NetworkUtil.ipConvertValue(ipAddress))
              .setCreateTime(DateUtil.getNow())
              .setCreateUser(Sign.getUserId());

            if (networkIpService.save(workIp)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"添加网络地址成功"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网络地址失败"));
            }
    }

    @PutMapping("/ip/update")
    @ApiOperation(value = "修改网络地址", notes = "修改网络地址")
    public ResponseEntity<BaseResponse> updateIp(@RequestBody NetworkIpVO networkIp) {
            if (networkIp.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"缺少主键"));
            }
            String ipAddress = networkIp.getIpAddress();
            if (!NetworkUtil.checkIp(ipAddress)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网络地址失败, ip错误"));
            }
            NetworkIp workIp = new NetworkIp();
            BeanUtils.copyProperties(networkIp, workIp);
            workIp.setIpValue(NetworkUtil.ipConvertValue(ipAddress))
                .setUpdateUser(Sign.getUserId())
                .setUpdateTime(DateUtil.getNow());
            if (networkIpService.updateById(workIp)) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"更新网络地址成功"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"添加网络地址失败"));
            }

    }

    @PutMapping("/ip/remove")
    @ApiOperation(value = "删除", notes = "删除网络地址")
    public ResponseEntity<BaseResponse> removeIp(@RequestParam Long id) {
        log.info("删除网络地址 networkIp id：{}", id);
        if (networkIpService.removeById(id)) {
                networkIpService.modifyUpdateRecord(id, Sign.getUserId());
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"删除网络地址成功"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"删除网络地址失败"));
        }

    }

    @PutMapping("/ip/remove_batch")
    @ApiOperation(value = "批量删除", notes = "批量删除网络地址")
    public ResponseEntity<BaseResponse> removeIpBatch(@RequestParam(name = "ids") String ids) {
        log.info("删除网络地址 ids：{}", ids);
         if (StringUtils.isEmpty(ids)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(ResultEnum.DEFAULT_FAIL.getCode(),"参数不识别"));
            } else {
                String[] idArray = ids.split(",");
                this.networkIpService.removeByIds(Arrays.asList(idArray));
                for (String id : idArray) {
                    this.networkIpService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
                }
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(ResultEnum.DEFAULT_SUCCESS.getCode(),"删除网络地址成功"));
         }

    }
}
