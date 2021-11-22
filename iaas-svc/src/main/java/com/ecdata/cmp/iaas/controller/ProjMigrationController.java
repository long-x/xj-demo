package com.ecdata.cmp.iaas.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.dto.response.*;
import com.ecdata.cmp.iaas.entity.dto.transferCloud.ProjMigrationVO;
import com.ecdata.cmp.iaas.entity.transferCloud.ProjMigration;
import com.ecdata.cmp.iaas.service.ProjMigrationService;
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
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author ZhaoYX
 * @since 2019/11/19 15:33,
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/migrate")
@Api(tags = "项目上云的API")
public class ProjMigrationController {
    
    @Autowired
    private ProjMigrationService projMigrationService;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询上云接口", notes = "根据id查询上云接口")
    @ApiImplicitParam(name = "id", value = "上云接口id", required = true, paramType = "path")
    public ResponseEntity<ProjMigrationResponse> getById(@PathVariable(name = "id") Long id) {
        ProjMigration migrate = projMigrationService.getById(id);
        ProjMigrationVO migrateVO = new ProjMigrationVO();
        BeanUtils.copyProperties(migrate, migrateVO);
        return ResponseEntity.status(HttpStatus.OK).body(new ProjMigrationResponse(migrateVO));
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查看上云接口", notes = "分页查看上云接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "supervisorName", value = "主管单位", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "资源状态", paramType = "query", dataType = "string")
    })
    public ResponseEntity<ProjMigrationMapResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                         @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) String supervisorName,
                                                         @RequestParam(required = false) String status) {
        //拼接条件
        QueryWrapper<ProjMigration> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(ProjMigration::getBudgetName, keyword).or().like(ProjMigration::getProjectName, keyword)
                    .or().like(ProjMigration::getSupervisorName, keyword);
        }
        if(StringUtils.isNotEmpty(supervisorName)){
            queryWrapper.lambda().eq(ProjMigration::getSupervisorName,supervisorName);
        }
        if(StringUtils.isNotEmpty(status)){
            queryWrapper.lambda().eq(ProjMigration::getStatus,status);
        }
        queryWrapper.lambda().orderByDesc(ProjMigration::getUpdateTime);
        //分页查询
        Page<ProjMigration> page = new Page<>(pageNo, pageSize);
        IPage<ProjMigration> result = projMigrationService.page(page, queryWrapper);
        List<ProjMigrationVO> migrateVOList = new ArrayList<>();
        List<ProjMigration> migrateList = result.getRecords();
        if (migrateList != null && migrateList.size() > 0) {
            for (ProjMigration migrate : migrateList) {
                ProjMigrationVO migrateVo = new ProjMigrationVO();
                BeanUtils.copyProperties(migrate, migrateVo);
                migrateVOList.add(migrateVo);
            }
        }
        migrateVOList.forEach(System.out::println);
        //查询条件下拉框
        Map<String, Object> conditions =projMigrationService.selectConditions();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("pageData",new PageVO<>(result, migrateVOList));
        map.put("selectConditions",conditions);
        //带下拉条件的分页
        return ResponseEntity.status(HttpStatus.OK).body(new ProjMigrationMapResponse(map));
                //(new ProjMigrationPageResponse(new PageVO<>(result, migrateVOList)));


    }


    @GetMapping("/list")
    @ApiOperation(value = "获取上云接口列表", notes = "获取上云接口列表")
    public ResponseEntity<ProjMigrationListResponse> list() {
        List<ProjMigration> projMigrationList = projMigrationService.list();
        List<ProjMigrationVO> projMigrationVOList = new ArrayList<>();
        if (projMigrationList != null && projMigrationList.size() > 0) {
            for (ProjMigration projMigration : projMigrationList) {
                ProjMigrationVO projMigrationVO = new ProjMigrationVO();
                BeanUtils.copyProperties(projMigration, projMigrationVO);
                projMigrationVOList.add(projMigrationVO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ProjMigrationListResponse(projMigrationVOList));

    }
    

    @PostMapping("/add")
    @ApiOperation(value = "新增上云接口", notes = "新增上云接口")
    public ResponseEntity<BaseResponse> add(@RequestBody ProjMigrationVO migrateVo) {
        BaseResponse baseResponse = new BaseResponse();
        ProjMigration migrate = new ProjMigration();
        BeanUtils.copyProperties(migrateVo, migrate);
        migrate.setId(SnowFlakeIdGenerator.getInstance().nextId());
        migrate.setCreateUser(Sign.getUserId());
        migrate.setCreateTime(DateUtil.getNow());
        migrate.setApplyTime(DateUtil.getNow());
        migrate.setUpdateUser(Sign.getUserId());
        migrate.setUpdateTime(DateUtil.getNow());
        if (projMigrationService.save(migrate)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("添加失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改上云接口", notes = "修改上云接口")
    public ResponseEntity<BaseResponse> update(@RequestBody ProjMigrationVO migrateVo) {
        BaseResponse baseResponse = new BaseResponse();
        ProjMigration migrate = new ProjMigration();
        BeanUtils.copyProperties(migrateVo, migrate);
        Long id = migrate.getId();
        if (id == null) {
            baseResponse.setResultEnum(ResultEnum.MISS_PRIMARY_KEY);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
        QueryWrapper<ProjMigration> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProjMigration::getId, id);
        String proName = migrate.getProjectName();
//        if (proName == null) {
//            queryWrapper.lambda().isNull(ProjMigration::getProjectName);
//        } else {
//            queryWrapper.lambda().eq(ProjMigration::getId, proName);
//        }
        int count = this.projMigrationService.count(queryWrapper);
        if (count == 0) {
            //没找到满足条件的记录
            System.out.println(count);
            baseResponse.setMessage("更新失败，未找到该记录，不符合更新要求");
        }
        migrate.setUpdateUser(Sign.getUserId());
        migrate.setUpdateTime(DateUtil.getNow());
        if (projMigrationService.updateById(migrate)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("更新失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }
    }



    @PutMapping("/remove/{id}")
    @ApiOperation(value = "删除", notes = "删除上云接口")
    @ApiImplicitParam(name = "id", value = "上云接口id", required = true, paramType = "path")
    public ResponseEntity<BaseResponse> remove(@PathVariable(name = "id") Long id) {
        log.info("删除上云接口 migrate id：{}", id);
        BaseResponse baseResponse = new BaseResponse();
        ProjMigration migrate = projMigrationService.getById(id);
        migrate.setUpdateTime(DateUtil.getNow());
        migrate.setUpdateUser(Sign.getUserId());
        projMigrationService.updateById(migrate);
        if (projMigrationService.removeById(id)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        } else {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        }

    }

    @PutMapping("/remove_batch")
    @ApiOperation(value = "批量删除", notes = "批量删除上云接口(逻辑删除)")
    public ResponseEntity<BaseResponse> removeBatch(@RequestParam(name = "ids") String ids) {
        log.info("删除用户 ids：{}", ids);
        BaseResponse baseResponse = new BaseResponse();
        String[] idArray = ids.split(",");
        if (StringUtils.isEmpty(ids)) {
            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("参数不识别");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
        } else {
            //修改时间和修改人
            for (String id : idArray) {
                ProjMigration migrate =projMigrationService.getById(id);
                migrate.setUpdateTime(DateUtil.getNow());
                migrate.setUpdateUser(Sign.getUserId());
                projMigrationService.updateById(migrate);
            }
            //逻辑删除
            projMigrationService.removeByIds(Arrays.asList(idArray));

            baseResponse.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            baseResponse.setMessage("删除成功");
            return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
        }
    }

    @GetMapping("/count")
    @ApiOperation(value = "获取上云接口数", notes = "获取上云接口数")
    public ResponseEntity<BaseResponse> count(){ //@RequestBody ProjMigrationVO migrateVo
        ProjMigrationCountResponse response = new ProjMigrationCountResponse();
        QueryWrapper<ProjMigration> queryWrapper = new QueryWrapper<>();
        ProjMigration migrate = new ProjMigration();

        if(migrate.getStatus()!=null)
           queryWrapper.lambda();//.eq(ProjMigration::getStatus, migrate.getStatus());
//https://blog.csdn.net/weixin_38111957/article/details/91539019   自定义sql
        //后4个已分配，其它未分配
        List<Map<String,Object>> counts =projMigrationService.selectByMyWrapper(queryWrapper);
        System.out.println(counts);
//        Integer count = projMigrationService.count(queryWrapper);
        if(counts!=null) {
            response.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            response.setMessage("统计成功");
            response.setData(counts);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{

            response.setData(counts);
            response.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
            response.setMessage("统计失败");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    /**
     * double那边出错了
     * @return
     */
    @GetMapping("/percentage")
    @ApiOperation(value = "上云接口百分比", notes = "上云接口百分比")
    public ResponseEntity<BaseResponse> percentage(){   //@RequestBody ProjMigrationVO migrateVo
        ProjMigrationPercentResponse response = new ProjMigrationPercentResponse();
        QueryWrapper<ProjMigration> queryWrapper = new QueryWrapper<>();
        //总数
        Integer count = this.projMigrationService.count(queryWrapper);
        log.info("percentage===count "+count);
        //某状态数量
        queryWrapper.lambda().ge(ProjMigration::getStatus,2);
        Integer applyCount = projMigrationService.count(queryWrapper);
//            百分比
        log.info("percentage===applyCount "+applyCount);
        double temp =count>0?((float)applyCount/(float)count)*100:0.0;
        log.info("percentage===temp "+temp);
        DecimalFormat df = new DecimalFormat("0.00");
        String percentage = df.format(temp);
        log.info("percentage===percentage "+percentage);
        System.out.println(count+" "+applyCount+" "+temp+" "+percentage);
        //返回响应
        double res=Double.parseDouble(percentage);
        response.setData(res);
        response.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        response.setMessage("统计成功");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }




}
