package com.ecdata.cmp.iaas.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.distribution.DeptCounExcelVO;
import com.ecdata.cmp.iaas.entity.dto.employ.EmployVO;
import com.ecdata.cmp.iaas.entity.dto.excel.TrackingStatementDayVO;
import com.ecdata.cmp.iaas.entity.dto.excel.TrackingStatementVO;
import com.ecdata.cmp.iaas.entity.dto.report.CalculationResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.report.CloudResourceAssignedStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.report.CloudResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.response.StatisticsVOListResponse;
import com.ecdata.cmp.iaas.entity.dto.statistics.StatisticsVO;
import com.ecdata.cmp.iaas.service.IaasApplyReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xuxiaojian
 * @date 2020/5/11 17:12
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/cloud-report")
@Api(tags = "?????????????????????API")
public class IaasApplyReportController {
    @Autowired
    private IaasApplyReportService applyReportService;

    @GetMapping("/cloud_resource_statistics")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    public ResponseEntity<List<CloudResourceStatisticsVO>> cloudResourceStatistics(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "businessName", required = false) String businessName) {
        List<CloudResourceStatisticsVO> iaasAreaVOS = applyReportService.cloudResourceStatistics(type, businessName);
        return ResponseEntity.status(HttpStatus.OK).body(iaasAreaVOS);
    }


    @GetMapping("/cloud_resource_statistics_excel")
    @ApiOperation(value = "?????????????????????????????????", notes = "?????????????????????????????????")
    public void cloudResourceStatisticsExcel(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "businessName", required = false) String businessName,HttpServletResponse response) {
        List<CloudResourceStatisticsVO> iaasAreaVOS = applyReportService.cloudResourceStatistics(type, businessName);


        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), CloudResourceStatisticsVO.class, iaasAreaVOS);
        try {

            //response???HttpServletResponse??????
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");

            //test.xls??????????????????????????????????????????????????????????????????????????????
            String names = java.net.URLEncoder.encode("cloud_resource", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + names + ".xls");

            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/cloud_resource_assigned_statistics")
    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
    public ResponseEntity<List<CloudResourceAssignedStatisticsVO>> cloudResourceAssignedStatistics(
            @RequestParam(name = "businessId", required = false) String businessId,
            @RequestParam(name = "areaId", required = false) String areaId,
            @RequestParam(name = "businessName", required = false) String businessName) {
        Map<String, Object> parm = new HashMap<>();
        parm.put("areaId", areaId);
        parm.put("businessId", businessId);
        parm.put("businessName", businessName);
        List<CloudResourceAssignedStatisticsVO> iaasAreaVOS = applyReportService.cloudResourceAssignedStatistics(parm);
        return ResponseEntity.status(HttpStatus.OK).body(iaasAreaVOS);
    }

//    @GetMapping("/calculation_resource_statistics")
//    @ApiOperation(value = "?????????????????????", notes = "?????????????????????")
//    public ResponseEntity<List<CalculationResourceStatisticsVO>> calculationResourceStatistics() {
//        Map<String, Object> parm = new HashMap<>();
//        List<CalculationResourceStatisticsVO> iaasAreaVOS = applyReportService.calculationResourceStatistics(parm);
//        return ResponseEntity.status(HttpStatus.OK).body(iaasAreaVOS);
//    }
//
//
//    @GetMapping("/calculation_resource_statistics_excel")
//    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????")
//    public void calculationResourceStatisticsExcel(HttpServletResponse response) {
//        Map<String, Object> parm = new HashMap<>();
//        List<CalculationResourceStatisticsVO> iaasAreaVOS = applyReportService.calculationResourceStatistics(parm);
//
//        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), CalculationResourceStatisticsVO.class, iaasAreaVOS);
//        try {
//
//            //response???HttpServletResponse??????
//            response.setHeader("content-type", "application/octet-stream");
//            response.setContentType("application/octet-stream");
//
//            //test.xls??????????????????????????????????????????????????????????????????????????????
//            String names = java.net.URLEncoder.encode("calculation_resource", "UTF-8");
//            response.setHeader("Content-Disposition", "attachment;filename=" + names + ".xls");
//
//            workbook.write(response.getOutputStream());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


    @GetMapping("/queryTowDep")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<List<Map<String,Object>>> queryTowDep() {
        List<Map<String,Object>> iaasAreaVOS = applyReportService.queryTowDep();
        return ResponseEntity.status(HttpStatus.OK).body(iaasAreaVOS);
    }

    @GetMapping("/queryArea")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseEntity<List<Map<String,Object>>> queryArea() {
        List<Map<String,Object>> iaasAreaVOS = applyReportService.queryArea();
        return ResponseEntity.status(HttpStatus.OK).body(iaasAreaVOS);
    }

    @GetMapping("/security_resource_usage_report_excel")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    public void securityResourceUsageReportExcel(HttpServletResponse response) {
        Map<String, Object> parm = new HashMap<>();
        List<CalculationResourceStatisticsVO> iaasAreaVOS = applyReportService.securityResourceUsageReport(parm);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), CalculationResourceStatisticsVO.class, iaasAreaVOS);
        try {

            //response???HttpServletResponse??????
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");

            //test.xls??????????????????????????????????????????????????????????????????????????????
            String names = java.net.URLEncoder.encode("security_resource", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + names + ".xls");

            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/security_resource_usage_report")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    public ResponseEntity<List<CalculationResourceStatisticsVO>> securityResourceUsageReport() {
        Map<String, Object> parm = new HashMap<>();
        List<CalculationResourceStatisticsVO> iaasAreaVOS = applyReportService.securityResourceUsageReport(parm);
        return ResponseEntity.status(HttpStatus.OK).body(iaasAreaVOS);
    }


    @GetMapping("/resource_tracking_statement")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    public ResponseEntity<List> cloudResourceAssignedStatistics(
            @RequestParam(name = "time", required = false) String time,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "statistics", required = false) String statistics,
            @RequestParam(name = "businessName", required = false) String businessName,
            @RequestParam(name = "resourceName", required = false) String resourceName) {


        Map<String, String> parm = new HashMap<>();

        Optional.ofNullable(time).filter(StringUtils::isNotBlank).ifPresent(e -> parm.put("time", e)); //??????
        Optional.ofNullable(statistics).filter(StringUtils::isNotBlank).ifPresent(e -> parm.put("statistics", e)); //?????? ???/???

        List<Map<String, String>> list = applyReportService.resourceTrackingStatement1(parm);

        Map<String, String> parm2 = new HashMap<>();
        Optional.ofNullable(businessName).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("businessName", e)); //????????????
        Optional.ofNullable(resourceName).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("resourceName", e)); //????????????
        Optional.ofNullable(type).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("type", e)); //???????????? ??????/??????

        if(MapUtils.isNotEmpty(parm2)) {
            for (Map.Entry<String, String> entry : parm2.entrySet()) {
                String paramKey = entry.getKey();
                String paramValue = entry.getValue();

                list = list.stream().filter(e -> {
                    String responseValue = e.get(paramKey);
                    return StringUtils.equalsIgnoreCase(paramValue, responseValue);
                }).collect(Collectors.toList());
            }
        }

        log.info("cloudResourceAssignedStatistics list={}", list);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/resource_tracking_statement_excel")
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    public void cloudResourceAssignedStatisticsExcel(
            @RequestParam(name = "time", required = false) String time,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "statistics", required = false) String statistics,
            @RequestParam(name = "businessName", required = false) String businessName,
            @RequestParam(name = "resourceName", required = false) String resourceName,HttpServletResponse response) {


        Map<String, String> parm = new HashMap<>();

        Optional.ofNullable(time).filter(StringUtils::isNotBlank).ifPresent(e -> parm.put("time", e)); //??????
        Optional.ofNullable(statistics).filter(StringUtils::isNotBlank).ifPresent(e -> parm.put("statistics", e)); //?????? ???/???

        List<Map<String, String>> list = applyReportService.resourceTrackingStatement1(parm);

        Map<String, String> parm2 = new HashMap<>();
        Optional.ofNullable(businessName).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("businessName", e)); //????????????
        Optional.ofNullable(resourceName).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("resourceName", e)); //????????????
        Optional.ofNullable(type).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("type", e)); //???????????? ??????/??????

        if(MapUtils.isNotEmpty(parm2)) {
            for (Map.Entry<String, String> entry : parm2.entrySet()) {
                String paramKey = entry.getKey();
                String paramValue = entry.getValue();

                list = list.stream().filter(e -> {
                    String responseValue = e.get(paramKey);
                    return StringUtils.equalsIgnoreCase(paramValue, responseValue);
                }).collect(Collectors.toList());
            }
        }

        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        Workbook workbook =null;
        if("year".equals(statistics)){
            List<TrackingStatementVO> trackingStatementVOS = JSONObject.parseArray(jsonArray.toJSONString(), TrackingStatementVO.class);
            workbook = ExcelExportUtil.exportExcel(new ExportParams(), TrackingStatementVO.class, trackingStatementVOS);
            log.info("?????? - {}",trackingStatementVOS);
        }else if("month".equals(statistics)){
            List<TrackingStatementDayVO> trackingStatementDayVO = JSONObject.parseArray(jsonArray.toJSONString(), TrackingStatementDayVO.class);
            workbook = ExcelExportUtil.exportExcel(new ExportParams(), TrackingStatementDayVO.class, trackingStatementDayVO);
            log.info("?????? - {}",trackingStatementDayVO);
        }
        try {

            //response???HttpServletResponse??????
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");

            //test.xls??????????????????????????????????????????????????????????????????????????????
            String names = java.net.URLEncoder.encode("tracking_statement", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + names + ".xls");

            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }







    @GetMapping("/cloud_resource_assigned_statistics2")
    @ApiOperation(value = "???????????????????????????2", notes = "???????????????????????????2")
    public ResponseEntity<List<Map<String,String>>> cloudResourceAssignedStatistics2(
            @RequestParam(name = "secondDept", required = false) String secondDept,
            @RequestParam(name = "azone", required = false) String azone,
            @RequestParam(name = "thirdlyDept", required = false) String thirdlyDept) {

        List<Map<String, String>> list = applyReportService.cloudResourceAssignedStatistics2();

        Map<String, String> parm2 = new HashMap<>();
        Optional.ofNullable(azone).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("azone", e)); //??????
        Optional.ofNullable(secondDept).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("secondDept", e)); //??????
        Optional.ofNullable(thirdlyDept).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("thirdlyDept", e)); //??????

        if(MapUtils.isNotEmpty(parm2)) {
            for (Map.Entry<String, String> entry : parm2.entrySet()) {
                String paramKey = entry.getKey();
                String paramValue = entry.getValue();

                list = list.stream().filter(e -> {
                    String responseValue = e.get(paramKey);
                    return StringUtils.equalsIgnoreCase(paramValue, responseValue);
                }).collect(Collectors.toList());
            }
        }


        return ResponseEntity.status(HttpStatus.OK).body(list);
    }




    @GetMapping("/cloud_resource_assigned_statistics2_excel")
    @ApiOperation(value = "???????????????????????????2", notes = "???????????????????????????2")
    public void cloudResourceAssignedStatistics2Excel(
            @RequestParam(name = "secondDept", required = false) String secondDept,
            @RequestParam(name = "azone", required = false) String azone,
            @RequestParam(name = "thirdlyDept", required = false) String thirdlyDept,
            HttpServletResponse response) {

        List<Map<String, String>> list = applyReportService.cloudResourceAssignedStatistics2();

        Map<String, String> parm2 = new HashMap<>();
        Optional.ofNullable(azone).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("azone", e)); //??????
        Optional.ofNullable(secondDept).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("secondDept", e)); //??????
        Optional.ofNullable(thirdlyDept).filter(StringUtils::isNotBlank).ifPresent(e -> parm2.put("thirdlyDept", e)); //??????

        if(MapUtils.isNotEmpty(parm2)) {
            for (Map.Entry<String, String> entry : parm2.entrySet()) {
                String paramKey = entry.getKey();
                String paramValue = entry.getValue();

                list = list.stream().filter(e -> {
                    String responseValue = e.get(paramKey);
                    return StringUtils.equalsIgnoreCase(paramValue, responseValue);
                }).collect(Collectors.toList());
            }
        }

        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(list));
        List<DeptCounExcelVO> deptCounExcelVOS = JSONObject.parseArray(jsonArray.toJSONString(), DeptCounExcelVO.class);
        log.info("?????? - {}",deptCounExcelVOS);

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DeptCounExcelVO.class, deptCounExcelVOS);
        try {

            //response???HttpServletResponse??????
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");

            //test.xls??????????????????????????????????????????????????????????????????????????????
            String names = java.net.URLEncoder.encode("cloud_resource_assigned", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + names + ".xls");

            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    @GetMapping("/cloud_resource_statistics_new")
    @ApiOperation(value = "???????????????????????????new", notes = "???????????????????????????new")
    public ResponseEntity<StatisticsVOListResponse> cloudResourceStatisticsNew(
            @RequestParam(name = "keyword", required = false) String keyword) {
        StatisticsVOListResponse response = new StatisticsVOListResponse();
        Map<String, String> parm = new HashMap<>();
        Optional.ofNullable(keyword).filter(StringUtils::isNotBlank).ifPresent(e -> parm.put("keyword", e)); //????????????
        parm.put("userId", Sign.getUserId().toString());
//        parm.put("userId", "2777994821132290");
        List<StatisticsVO> statisticsVOS = applyReportService.cloudResourceStatisticsNew(parm);

        response.setData(statisticsVOS);
        response.setCode(ResultEnum.DEFAULT_SUCCESS.getCode());
        response.setMessage("??????");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @GetMapping("/cloud_resource_statistics_new_excel")
    @ApiOperation(value = "???????????????????????????new", notes = "???????????????????????????new")
    public void cloudResourceStatisticsNewExcel(
            @RequestParam(name = "keyword", required = false) String keyword,
            HttpServletResponse response) {
        Map<String, String> parm = new HashMap<>();
        Optional.ofNullable(keyword).filter(StringUtils::isNotBlank).ifPresent(e -> parm.put("keyword", e)); //????????????
        parm.put("userId", Sign.getUserId().toString());
//        parm.put("userId", "2777994821132290");
        List<StatisticsVO> statisticsVOS = applyReportService.cloudResourceStatisticsNew(parm);


        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), StatisticsVO.class, statisticsVOS);
        try {

            //response???HttpServletResponse??????
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");

            //test.xls??????????????????????????????????????????????????????????????????????????????
            String names = java.net.URLEncoder.encode("cloud_resource_assigned", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + names + ".xls");

            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @GetMapping("/calculation_resource_statistics_new")
    @ApiOperation(value = "?????????????????????new", notes = "?????????????????????new")
    public ResponseEntity<List<EmployVO>> calculationResourceStatisticsNew() {
        List<EmployVO> list = applyReportService.calculationResourceStatisticsNew();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }



    @GetMapping("/calculation_resource_statistics_new_excel")
    @ApiOperation(value = "???????????????????????????new", notes = "???????????????????????????new")
    public void calculationResourceStatisticsNewExcel(HttpServletResponse response) {
        Map<String, Object> parm = new HashMap<>();
        List<EmployVO> list = applyReportService.calculationResourceStatisticsNew();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), EmployVO.class, list);
        try {

            //response???HttpServletResponse??????
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");

            //test.xls??????????????????????????????????????????????????????????????????????????????
            String names = java.net.URLEncoder.encode("calculation_resource", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + names + ".xls");

            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
