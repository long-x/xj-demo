package com.ecdata.cmp.iaas.controller;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.apply.OAResultVO;
import com.ecdata.cmp.iaas.service.OaService;
import com.ecdata.cmp.iaas.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xuxiaojian
 * @date 2020/4/13 17:20
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/oa")
@Api(tags = "oa相关接口")
public class OaController {

    @Autowired
    private OaService oaService;

    @Autowired
    private FileUtil fileUtil;

    @PostMapping("/result")
    @ApiOperation(value = "oa审批结果同步", notes = "oa审批结果同步")
    public ResponseEntity<BaseResponse> create(@RequestBody OAResultVO oaResultVO) {
        BaseResponse baseResponse = new BaseResponse();

        if (oaResultVO == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("数据不能为空！");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseResponse);
        }

        if (StringUtils.isBlank(oaResultVO.getApprovalResult())) {
            baseResponse.setCode(202);
            baseResponse.setMessage("审批不能为空！");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseResponse);
        }

        if (StringUtils.isBlank(oaResultVO.getWorkflowId())) {
            baseResponse.setCode(203);
            baseResponse.setMessage("流程id不能为空！");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseResponse);
        }
        //todo 日志表记录oa请求

        baseResponse = oaService.updateOaApprovalResult(oaResultVO);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    @GetMapping("/download/{id}")
    @ApiOperation(value = "oa下载Pdf", notes = "oa下载Pdf")
    public ResponseEntity<BaseResponse> download(@PathVariable("id") String id, HttpServletResponse response) {
        String doID = "";
        if (id.contains(".pdf")) {
            doID = id.split(".pdf")[0];
        } else {
            doID = id;
        }
        BaseResponse baseResponse = fileUtil.downloadFile(response, doID);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
}