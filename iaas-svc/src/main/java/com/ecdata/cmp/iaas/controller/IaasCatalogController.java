package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.catalog.IaasCatalog;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVO;
import com.ecdata.cmp.iaas.entity.dto.response.catalog.CatalogMachineComponentResponse;
import com.ecdata.cmp.iaas.entity.dto.response.catalog.IaasCatalogPageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.catalog.IaasCatalogResponse;
import com.ecdata.cmp.iaas.service.ICatalogService;
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
 * @create 2019-11-27 15:14
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/iaas_catalog")
@Api(tags = "服务目录相关的API")
public class IaasCatalogController {
    @Autowired
    private ICatalogService iCatalogService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查看服务目录", notes = "分页查看服务目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "catalogName", value = "服务目录名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "state", value = "服务目录状态", paramType = "query", dataType = "String")
    })
    public ResponseEntity<IaasCatalogPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                        @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                        @RequestParam(required = false) String catalogName,
                                                        @RequestParam(required = false) String order,
                                                        @RequestParam(required = false) Long state) {
        Page<IaasCatalogVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> params = new HashMap<>();
        params.put("catalogName", catalogName);
        params.put("state", state);
        params.put("order", order);
        IPage<IaasCatalogVO> result = iCatalogService.queryIaasCatalog(page, params);
        return ResponseEntity.status(HttpStatus.OK).body(new IaasCatalogPageResponse(new PageVO<>(result)));

    }

    @PostMapping("/add")
    @ApiOperation(value = "添加服务目录", notes = "添加服务目录")
    public ResponseEntity<IaasCatalogResponse> saveCatalog(@RequestBody IaasCatalogVO catalogVO) {
        IaasCatalogResponse baseResponse = new IaasCatalogResponse();

        //校验必填项
        if (StringUtils.isEmpty(catalogVO.getCatalogName())) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("请输入服务目录！");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

        //对象赋值
        IaasCatalog iaasCatalog = new IaasCatalog();
        BeanUtils.copyProperties(catalogVO, iaasCatalog);

        Long userId = Sign.getUserId();
        long id = SnowFlakeIdGenerator.getInstance().nextId();
        iaasCatalog.setId(id);
        iaasCatalog.setCreateTime(DateUtil.getNow());
        iaasCatalog.setCreateUser(userId);
        iaasCatalog.setUpdateTime(DateUtil.getNow());
        iaasCatalog.setUpdateUser(userId);
        iaasCatalog.setState(1);

        try {
            iCatalogService.savaIaasCatalog(iaasCatalog);
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功!");
            baseResponse.setCatalogId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } catch (Exception e) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加失败!");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        }
    }

    /**
     * 删除、发布、撤销发布
     *
     * @param catalogVO
     * @return
     */
    @PostMapping("/update_catalog")
    @ApiOperation(value = "修改服务目录", notes = "修改服务目录")
    public ResponseEntity<BaseResponse> updateCatalog(@RequestBody IaasCatalogVO catalogVO) {
        BaseResponse baseResponse = new BaseResponse();

        //撤销发布时，验证是否被服务目录使用
        if (catalogVO != null && catalogVO.getState() == 1) {
            List<IaasProcessApplyVO> checkCatalog = iCatalogService.checkCatalogIFUse(catalogVO.getId());
            if (CollectionUtils.isNotEmpty(checkCatalog)) {
                baseResponse.setCode(201);
                baseResponse.setMessage("服务目录已被使用不能撤回发布！");
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
            }
        }

        //对象赋值
        IaasCatalog iaasCatalog = new IaasCatalog();
        BeanUtils.copyProperties(catalogVO, iaasCatalog);

        Long userId = Sign.getUserId();
        iaasCatalog.setUpdateTime(DateUtil.getNow());
        iaasCatalog.setUpdateUser(userId);

        try {
            iCatalogService.updateCatalog(iaasCatalog);

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("修改成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } catch (Exception e) {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("修改失败");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        }
    }

    @PostMapping("/add_catalog_machine_component")
    @ApiOperation(value = "新增虚拟机组件及组件参数信息", notes = "新增虚拟机组件及组件参数信息")
    public ResponseEntity<BaseResponse> saveTemplateMachineComponent(@RequestBody(required = false) CatalogMachineComponentResponse catalogMachineComponent) {
        BaseResponse baseResponse = iCatalogService.saveCatalogMachineComponent(catalogMachineComponent);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    @PostMapping("/modify_catalog_machine_component")
    @ApiOperation(value = "编辑虚拟机组件及组件参数信息", notes = "编辑虚拟机组件及组件参数信息")
    public ResponseEntity<BaseResponse> editTemplateMachineComponent(@RequestBody(required = false) CatalogMachineComponentResponse catalogMachineComponent) {
        BaseResponse baseResponse = iCatalogService.editCatalogMachineComponent(catalogMachineComponent);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
    }

    /**
     * 查询虚拟机及组件信息
     *
     * @param catalogId
     * @return
     */
    @GetMapping("/query_machine_tree")
    @ApiOperation(value = "查询虚拟机树形", notes = "查询虚拟机树形")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "catalogId", value = "模板id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<CatalogMachineComponentResponse> queryMachineTree(@RequestParam(value = "catalogId") Long catalogId) {
        CatalogMachineComponentResponse treeResponses = iCatalogService.queryMachineTree(catalogId);
        return ResponseEntity.status(HttpStatus.OK).body(treeResponses);
    }

    /**
     * 删除服务目录信息
     *
     * @param catalogId
     * @return
     */
    @GetMapping("/delete_catalog")
    @ApiOperation(value = "删除服务目录信息", notes = "删除服务目录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "catalogId", value = "模板id", paramType = "query", dataType = "Long")
    })
    public ResponseEntity<BaseResponse> deleteTemplate(@RequestParam(value = "catalogId") Long catalogId) {
        BaseResponse baseResponse = new BaseResponse();

        //撤销发布时，验证是否被服务目录使用
        if (catalogId != null) {
            List<IaasProcessApplyVO> checkCatalog = iCatalogService.checkCatalogIFUse(catalogId);
            if (CollectionUtils.isNotEmpty(checkCatalog)) {
                baseResponse.setCode(201);
                baseResponse.setMessage("服务目录已被使用不能撤回发布！");
                return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
            }
        }
        iCatalogService.deleteCatalog(catalogId);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

}
