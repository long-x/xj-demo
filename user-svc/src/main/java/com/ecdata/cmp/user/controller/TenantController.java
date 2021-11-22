package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.TenantVO;
import com.ecdata.cmp.user.dto.response.TenantListResponse;
import com.ecdata.cmp.user.dto.response.TenantPageResponse;
import com.ecdata.cmp.user.dto.response.TenantResponse;
import com.ecdata.cmp.user.entity.Tenant;
import com.ecdata.cmp.user.service.ITenantService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-05-07
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/tenant")
@Api(tags = "租户相关接口")
public class TenantController {

    /**
     * 租户Service
     */
    @Autowired
    private ITenantService tenantService;


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询租户", notes = "根据id查询租户")
    @ApiImplicitParam(name = "id", value = "租户id", required = true, paramType = "path")
    public ResponseEntity<TenantResponse> getById(@PathVariable(name = "id") Long id) {
        TenantVO tenantVO = new TenantVO();
        Tenant tenant = tenantService.getById(id);
        BeanUtils.copyProperties(tenant, tenantVO);
        return ResponseEntity.status(HttpStatus.OK).body(new TenantResponse(tenantVO));

    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查看租户", notes = "分页查看租户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<TenantPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                   @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                   @RequestParam(required = false) String keyword) {
        Page<TenantVO> page = new Page<>(pageNo, pageSize);
        IPage<TenantVO> result = tenantService.qryTenantInfo(page, keyword);
        return ResponseEntity.status(HttpStatus.OK).body(new TenantPageResponse(new PageVO<>(result)));

    }

    @GetMapping("/list")
    @ApiOperation(value = "获取租户列表", notes = "获取租户列表")
    public ResponseEntity<TenantListResponse> list() {
        List<TenantVO> tenantVOList = new ArrayList<>();
        List<Tenant> tenantList = tenantService.list();
        if (tenantList != null && tenantList.size() > 0) {
            for (Tenant tenant : tenantList) {
                TenantVO tenantVO = new TenantVO();
                BeanUtils.copyProperties(tenant, tenantVO);
                tenantVOList.add(tenantVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TenantListResponse(tenantVOList));

    }

    @PostMapping("/add")
    @ApiOperation(value = "新增租户", notes = "新增租户")
    public ResponseEntity<BaseResponse> add(@RequestBody TenantVO tenantVO) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        if (tenantVO.getParentId() == null) {
            baseResponse.setCode(ResultEnum.PARAM_MISS.getCode());
            baseResponse.setMessage("缺少父租户");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantVO, tenant);
        Long userId = Sign.getUserId();
        tenant.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(userId)
                .setCreateTime(DateUtil.getNow());
        if (tenantService.save(tenant)) {
            tenantService.initUser(tenant);
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加租户成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_FAIL.getCode());
            baseResponse.setMessage("添加租户失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改租户", notes = "修改租户")
    public ResponseEntity<BaseResponse> update(@RequestBody TenantVO tenantVO) {
        BaseResponse baseResponse = new BaseResponse();
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantVO, tenant);
        if (tenant.getId() == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        final Long topTenantId = 10000L;
        if (!topTenantId.equals(tenant.getId()) && tenant.getParentId() == null) {
            baseResponse.setResultEnum(ResultEnum.PARAM_MISS);
            baseResponse.setMessage("缺少父租户");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        tenant.setUpdateUser(Sign.getUserId());
        tenant.setUpdateTime(DateUtil.getNow());
        if (tenantService.updateById(tenant)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新租户成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加租户失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除租户")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        BaseResponse baseResponse = new BaseResponse();
        log.info("删除租户 tenant id：{}", id);
        if (tenantService.removeById(id)) {
            tenantService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除租户成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除租户失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/removeBatch")
    @ApiOperation(value = "批量删除", notes = "批量删除租户")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        BaseResponse baseResponse = new BaseResponse();
        log.info("删除租户 ids：{}", ids);
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            this.tenantService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.tenantService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("批量删除租户成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }
}
