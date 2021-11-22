package com.ecdata.cmp.iaas.controller;

import com.ecdata.cmp.activiti.dto.vo.ActHistoricTaskInstanceVO;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.ApplyInfoVO;
import com.ecdata.cmp.iaas.entity.dto.apply.SaveDataVO;
import com.ecdata.cmp.iaas.service.IApplyService;
import com.ecdata.cmp.iaas.service.IAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/3/4 13:14
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/audit")
@Api(tags = "审核相关的API")
public class AuditController {

    @Autowired
    private IAuditService auditService;

    @Autowired
    private IApplyService iApplyService;

    @GetMapping("/process_tracking")
    @ApiOperation(value = "流程跟踪", notes = "流程跟踪")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "配置信息id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<List<ActHistoricTaskInstanceVO>> queryApplyResource(@RequestParam(value = "processInstanceId", required = false) String processInstanceId) {
        List<ActHistoricTaskInstanceVO> actHistoricTaskInstanceVOS = auditService.queryProcessTracking(processInstanceId);
        return ResponseEntity.status(HttpStatus.OK).body(actHistoricTaskInstanceVOS);
    }

    /**
     * 审核：同意，拒绝，驳回
     *
     * @param applyInfoVO
     * @return
     */
    @PostMapping("/process_apply")
    @ApiOperation(value = "审批", notes = "审批")
    public ResponseEntity<BaseResponse> saveTemplateMachineComponent(@RequestBody(required = false) ApplyInfoVO applyInfoVO) {
        BaseResponse baseResponse = iApplyService.apply(applyInfoVO);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }


    @PostMapping("/save_data")
    @ApiOperation(value = "保存预填数据", notes = "保存预填数据")
    public ResponseEntity<BaseResponse> saveData(@RequestBody(required = false) SaveDataVO saveDataVO) {
        BaseResponse baseResponse = iApplyService.saveData(saveDataVO);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }



}
