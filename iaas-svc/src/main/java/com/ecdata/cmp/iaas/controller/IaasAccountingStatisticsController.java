package com.ecdata.cmp.iaas.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.common.utils.ExcelUtils;
import com.ecdata.cmp.iaas.entity.dto.ChargingVO;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingStatisticsExcelVO;
import com.ecdata.cmp.iaas.entity.dto.IaasAccountingStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasAccountingStatisticsPageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasChargingListResponse;
import com.ecdata.cmp.iaas.entity.dto.vm.VMGroupVO;
import com.ecdata.cmp.iaas.service.IaasAccountingRulesService;
import com.ecdata.cmp.iaas.service.IaasAccountingStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/7 11:04
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/accounting_statistics")
@Api(tags = "计量计费统计")
public class IaasAccountingStatisticsController {

    @Autowired
    private IaasAccountingRulesService accountingRulesService;

    @Autowired
    private IaasAccountingStatisticsService statisticsService;


    @GetMapping("/page")
    @ApiOperation(value = "分页查看计费统计信息", notes = "分页查看计费统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "startTime", value = "起始时间(yyyy-MM-dd)", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "endTime", value = "结束时间(yyyy-MM-dd)", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "statistics", value = "统计口径(天/年/月)", paramType = "query", dataType = "string"),
    })
    public ResponseEntity<IaasAccountingStatisticsPageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                                       @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                                       @RequestParam(required = false) String startTime,
                                                                       @RequestParam(required = false) String endTime,
                                                                       @RequestParam(required = false) String statistics
    ) {
        Page<IaasAccountingStatisticsVO> page = new Page<>(pageNo, pageSize);
        //封装查询条件map
        Map<String, Object> params = new HashMap<>();
        if (startTime != null) {
            params.put("startTime", startTime);
        }
        if (endTime != null) {
            params.put("endTime", endTime);
        }
        if (statistics != null) {
            params.put("statistics", statistics);
        }
        Long userId = Sign.getUserId();
        if (userId != null) {
            params.put("userId", userId);
        }
        IPage<IaasAccountingStatisticsVO> iPage = statisticsService.qrStatisticsList(page, params);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasAccountingStatisticsPageResponse(new PageVO<>(iPage)));
    }



    @GetMapping("/get_excel_bms")
    @ApiOperation(notes ="导出excel",value = "导出excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "起始时间(yyyy-MM-dd)", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "endTime", value = "结束时间(yyyy-MM-dd)", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "statistics", value = "统计口径(天/年/月)", paramType = "query", dataType = "string"),
    })
    public void Export(@RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) String statistics,HttpServletResponse response) {
        //获取数据
        Map<String, Object> params = new HashMap<>();
        if (startTime != null) {
            params.put("startTime", startTime);
        }
        if (endTime != null) {
            params.put("endTime", endTime);
        }
        if (statistics != null) {
            params.put("statistics", statistics);
        }
        Long userId = Sign.getUserId();
        if (userId != null) {
            params.put("userId", userId);
        }
        List<IaasAccountingStatisticsExcelVO> list = statisticsService.export(params);


        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), IaasAccountingStatisticsExcelVO.class, list);
        try {

            //response为HttpServletResponse对象
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");

            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            String names = java.net.URLEncoder.encode("excel_bms", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + names + ".xls");

            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

// cn.hutool 不兼容  统一使用easy-poi
//        String[] columnNames = { "组织名称", "有效时间","cpu核数","cpu单价","内存数量","内存单价",
//                "磁盘数量","磁盘单价","裸金属类型1数量","裸金属类型1单价","裸金属类型2数量","裸金属类型2单价","合计"};
//
//        String[] keys = {"orgName", "effectiveDate","cpuCount", "cpuUnitPrice", "memoryCount",
//        "memoryUnitPrice","diskCount","diskUnitPrice","bmsType1Count","bmsType1UnitPrice","bmsType2Count",
//                "bmsType2UnitPrice","money"};
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        ExcelUtils.export(response, "计量计费"+ sdf.format(new Date()) + ".xls", list, columnNames, keys);


    }




    @GetMapping("/charging_list")
    @ApiOperation(value = "按照业务组计费统计列表", notes = "按照业务组计费统计列表")
    public ResponseEntity<IaasChargingListResponse> list() {
//        IaasChargingListResponse response = new IaasChargingListResponse();
        List<ChargingVO> chargingVOS = statisticsService.qrCharging();
//        response.setData(chargingVOS);
        return ResponseEntity.status(HttpStatus.OK).body(new IaasChargingListResponse(chargingVOS));
    }



    @GetMapping("/all_list")
    @ApiOperation(value = "插入计费数据", notes = "插入计费数据")
    public ResponseEntity<BaseResponse> allList() {
        boolean b = statisticsService.qrInfo();
        if (b){
            return ResponseEntity.status(HttpStatus.OK).body(
                    BaseResponse.builder()
                            .code(ResultEnum.DEFAULT_SUCCESS.getCode()).message("is ok").build());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse.builder()
                        .code(ResultEnum.DEFAULT_FAIL.getCode()).message("is ok").build());

    }



    }
