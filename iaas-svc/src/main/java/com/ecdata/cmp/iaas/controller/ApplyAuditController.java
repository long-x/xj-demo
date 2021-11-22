package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.apply.IaasApplyRelationInfo;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyAuditVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyRelationInfoParams;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasNextApplyUserParam;
import com.ecdata.cmp.iaas.entity.dto.apply.ProgramSupportParam;
import com.ecdata.cmp.iaas.entity.dto.response.apply.AuditResponse;
import com.ecdata.cmp.iaas.service.IApplyAuditCalculateService;
import com.ecdata.cmp.iaas.service.IApplySecurityServerService;
import com.ecdata.cmp.iaas.service.IaasApplyNetworkPolicyService;
import com.ecdata.cmp.iaas.service.IaasApplyOtherService;
import com.ecdata.cmp.iaas.service.IaasApplyRelationInfoService;
import com.ecdata.cmp.iaas.service.IaasApplySoftwareServerService;
import com.ecdata.cmp.iaas.service.IaasApplyStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ty
 * @date 2020/3/10 15:03
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/apply/audit")
@Api(tags = "计算信息相关的API")
public class ApplyAuditController {

    @Autowired
    private IApplyAuditCalculateService iApplyAuditCalculateService;

    @Autowired
    private IApplySecurityServerService iApplySecurityServerService;

    @Autowired
    private IaasApplyStorageService applyStorageService;

    @Autowired
    private IaasApplySoftwareServerService softwareServerService;

    @Autowired
    private IaasApplyNetworkPolicyService applyNetworkPolicyService;

    @Autowired
    private IaasApplyOtherService iaasApplyOtherService;

    @Autowired
    private IaasApplyRelationInfoService relationInfoService;

    @PostMapping("/save")
    @ApiOperation(value = "保存计算信息", notes = "保存计算信息")
    public ResponseEntity<AuditResponse> saveIaasApplyCalculate(@RequestBody(required = false) IaasApplyAuditVO applyAuditVO) {
        AuditResponse baseResponse = new AuditResponse();
        try {
            if (1 == applyAuditVO.getType()) {//计算资源
                baseResponse = iApplyAuditCalculateService.saveIaasApplyCalculate(applyAuditVO.getIaasApplyCalculateVO());

                relationInfoService.handleRelationInfo(baseResponse.getId(), applyAuditVO.getApplyId(), applyAuditVO.getProcessInstanceId());
            } else if (2 == applyAuditVO.getType()) {//存储信息
                baseResponse = applyStorageService.saveIaasApplyStorage(applyAuditVO.getIaasApplyStorageVO());

                relationInfoService.handleRelationInfo(baseResponse.getId(), applyAuditVO.getApplyId(), applyAuditVO.getProcessInstanceId());
            } else if (3 == applyAuditVO.getType()) {//安全服务
                baseResponse = iApplySecurityServerService.saveSecurityServer(applyAuditVO.getIaasApplySecurityServerVO());

                relationInfoService.handleRelationInfo(baseResponse.getId(), applyAuditVO.getApplyId(), applyAuditVO.getProcessInstanceId());
            } else if (4 == applyAuditVO.getType()) {//网络策略
                baseResponse = applyNetworkPolicyService.saveIaasApplyNetworkPolicy(applyAuditVO.getIaasApplyNetworkPolicyVO());

                relationInfoService.handleRelationInfo(baseResponse.getId(), applyAuditVO.getApplyId(), applyAuditVO.getProcessInstanceId());
            } else if (5 == applyAuditVO.getType()) {//软件服务
                baseResponse = softwareServerService.saveIaasApplySoftwareServer(applyAuditVO.getIaasApplySoftwareServerVO());

                relationInfoService.handleRelationInfo(baseResponse.getId(), applyAuditVO.getApplyId(), applyAuditVO.getProcessInstanceId());
            } else if (6 == applyAuditVO.getType()) {//其他信息
                baseResponse = iaasApplyOtherService.saveIaasApplyOther(applyAuditVO.getIaasApplyOtherVO());

                relationInfoService.handleRelationInfo(baseResponse.getId(), applyAuditVO.getApplyId(), applyAuditVO.getProcessInstanceId());
            }
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } catch (Exception e) {
            log.info("save iaas apply calculate error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/save_relation_info")
    @ApiOperation(value = "审核关系维护", notes = "审核关系维护")
    public ResponseEntity<BaseResponse> saveRelationInfo(@RequestBody(required = false) IaasApplyRelationInfoParams relationInfoParams) {

        if (CollectionUtils.isEmpty(relationInfoParams.getParamList())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        relationInfoService.saveIaasApplyRelationInfo(relationInfoParams);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder().build());
    }

    @PostMapping("/next_apply_user")
    @ApiOperation(value = "添加下一级审批人", notes = "添加下一级审批人")
    public ResponseEntity<BaseResponse> nextApplyUser(@RequestBody(required = false) IaasNextApplyUserParam param) {

        relationInfoService.nextApplyUser(param);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder().build());
    }

    @PostMapping("/program_support")
    @ApiOperation(value = "方案支持", notes = "方案支持")
    public ResponseEntity<BaseResponse> programSupport(@RequestBody(required = false) ProgramSupportParam param) {

        relationInfoService.programSupport(param);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder().build());
    }

    @GetMapping(path = "/delete")
    @ApiOperation(value = "删除计算信息", notes = "删除计算信息")
    public ResponseEntity<BaseResponse> deleteIaasApplyCalculate(Long id, int type) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            if (1 == type) {//计算资源
                baseResponse = iApplyAuditCalculateService.deleteIaasApplyCalculate(id);

                QueryWrapper<IaasApplyRelationInfo> query = new QueryWrapper<>();
                query.lambda().eq(IaasApplyRelationInfo::getBusinessId, id);
                relationInfoService.remove(query);
            } else if (2 == type) {//存储信息
                baseResponse = applyStorageService.deleteIaasApplyStorage(id);

                QueryWrapper<IaasApplyRelationInfo> query = new QueryWrapper<>();
                query.lambda().eq(IaasApplyRelationInfo::getBusinessId, id);
                relationInfoService.remove(query);
            } else if (3 == type) {//安全服务
                baseResponse = iApplySecurityServerService.deleteSecurityServer(id);

                QueryWrapper<IaasApplyRelationInfo> query = new QueryWrapper<>();
                query.lambda().eq(IaasApplyRelationInfo::getBusinessId, id);
                relationInfoService.remove(query);
            } else if (4 == type) {//网络策略
                baseResponse = applyNetworkPolicyService.deleteIaasApplyNetworkPolicy(id);

                QueryWrapper<IaasApplyRelationInfo> query = new QueryWrapper<>();
                query.lambda().eq(IaasApplyRelationInfo::getBusinessId, id);
                relationInfoService.remove(query);
            } else if (5 == type) {//软件服务
                baseResponse = softwareServerService.deleteIaasApplySoftwareServer(id);

                QueryWrapper<IaasApplyRelationInfo> query = new QueryWrapper<>();
                query.lambda().eq(IaasApplyRelationInfo::getBusinessId, id);
                relationInfoService.remove(query);
            }else if(6 == type){
                baseResponse= iaasApplyOtherService.deleteIaasApplyOther(id);

                QueryWrapper<IaasApplyRelationInfo> query = new QueryWrapper<>();
                query.lambda().eq(IaasApplyRelationInfo::getBusinessId, id);
                relationInfoService.remove(query);
            }
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } catch (Exception e) {
            log.info("delete iaas apply calculate error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(path = "/query_apply_user")
    @ApiOperation(value = "服务台查询选择审批人", notes = "服务台查询选择审批人")
    public ResponseEntity<String> queryApplyUser(Long applyId) {
       return ResponseEntity.status(HttpStatus.OK).body(relationInfoService.queryApplyUser(applyId));
    }

    @GetMapping(path = "/query_apply_user_id")
    @ApiOperation(value = "服务台查询选择审批人", notes = "服务台查询选择审批人")
    public ResponseEntity<List<Map<String,String>>> queryApplyUserId(Long applyId) {
        return ResponseEntity.status(HttpStatus.OK).body(relationInfoService.queryApplyUserId(applyId));
    }



    @GetMapping(path = "/del_apply_user")
    @ApiOperation(value = "删除实施人", notes = "删除实施人")
    public ResponseEntity<BaseResponse> queryApplyUserId(Long applyId,Long userId) {
        BaseResponse baseResponse = new BaseResponse();
        if(relationInfoService.deleteApplyUser(applyId,userId)){
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");

        }else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("删除失败");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

}
