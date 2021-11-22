package com.ecdata.cmp.activiti.controller;

import com.ecdata.cmp.activiti.dto.vo.webservice.WorkflowRequestInfoVO;
import com.ecdata.cmp.activiti.dto.vo.webservice.WorkflowRequestTableFieldVO;
import com.ecdata.cmp.activiti.dto.vo.webservice.WorkflowRequestTableRecordVO;
import com.ecdata.cmp.activiti.service.WebServiceService;
import com.ecdata.cmp.activiti.webService.WorkflowBaseInfo;
import com.ecdata.cmp.activiti.webService.WorkflowMainTableInfo;
import com.ecdata.cmp.activiti.webService.WorkflowRequestInfo;
import com.ecdata.cmp.activiti.webService.WorkflowRequestTableField;
import com.ecdata.cmp.activiti.webService.WorkflowRequestTableRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

/**
 * @author xuxiaojian
 * @date 2020/4/13 9:54
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/oa")
@Api(tags = "泛微oa相关接口")
public class WebServiceController {

    @Autowired
    private WebServiceService webServiceService;

    @GetMapping("/getUserId")
    @ApiOperation(value = "获取人员ID", notes = "获取人员ID")
    public ResponseEntity<String> getUserId(@RequestParam String filedType,
                                            @RequestParam String filedValue) {
        return ResponseEntity.status(HttpStatus.OK).body(webServiceService.getUserId(filedType, filedValue));
    }

    @PostMapping("/doCreateWorkflowRequest")
    @ApiOperation(value = "创建工作流程", notes = "创建工作流程")
    public ResponseEntity<String> doCreateWorkflowRequest(@RequestBody WorkflowRequestInfoVO workflowRequestInfoVO) {
        WorkflowRequestInfo workflowRequestInfo = new WorkflowRequestInfo();
        assembler(workflowRequestInfoVO, workflowRequestInfo);
        return ResponseEntity.status(HttpStatus.OK).body(webServiceService.doCreateWorkflowRequest(workflowRequestInfo, workflowRequestInfoVO.getUserId()));
    }

    @PostMapping("/submitWorkflowRequest")
    @ApiOperation(value = "提交工作流程", notes = "提交工作流程")
    public ResponseEntity<String> submitWorkflowRequest(@RequestBody WorkflowRequestInfoVO vo) {
        WorkflowRequestInfo workflowRequestInfo = new WorkflowRequestInfo();
        assembler(vo, workflowRequestInfo);
        return ResponseEntity.status(HttpStatus.OK).body(webServiceService.submitWorkflowRequest(workflowRequestInfo, vo.getRequestId(), vo.getUserId(), vo.getType(), vo.getRemark()));
    }

    private void assembler(WorkflowRequestInfoVO vo, WorkflowRequestInfo workflowRequestInfo) {
        WorkflowRequestTableRecordVO[] requestTableRecordVOS = vo.getWorkflowMainTableInfo().getRequestRecords();
        WorkflowRequestTableFieldVO[] workflowRequestTableFieldVOS = requestTableRecordVOS[0].getWorkflowRequestTableFields();

        WorkflowRequestTableField[] workflowRequestTableFields = new WorkflowRequestTableField[10];
        for (int i = 0; i < workflowRequestTableFieldVOS.length; i++) {
            WorkflowRequestTableField tableField = new WorkflowRequestTableField();
            tableField.setFieldName(workflowRequestTableFieldVOS[i].getFieldName());
            tableField.setFieldType(workflowRequestTableFieldVOS[i].getFieldType());
            tableField.setFieldValue(workflowRequestTableFieldVOS[i].getFieldValue());
            tableField.setEdit(workflowRequestTableFieldVOS[i].getEdit());
            tableField.setView(workflowRequestTableFieldVOS[i].getView());
            workflowRequestTableFields[i] = tableField;
        }

        WorkflowRequestTableRecord tableRecord = new WorkflowRequestTableRecord();
        tableRecord.setWorkflowRequestTableFields(workflowRequestTableFields);

        WorkflowRequestTableRecord[] requestRecords = {tableRecord};

        WorkflowMainTableInfo mainTableInfo = new WorkflowMainTableInfo();
        mainTableInfo.setRequestRecords(requestRecords);

        workflowRequestInfo.setWorkflowMainTableInfo(mainTableInfo);
        workflowRequestInfo.setCreatorId(vo.getCreatorId());
        workflowRequestInfo.setRequestLevel(vo.getRequestLevel());
        workflowRequestInfo.setRequestName(vo.getRequestName());

        WorkflowBaseInfo workflowBaseInfo = new WorkflowBaseInfo();
        workflowBaseInfo.setWorkflowId(vo.getWorkflowBaseInfo().getWorkflowId());
        workflowRequestInfo.setWorkflowBaseInfo(workflowBaseInfo);
    }
}
