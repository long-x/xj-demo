package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoListVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyServiceSecurityResourcesVO;
import com.ecdata.cmp.iaas.entity.dto.request.IaasApplyRequest;
import com.ecdata.cmp.iaas.entity.dto.response.ServerNamePrefixResponse;
import com.ecdata.cmp.iaas.entity.dto.response.apply.ApplyResponse;
import com.ecdata.cmp.iaas.entity.dto.response.apply.IaasApplyResourcePageResponse;
import com.ecdata.cmp.iaas.service.IApplyResourceService;
import com.ecdata.cmp.user.client.SysBusinessGroupClient;
import com.ecdata.cmp.user.dto.SysBusinessGroupVO;
import com.ecdata.cmp.user.dto.response.SysBusinessGroupResponse;
import com.ecdata.cmp.user.dto.response.UserListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuxiaojian
 * @date 2020/3/4 13:14
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/apply")
@Api(tags = "申请相关的API")
public class ApplyResourceController {

    @Autowired
    private IApplyResourceService applyResourceService;
    @Autowired
    private SysBusinessGroupClient sysBusinessGroupClient;

    @GetMapping("/page")
    @ApiOperation(value = "分页查看申请目录", notes = "分页查看申请目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20")
    })
    public ResponseEntity<IaasApplyResourcePageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                              @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                              @RequestParam(required = false) String state,
                                                              @RequestParam(required = false) String startTime,
                                                              @RequestParam(required = false) String endTime,
                                                              @RequestParam(required = false) String keyword) {
        Page<IaasApplyResourceVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> params = new HashMap<>();
        params.put("state", state);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("keyword", keyword);
        params.put("currentUserId", Sign.getUserId());
        IPage<IaasApplyResourceVO> result = applyResourceService.queryApplyResource(page, params);
        return ResponseEntity.status(HttpStatus.OK).body(new IaasApplyResourcePageResponse(new PageVO<>(result)));

    }

    @PostMapping("/resource")
    @ApiOperation(value = "资源申请", notes = "资源申请")
    public ResponseEntity<ApplyResponse> saveResourceApply(@RequestBody(required = false) IaasApplyRequest applyRequest) {
        ApplyResponse baseResponse = applyResourceService.saveResourceApply(applyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @GetMapping("/change_resource_copy")
    @ApiOperation(value = "变更按钮", notes = "变更按钮")
    public ResponseEntity<IaasApplyResourceVO> copyResourceApply(@RequestParam(value = "applyId", required = false) Long applyId) {
        IaasApplyResourceVO baseResponse = applyResourceService.copyResourceApply(applyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/update_resource_config")
    @ApiOperation(value = "修改资源申请", notes = "修改资源申请")
    public ResponseEntity<BaseResponse> updateResourceApply(@RequestBody(required = false) IaasApplyRequest applyRequest) {
        BaseResponse baseResponse = applyResourceService.updateResourceApply(applyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/start_resource_apply")
    @ApiOperation(value = "发起资源申请", notes = "发起资源申请")
    public ResponseEntity<BaseResponse> updateResourceApply(@RequestBody(required = false) IaasApplyResourceVO applyRequest) {
        BaseResponse baseResponse = applyResourceService.startResourceApply(applyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/submit_apply")
    @ApiOperation(value = "提交资源申请", notes = "提交资源申请")
    public ResponseEntity<BaseResponse> submitApply(@RequestBody(required = false) IaasApplyResourceVO applyResourceVO) {
        BaseResponse baseResponse = applyResourceService.submitApply(applyResourceVO);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/update_config_info")
    @ApiOperation(value = "修改申请配置信息", notes = "修改申请配置信息")
    public ResponseEntity<BaseResponse> updateApplyConfigInfo(@RequestBody(required = false) IaasApplyRequest applyRequest) {
        BaseResponse baseResponse = applyResourceService.updateApplyConfigInfo(applyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @GetMapping("/query_apply_resource")
    @ApiOperation(value = "查询资源申请详情", notes = "查询资源申请详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "配置信息id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<List<IaasApplyResourceVO>> queryApplyResource(@RequestParam(value = "applyId", required = false) Long applyId) {
        List<IaasApplyResourceVO> iaasApplyResourceVOS = applyResourceService.queryApplyResource(applyId);
        return ResponseEntity.status(HttpStatus.OK).body(iaasApplyResourceVOS);
    }

    @GetMapping("/recycle_resources_auto")
    @ApiOperation(value = "自动回收资源", notes = "自动回收资源")
    public ResponseEntity<String> recycleResourcesAuto() {
        List<IaasApplyResourceVO> iaasApplyResourceVOList = applyResourceService.recycleResourcesAuto();
        // 无申请资源到期
        if (CollectionUtils.isEmpty(iaasApplyResourceVOList)) {
            return ResponseEntity.status(HttpStatus.OK).body("到期申请资源为空");
        }

        // 审批完成租期到期的资源重新copy一份存入数据库, 再发起一条默认审批流程，审批中添加取消纳管按钮
        for (IaasApplyResourceVO vo : iaasApplyResourceVOList) {
            vo.setState(1);
            vo.setBusinessActivitiId(152777148528160771L);
            vo.setBusinessActivitiName("城投新审核流程");
            applyResourceService.copyResourceAndStartRecoveryApply(vo);
        }

        return ResponseEntity.status(HttpStatus.OK).body("自动回收资源成功");
    }

    @GetMapping("/query_config_list")
    @ApiOperation(value = "查询资源申请详情", notes = "查询资源申请详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "配置信息id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<List<IaasApplyConfigInfoVO>> queryConfigList(@RequestParam(value = "applyId", required = false) Long applyId) {
        List<IaasApplyResourceVO> iaasApplyResourceVOS = applyResourceService.queryApplyResource(applyId);
        if (CollectionUtils.isEmpty(iaasApplyResourceVOS)) {
            ResponseEntity.status(HttpStatus.OK).body(new ArrayList<IaasApplyResourceVO>());
        }

        return ResponseEntity.status(HttpStatus.OK).body(iaasApplyResourceVOS.get(0).getIaasApplyConfigInfoVOList());
    }

    @GetMapping("/query_history_config_list")
    @ApiOperation(value = "查询历史资源申请详情", notes = "查询历史资源申请详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<IaasApplyConfigInfoListVO> queryHistoryConfigList(@RequestParam(value = "businessGroupId", required = true) Long businessGroupId) {
        IaasApplyConfigInfoListVO iaasApplyResourceVOS = applyResourceService.queryApplyResourceByProjectId(businessGroupId);

        return ResponseEntity.status(HttpStatus.OK).body(iaasApplyResourceVOS);
    }

    @PostMapping("/config_update_or_Delete")
    @ApiOperation(value = "资源变更或回收", notes = "资源变更或回收")
    public ResponseEntity<ApplyResponse> handleConfigUpdateOrDelete(@RequestBody(required = false) IaasApplyRequest applyRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(applyResourceService.handleConfigChange(applyRequest));
    }

    @GetMapping("/query_config_info")
    @ApiOperation(value = "查询申请资源详情", notes = "查询申请资源详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configId", value = "配置信息id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<IaasApplyConfigInfoVO> queryConfigInfo(@RequestParam(value = "configId") Long configId) {
        IaasApplyConfigInfoVO applyConfigInfo = applyResourceService.queryApplyConfigInfo(configId);
        return ResponseEntity.status(HttpStatus.OK).body(applyConfigInfo);
    }

    @GetMapping("/query_apply_details")
    @ApiOperation(value = "审核时详情展示", notes = "审核时详情展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "applyId", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<IaasApplyResourceVO> queryApplyDetails(@RequestParam(value = "applyId") Long applyId) {
        IaasApplyResourceVO applyConfigInfo = applyResourceService.queryApplyDetails(applyId);
        return ResponseEntity.status(HttpStatus.OK).body(applyConfigInfo);
    }

    @GetMapping("/query_apply_relation_details")
    @ApiOperation(value = "审核时关系详情展示", notes = "审核时关系详情展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "applyId", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<IaasApplyResourceVO> queryApplyRelationDetails(@RequestParam(value = "applyId") Long applyId) {
        //只有服务审核人员和运营主管可以查看
        IaasApplyResourceVO applyConfigInfo = applyResourceService.queryApplyRelationDetails(applyId);
        return ResponseEntity.status(HttpStatus.OK).body(applyConfigInfo);
    }

    @GetMapping("/delete_apply_resource")
    @ApiOperation(value = "删除资源申请全部信息", notes = "删除资源申请全部信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "申请信息id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<BaseResponse> deleteApplyAll(@RequestParam(value = "applyId") Long applyId) {
        BaseResponse baseResponse = applyResourceService.deleteApplyAll(applyId);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping("/delete_apply_config_details")
    @ApiOperation(value = "删除资源申请信息", notes = "删除资源申请信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configIds", value = "配置信息id", paramType = "query", dataType = "Longs")
    })
    public ResponseEntity<BaseResponse> deleteApplyResource(@RequestParam(value = "configIds") String configIds) {
        BaseResponse baseResponse = new BaseResponse();
        if (StringUtils.isBlank(configIds)) {
            baseResponse.setCode(201);
            baseResponse.setMessage("删除内容不能为空！");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }

        List<Long> idsList = Arrays.asList(configIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        for (Long configId : idsList) {
            baseResponse = applyResourceService.deleteApplyResource(configId);
        }

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping("/auto_server_name")
    @ApiOperation(value = "自动生成服务器名称", notes = "自动生成服务器名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyType", value = "服务器类型", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "sysBusinessGroupId", value = "业务组ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "code", value = "服务器用途代码", paramType = "query", dataType = "String")
    })
    public ResponseEntity<ServerNamePrefixResponse> auto(@RequestParam(value = "applyType") int applyId,
                                                         @RequestParam(value = "sysBusinessGroupId") Long sysBusinessGroupId,
                                                         @RequestParam(value = "code") String code) {
        // 1.虚拟机，2.裸金属，3.安全资源
        ServerNamePrefixResponse serverNamePrefixResponse = new ServerNamePrefixResponse();
        Map<Integer, String> map = new HashMap<>();
        List<String> list = new ArrayList<>();
         map.put(1,"V");
        map.put(2,"P");
        String applyType = map.get(applyId);
        list.add(applyType);
        list.add("CT");
        ResponseEntity<SysBusinessGroupResponse> sysBusinessGroup = sysBusinessGroupClient.getSysBusinessGroupBySnp(sysBusinessGroupId);
        SysBusinessGroupVO vo = sysBusinessGroup.getBody().getData();
        if (vo != null){
            Long parentId = vo.getParentId();
            if (parentId != null){
                // 父
                ResponseEntity<SysBusinessGroupResponse> sysBusinessGroup2 = sysBusinessGroupClient.getSysBusinessGroupBySnp(parentId);
                SysBusinessGroupVO vo2 = sysBusinessGroup2.getBody().getData();
                if (vo2 != null && StringUtils.isNotBlank(vo2.getServerNamePrefix())){
                    list.add(vo2.getServerNamePrefix());
                }
            }
            if (StringUtils.isNotBlank(vo.getServerNamePrefix())){
                list.add(vo.getServerNamePrefix());
            }
        }

        list.add(code);
        String serverNamePrefix = StringUtils.join(list, "_");
        // 查询序号
        int prefix = applyResourceService.queryApplyServerNamePrefix(serverNamePrefix);
        list.add(String.format("%02d", prefix + 1));
        serverNamePrefixResponse.setData(StringUtils.join(list, "_"));
        return ResponseEntity.status(HttpStatus.OK).body(serverNamePrefixResponse);
    }

    @GetMapping("/query_apply_service_security_resources")
    @ApiOperation(value = "获取安全资源信息", notes = "获取安全资源信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyType", value = "资源类型", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "sysBusinessGroupId", value = "业务组id", paramType = "query", dataType = "String")
    })
    public ResponseEntity<IaasApplyConfigInfoVO> queryApplyServiceSecurityResources(@RequestParam(value = "applyType") Integer applyType, @RequestParam(value = "sysBusinessGroupId") String sysBusinessGroupId) {
        IaasApplyConfigInfoVO configInfoVO = applyResourceService.queryApplyServiceSecurityResources(applyType, sysBusinessGroupId);
        return ResponseEntity.status(HttpStatus.OK).body(configInfoVO);
    }

    @GetMapping("/add_candidate")
    @ApiOperation(value = "添加候选人", notes = "添加候选人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "审请资源ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "userId", value = "候选人ID", paramType = "query", dataType = "Long")
    })
    public void addCandidate(@RequestParam(value = "id") Long id, @RequestParam(value = "id") Long userId) {
        applyResourceService.addCandidate(id, userId);
    }

    @GetMapping("/getITDirectors")
    @ApiOperation(value = "IT主管审批人", notes = "IT主管审批人")
    public ResponseEntity<UserListResponse> getITDirectors() {
        UserListResponse userListResponse = applyResourceService.getITDirectors();
        return ResponseEntity.status(HttpStatus.OK).body(userListResponse);
    }

}
