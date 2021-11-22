package com.ecdata.cmp.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.user.dto.PythonVO;
import com.ecdata.cmp.user.dto.response.PythonListResponse;
import com.ecdata.cmp.user.dto.response.PythonPageResponse;
import com.ecdata.cmp.user.dto.response.PythonResponse;
import com.ecdata.cmp.user.entity.Python;
import com.ecdata.cmp.user.service.IPythonService;
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
 * @since 2019-11-22
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/python")
@Api(tags = "python相关接口")
public class PythonController {

    /**
     * pythonService
     */
    @Autowired
    private IPythonService pythonService;


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询python", notes = "根据id查询python")
    @ApiImplicitParam(name = "id", value = "pythonid", required = true, paramType = "path")
    public ResponseEntity<PythonResponse> getById(@PathVariable(name = "id") Long id) {
        PythonVO pythonVO = new PythonVO();
        Python python = pythonService.getById(id);
        BeanUtils.copyProperties(python, pythonVO);
        return ResponseEntity.status(HttpStatus.OK).body(new PythonResponse(pythonVO));

    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查看python", notes = "分页查看python")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20")
    })
    public ResponseEntity<PythonPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                   @RequestParam(defaultValue = "20", required = false) Integer pageSize) {
        QueryWrapper<Python> queryWrapper = new QueryWrapper<>();
        Page<Python> page = new Page<>(pageNo, pageSize);
        IPage<Python> result = pythonService.page(page, queryWrapper);
        List<PythonVO> pythonVOList = new ArrayList<>();
        List<Python> pythonList = result.getRecords();
        if (pythonList != null && pythonList.size() > 0) {
            for (Python python : pythonList) {
                PythonVO pythonVO = new PythonVO();
                BeanUtils.copyProperties(python, pythonVO);
                pythonVOList.add(pythonVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new PythonPageResponse(new PageVO<>(result, pythonVOList)));

    }

    @GetMapping("/list")
    @ApiOperation(value = "获取python列表", notes = "获取python列表")
    @ApiImplicitParam(name = "type", value = "类型(1:默认类型)", paramType = "query", dataType = "int")
    public ResponseEntity<PythonListResponse> list(@RequestParam(required = false) Integer type) {
        List<PythonVO> pythonVOList = new ArrayList<>();
        QueryWrapper<Python> queryWrapper = new QueryWrapper<>();
        if (type != null) {
            queryWrapper.lambda().eq(Python::getType, type);
        }
        List<Python> pythonList = pythonService.list(queryWrapper);
        if (pythonList != null && pythonList.size() > 0) {
            for (Python python : pythonList) {
                PythonVO pythonVO = new PythonVO();
                BeanUtils.copyProperties(python, pythonVO);
                pythonVOList.add(pythonVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new PythonListResponse(pythonVOList));

    }

    @PostMapping("/add")
    @ApiOperation(value = "新增python", notes = "新增python")
    public ResponseEntity<BaseResponse> add(@RequestBody PythonVO pythonVO) {
        BaseResponse baseResponse = new BaseResponse();
        Python python = new Python();
        BeanUtils.copyProperties(pythonVO, python);
        python.setId(SnowFlakeIdGenerator.getInstance().nextId())
                .setCreateUser(Sign.getUserId())
                .setCreateTime(DateUtil.getNow());
        if (pythonService.save(python)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加python成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加python失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/update")
    @ApiOperation(value = "修改python", notes = "修改python")
    public ResponseEntity<BaseResponse> update(@RequestBody PythonVO pythonVO) {
        BaseResponse baseResponse = new BaseResponse();
        Python python = new Python();
        BeanUtils.copyProperties(pythonVO, python);
        if (python.getId() == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        python.setUpdateUser(Sign.getUserId());
        python.setUpdateTime(DateUtil.getNow());
        if (pythonService.updateById(python)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新python成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加python失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/remove")
    @ApiOperation(value = "删除", notes = "删除python")
    public ResponseEntity<BaseResponse> remove(@RequestParam Long id) {
        log.info("删除python python id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        if (pythonService.removeById(id)) {
            pythonService.modifyUpdateRecord(id, Sign.getUserId());
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除python成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除python失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/remove_batch")
    @ApiOperation(value = "批量删除", notes = "批量删除python")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("删除python ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            this.pythonService.removeByIds(Arrays.asList(idArray));
            for (String id : idArray) {
                this.pythonService.modifyUpdateRecord(Long.parseLong(id), Sign.getUserId());
            }
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除python成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }
}
