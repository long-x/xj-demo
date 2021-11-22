package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import com.ecdata.cmp.iaas.entity.dto.response.process.IaasProcessPageResponse;
import com.ecdata.cmp.iaas.service.IProcessService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-27 15:14
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/iaas_process")
@Api(tags = "申请服务相关的API")
public class IaasProcessController {
    @Autowired
    private IProcessService iProcessService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查看服务目录", notes = "分页查看服务目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "catalogName", value = "服务目录名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "state", value = "服务目录状态", paramType = "query", dataType = "String")
    })
    public ResponseEntity<IaasProcessPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                        @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                        @RequestParam(required = false) String catalogName,
                                                        @RequestParam(required = false) Long state) {
        Page<IaasProcessApplyVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> params = new HashMap<>();
        params.put("state", state);
        params.put("catalogName", catalogName);
        params.put("currentUserId", Sign.getUserId());
        IPage<IaasProcessApplyVO> result = iProcessService.queryProcessApply(page, params);
        return ResponseEntity.status(HttpStatus.OK).body(new IaasProcessPageResponse(new PageVO<>(result)));

    }

    @GetMapping("/list")
    @ApiOperation(value = "查看服务目录", notes = "查看服务目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentUserId", value = "当前用户id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<List<IaasProcessApplyVO>> list(@RequestParam(required = false) Long currentUserId) {
        Map<String, Object> params = new HashMap<>();
        params.put("state", 1);
        params.put("currentUserId", currentUserId);
        List<IaasProcessApplyVO> result = iProcessService.queryCurrentUserProcessApply(params);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 保存申请服务信息
     *
     * @param processApplyVO
     * @return
     */
    @PostMapping("/add_process_apply")
    @ApiOperation(value = "服务申请信息保存", notes = "服务申请信息保存")
    public ResponseEntity<BaseResponse> saveTemplateMachineComponent(@RequestBody(required = false) IaasProcessApplyVO processApplyVO) {
        BaseResponse baseResponse = iProcessService.saveProcess(processApplyVO);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    /**
     * 编辑申请服务信息
     *
     * @param processApplyVO
     * @return
     */
    @PostMapping("/modify_process_apply")
    @ApiOperation(value = "服务申请信息编辑", notes = "服务申请信息编辑")
    public ResponseEntity<BaseResponse> modifyTemplateMachineComponent(@RequestBody(required = false) IaasProcessApplyVO processApplyVO) {
        BaseResponse baseResponse = iProcessService.editProcess(processApplyVO);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    /**
     * 撤销发布
     *
     * @param processApplyVO
     * @return
     */
    @PostMapping("/update_process")
    @ApiOperation(value = "修改服务申请", notes = "修改服务申请")
    public ResponseEntity<BaseResponse> updateCatalog(@RequestBody IaasProcessApplyVO processApplyVO) {
        //对象赋值
        BaseResponse baseResponse = null;
        try {
            baseResponse = iProcessService.updateProcess(processApplyVO);

            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } catch (Exception e) {
            log.error("ssssssssssssss",e);
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改失败");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        }
    }

    /**
     * 查询申请服务详情
     *
     * @param processApplyId
     * @return
     */
    @GetMapping("/query_process_apply_details")
    @ApiOperation(value = "查询申请服务详情", notes = "查询申请服务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processApplyId", value = "模板id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<IaasProcessApplyVO> queryMachineTree(@RequestParam(value = "processApplyId") Long processApplyId) {
        IaasProcessApplyVO treeResponses = iProcessService.queryProcessApplyDetails(processApplyId);
        return ResponseEntity.status(HttpStatus.OK).body(treeResponses);
    }

    /**
     * 删除服务申请信息
     *
     * @param processApplyId
     * @return
     */
    @GetMapping("/delete_process")
    @ApiOperation(value = "删除服务申请信息", notes = "删除服务申请信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processApplyId", value = "服务申请id", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "deleteReason", value = "删除原因", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", paramType = "query", dataType = "String")
    })
    public ResponseEntity<BaseResponse> deleteTemplate(@RequestParam(value = "processApplyId") Long processApplyId,
                                                       @RequestParam(value = "processInstanceId") String processInstanceId,
                                                       @RequestParam(value = "deleteReason") String deleteReason) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            iProcessService.deleteProcess(processApplyId, processInstanceId, deleteReason);
        } catch (Exception e) {
            baseResponse.setCode(201);
            baseResponse.setMessage("删除申请服务失败！");
        }
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }
}
