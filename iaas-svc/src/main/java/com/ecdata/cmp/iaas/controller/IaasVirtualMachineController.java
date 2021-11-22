package com.ecdata.cmp.iaas.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.response.IaasVirtualMachineInfoResponse;
import com.ecdata.cmp.iaas.entity.dto.response.IaasVirtualMachinePageResponse;
import com.ecdata.cmp.iaas.entity.dto.response.VMGroupVOListResponse;
import com.ecdata.cmp.iaas.entity.dto.vm.VMGroupVO;
import com.ecdata.cmp.iaas.service.IaasVirtualMachineService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/25 10:59
 * @modified By：
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/v1/iaasVirtualMachine")
@Api(tags = "虚拟机资源查询列表API")
public class IaasVirtualMachineController {

    @Autowired
    private IaasVirtualMachineService machineService;


    @GetMapping("/page")
    @ApiOperation(value = "查询虚拟机利用率", notes = "查询虚拟机利用率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "businessGroupId", value = "业务组id", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "state", value = "状态", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasVirtualMachinePageResponse> page(@RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                               @RequestParam(defaultValue = "20", required = false) Integer pageSize,
                                                               @RequestParam String userId,
                                                               @RequestParam(required = false) String businessGroupId,
                                                               @RequestParam(required = false) String state,
                                                               @RequestParam(required = false) String keyword) {
        Page<IaasVirtualMachineVO> page = new Page<>(pageNo, pageSize);
        //调用分页查询方法
        Map<String, Object> params = new HashMap<>();
        if (userId != null) {
            params.put("userId", userId);
        }
        if (businessGroupId != null) {
            params.put("businessGroupId", businessGroupId);
        }
        if (state != null) {
            params.put("state", state);
        }
        if (keyword != null) {
            params.put("keyword", keyword);
        }

        IPage<IaasVirtualMachineVO> result = machineService.selectIaasVirtualMachineAll(page, params);
        //响应编码设置
        return ResponseEntity.status(HttpStatus.OK).body(new IaasVirtualMachinePageResponse(new PageVO<>(result)));
    }


    @GetMapping("/qrt_iaas_virtual_machine_info/{id}")
    @ApiOperation(value = "根据id查询单个虚拟机利用率", notes = "根据id查询单个虚拟机利用率")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    public ResponseEntity<IaasVirtualMachineInfoResponse> qrtIaasVirtualMachineInfo(@PathVariable(name = "id") String id) {

        IaasVirtualMachineVO machineVO = machineService.qrtIaasVirtualMachineInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body(new IaasVirtualMachineInfoResponse(machineVO));
    }


    @GetMapping("/qr_machine_optimize")
    @ApiOperation(value = "虚拟机优化查询", notes = "虚拟机优化查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页", paramType = "query", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", paramType = "query", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "date", value = "天数", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "maxCpu", value = "cpu最大值", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "avgCpu", value = "cpu平均值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "maxMemory", value = "内存最大值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "avgMemory", value = "内存平均值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "maxCpug", value = "大于cpu最大值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "avgCpug", value = "大于cpu平均值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "maxMemoryg", value = "大于内存最大值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "avgMemoryg", value = "大于内存平均值", paramType = "query", dataType = "string")
    })
    public ResponseEntity<IaasVirtualMachinePageResponse> qrMachineOptimize(
            @RequestParam(defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(defaultValue = "20", required = false) Integer pageSize,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String maxCpu,
            @RequestParam(required = false) String avgCpu,
            @RequestParam(required = false) String maxMemory,
            @RequestParam(required = false) String avgMemory,
            @RequestParam(required = false) String maxCpug,
            @RequestParam(required = false) String avgCpug,
            @RequestParam(required = false) String maxMemoryg,
            @RequestParam(required = false) String avgMemoryg) {
        Page<IaasVirtualMachineVO> page = new Page<>(pageNo, pageSize);
        Map<String, Object> params = new HashMap<>();
        if (date != null) {
            params.put("date", 500);
        }
        if (maxCpu != null) {
            params.put("maxCpu", maxCpu);
        }
        if (avgCpu != null) {
            params.put("avgCpu", avgCpu);
        }
        if (maxMemory != null) {
            params.put("maxMemory", maxMemory);
        }
        if (avgMemory != null) {
            params.put("avgMemory", avgMemory);
        }

        if (maxCpug != null) {
            params.put("maxCpug", maxCpug);
        }
        if (avgCpug != null) {
            params.put("avgCpug", avgCpug);
        }
        if (maxMemoryg != null) {
            params.put("maxMemoryg", maxMemoryg);
        }
        if (avgMemoryg != null) {
            params.put("avgMemoryg", avgMemoryg);
        }

        //查询出id
        List<String> list = machineService.qrMachineOptimize(params);
        //根据id去查询虚拟机信息
        Map<String, Object> map = new HashMap<>();
        map.put("ids", list);
        map.put("type", date);
        IPage<IaasVirtualMachineVO> result = machineService.selectByIds(page, map);

        return ResponseEntity.status(HttpStatus.OK).body(new IaasVirtualMachinePageResponse(new PageVO<>(result)));
    }


    @GetMapping("/query_machine_info")
    @ApiOperation(value = "根据申请流程id查询虚拟机详情", notes = "根据申请流程id查询虚拟机详情")
    public ResponseEntity<IaasVirtualMachineInfoResponse> queryMachinesByProcessApplyVmId(@RequestParam(value = "processApplyVmId") Long processApplyVmId) {

        IaasVirtualMachineVO machineVO = machineService.queryMachinesByProcessApplyVmId(processApplyVmId);
        return ResponseEntity.status(HttpStatus.OK).body(new IaasVirtualMachineInfoResponse(machineVO));
    }


    @GetMapping("/get_vmstatistical_list")
    @ApiOperation(value = "云上系统统计表", notes = "云上系统统计表")
    public ResponseEntity<VMGroupVOListResponse> queryVmStatisticalList() {
        List<VMGroupVO> vmGroupVOS = machineService.VmStatisticalList();
        return ResponseEntity.status(HttpStatus.OK).body(new VMGroupVOListResponse(vmGroupVOS));
    }


    @GetMapping("/get_vmstatistical_list_excel")
    @ApiOperation(value = "云上系统统计表", notes = "云上系统统计表")
    public void queryVmStatisticalListExcel(HttpServletResponse response) {

//        String downloadFileName = new String((fileName).getBytes(), StandardCharsets.UTF_8);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<VMGroupVO> vmGroupVOS = machineService.VmStatisticalList();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), VMGroupVO.class, vmGroupVOS);
        try {

            //response为HttpServletResponse对象
//            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");

            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            String name = java.net.URLEncoder.encode("1111111", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xls");

            workbook.write(response.getOutputStream());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
