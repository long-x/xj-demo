package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.IaasTemplate;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVO;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasTemplatePageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasTemplateResponse;
import com.ecdata.cmp.iaas.entity.dto.response.template.TemplateVirtualMachineTreeResponse;
import com.ecdata.cmp.iaas.service.ITemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-18 15:14
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/iaas_template")
@Api(tags = "模板相关的API")
public class IaasTemplateController {
    @Autowired
    private ITemplateService iTemplateService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查看模板", notes = "分页查看模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "templateName", value = "模板标题", paramType = "query", dataType = "String")
    })
    public ResponseEntity<IaasTemplatePageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                         @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                         @RequestParam(required = false) String templateName) {
        Page<IaasTemplateVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> params = new HashMap<>();
        params.put("templateName", templateName);
        IPage<IaasTemplateVO> result = iTemplateService.queryIaasTemplate(page, params);
        return ResponseEntity.status(HttpStatus.OK).body(new IaasTemplatePageResponse(new PageVO<>(result)));

    }

    @PostMapping("/add")
    @ApiOperation(value = "新增模板", notes = "新增模板")
    public ResponseEntity<IaasTemplateResponse> saveTemplate(@RequestBody IaasTemplateVO templateVO) {
        IaasTemplateResponse baseResponse = new IaasTemplateResponse();

        //校验必填项
        if (StringUtils.isEmpty(templateVO.getTemplateName())) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("请输入标题！");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

        //对象赋值
        IaasTemplate iaasTemplate = new IaasTemplate();
        BeanUtils.copyProperties(templateVO, iaasTemplate);

        Long userId = Sign.getUserId();
        long id = SnowFlakeIdGenerator.getInstance().nextId();
        iaasTemplate.setId(id);
        iaasTemplate.setCreateTime(DateUtil.getNow());
        iaasTemplate.setCreateUser(userId);
        iaasTemplate.setUpdateTime(DateUtil.getNow());
        iaasTemplate.setUpdateUser(userId);
        //默认保存状态
        iaasTemplate.setState(1);

        try {
            iTemplateService.saveIaasTemplate(iaasTemplate);
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            baseResponse.setTemplateId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } catch (Exception e) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        }
    }

    /**
     * 发布、撤销发布
     *
     * @param templateVO
     * @return
     */
    @PostMapping("/update_template")
    @ApiOperation(value = "修改模板", notes = "修改模板")
    public ResponseEntity<BaseResponse> updateTemplate(@RequestBody IaasTemplateVO templateVO) {
        BaseResponse baseResponse = new BaseResponse();

        //撤销发布时，验证是否被服务目录使用
        if (templateVO != null && templateVO.getState() == 1) {
            List<IaasCatalogVO> checkTemplate = iTemplateService.checkTemplateIFUse(templateVO.getId());
            if (CollectionUtils.isNotEmpty(checkTemplate)) {
                baseResponse.setCode(201);
                baseResponse.setMessage("模板已被使用不能撤回发布！");
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
            }
        }
        //对象赋值
        IaasTemplate iaasTemplate = new IaasTemplate();
        BeanUtils.copyProperties(templateVO, iaasTemplate);

        Long userId = Sign.getUserId();
        iaasTemplate.setUpdateTime(DateUtil.getNow());
        iaasTemplate.setUpdateUser(userId);

        try {
            iTemplateService.updateTemplate(iaasTemplate);

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("修改成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } catch (Exception e) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改失败");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        }
    }

    /**
     * 删除模板信息
     *
     * @param templateId
     * @return
     */
    @GetMapping("/delete_template")
    @ApiOperation(value = "删除模板信息", notes = "删除模板信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "templateId", value = "模板id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<BaseResponse> deleteTemplate(@RequestParam(value = "templateId") Long templateId) {
        BaseResponse baseResponse = new BaseResponse();

        //撤销发布时，验证是否被服务目录使用
        if (templateId != null) {
            List<IaasCatalogVO> checkTemplate = iTemplateService.checkTemplateIFUse(templateId);
            if (CollectionUtils.isNotEmpty(checkTemplate)) {
                baseResponse.setCode(201);
                baseResponse.setMessage("模板已被使用不能删除！");
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
            }
        }
        iTemplateService.deleteTemplate(templateId);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    /**
     * 查询发布模板信息
     *
     * @return
     */
    @GetMapping("/query_template")
    @ApiOperation(value = "查询发布模板", notes = "查询发布模板")
    public ResponseEntity<List<IaasTemplateVO>> queryMachineTree() {
        List<IaasTemplateVO> treeResponses = iTemplateService.queryTemplate();
        return ResponseEntity.status(HttpStatus.OK).body(treeResponses);
    }

    /**
     * 查询虚拟机及组件信息
     *
     * @param templateId
     * @return
     */
    @GetMapping("/query_machine_tree")
    @ApiOperation(value = "查询虚拟机树形", notes = "查询虚拟机树形")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "templateId", value = "模板id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<List<TemplateVirtualMachineTreeResponse>> queryMachineTree(@RequestParam(value = "templateId") Long templateId) {
        List<TemplateVirtualMachineTreeResponse> treeResponses = iTemplateService.queryMachineTree(templateId);
        return ResponseEntity.status(HttpStatus.OK).body(treeResponses);
    }

    @PostMapping("/add_machine_component")
    @ApiOperation(value = "新增虚拟机组件及组件参数信息", notes = "新增虚拟机组件及组件参数信息")
    public ResponseEntity<BaseResponse> saveTemplateMachineComponent(@RequestBody(required = false) List<TemplateVirtualMachineTreeResponse> vos) {
        BaseResponse baseResponse = iTemplateService.saveTemplateMachineComponent(vos);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/modify_machine_component")
    @ApiOperation(value = "编辑虚拟机组件及组件参数信息", notes = "编辑虚拟机组件及组件参数信息")
    public ResponseEntity<BaseResponse> editTemplateMachineComponent(@RequestBody(required = false) List<TemplateVirtualMachineTreeResponse> vos) {
        BaseResponse baseResponse = iTemplateService.editTemplate(vos);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }
}
