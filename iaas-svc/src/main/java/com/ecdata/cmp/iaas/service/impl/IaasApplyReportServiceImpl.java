package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.iaas.entity.dto.distribution.CpuAndCountVO;
import com.ecdata.cmp.iaas.entity.dto.distribution.DeptCountVO;
import com.ecdata.cmp.iaas.entity.dto.employ.EmployBmVO;
import com.ecdata.cmp.iaas.entity.dto.employ.EmployVO;
import com.ecdata.cmp.iaas.entity.dto.employ.EmployVmVO;
import com.ecdata.cmp.iaas.entity.dto.report.*;
import com.ecdata.cmp.iaas.entity.dto.resource.ResourceResultSafetyVO;
import com.ecdata.cmp.iaas.entity.dto.resource.ResourceResultVO;
import com.ecdata.cmp.iaas.entity.dto.statistics.DistributionVMVO;
import com.ecdata.cmp.iaas.entity.dto.statistics.InTransitBMVO;
import com.ecdata.cmp.iaas.entity.dto.statistics.InTransitCMDVO;
import com.ecdata.cmp.iaas.entity.dto.statistics.StatisticsVO;
import com.ecdata.cmp.iaas.mapper.IaasAreaSumMapper;
import com.ecdata.cmp.iaas.mapper.report.IaasApplyReportMapper;
import com.ecdata.cmp.iaas.service.IaasApplyReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuxiaojian
 * @date 2020/5/11 14:00
 */
@Service
@Slf4j
public class IaasApplyReportServiceImpl implements IaasApplyReportService {
    @Autowired
    private IaasApplyReportMapper applyReportMapper;
    @Autowired
    private IaasAreaSumMapper iaasAreaSumMapper;

    @Override
    public List<CloudResourceStatisticsVO> cloudResourceStatistics(String type, String businessName) {
        List<CloudResourceStatisticsVO> resourceStatisticsVOS = new ArrayList<>();

        List<IaasApplyReport> iaasApplyReports = applyReportMapper.cloudResourceStatistics(businessName);

        if (CollectionUtils.isEmpty(iaasApplyReports)) {
            return new ArrayList<>();
        }

        List<IaasApplyReport> tree = getTree(iaasApplyReports);

        if (CollectionUtils.isEmpty(tree)) {
            return new ArrayList<>();
        }

        //总部管理员可以按照二级集团对资源进行统计,二级集团管理员则按照本集团下各三级机构的维度对各机构下的资源进行统计
        handleTotalTree(type, tree, resourceStatisticsVOS);

        //合计
        CloudResourceStatisticsVO vo = new CloudResourceStatisticsVO();
        vo.setName("合计");
        vo.setVmNum(resourceStatisticsVOS.stream().mapToInt(CloudResourceStatisticsVO::getVmNum).sum());
        vo.setCpu(resourceStatisticsVOS.stream().mapToInt(CloudResourceStatisticsVO::getCpu).sum());
        vo.setCpuAssigned(resourceStatisticsVOS.stream().mapToInt(CloudResourceStatisticsVO::getCpuAssigned).sum());
        vo.setMemory(resourceStatisticsVOS.stream().mapToInt(CloudResourceStatisticsVO::getMemory).sum());
        vo.setMemoryAssigned(resourceStatisticsVOS.stream().mapToInt(CloudResourceStatisticsVO::getMemoryAssigned).sum());

        int storage = 0;
        int storageAssigned = 0;
        for (CloudResourceStatisticsVO statisticsVO : resourceStatisticsVOS) {
            List<BareMetalVO> bareMetalVOS = statisticsVO.getBareMetalVOS();
            if (CollectionUtils.isNotEmpty(bareMetalVOS)) {
                if (bareMetalVOS.size() > 2) {
                    storage += bareMetalVOS.get(0).getBareMetal();
                    storage += bareMetalVOS.get(1).getBareMetal();
                    storageAssigned += bareMetalVOS.get(0).getBareMetalAssigned();
                    storageAssigned += bareMetalVOS.get(1).getBareMetalAssigned();
                } else {
                    storage += bareMetalVOS.get(0).getBareMetal();
                    storageAssigned += bareMetalVOS.get(0).getBareMetalAssigned();
                }
            }
        }

        vo.setStorage(storage);
        vo.setStorageAssigned(storageAssigned);
        return resourceStatisticsVOS;
    }

    @Override
    public List<CloudResourceAssignedStatisticsVO> cloudResourceAssignedStatistics(Map<String, Object> parm) {
        List<CloudResourceAssignedStatisticsVO> resourceStatisticsVOS = new ArrayList<>();

        List<IaasApplyReport> iaasApplyReports = applyReportMapper.cloudResourceAssignedStatistics(parm);

        if (CollectionUtils.isEmpty(iaasApplyReports)) {
            return new ArrayList<>();
        }

        List<IaasApplyReport> tree = getTree(iaasApplyReports);

        if (CollectionUtils.isEmpty(tree)) {
            return new ArrayList<>();
        }

        //总部管理员可以按照二级集团对资源进行统计,二级集团管理员则按照本集团下各三级机构的维度对各机构下的资源进行统计
        handleAssignedTotalTree(tree, resourceStatisticsVOS);

        return resourceStatisticsVOS;
    }

    @Override
    public List<CalculationResourceStatisticsVO> calculationResourceStatistics(Map<String, Object> parm) {
        List<ConfigInfo> configInfoList = applyReportMapper.calculationResourceStatistics();

        if (CollectionUtils.isEmpty(configInfoList)) {
            return new ArrayList<>();
        }
        List<CalculationResourceStatisticsVO> resultList = new ArrayList<>();

        //虚拟机
        Map<String, List<ConfigInfo>> cpuList = configInfoList.stream().filter(e -> StringUtils.isNotBlank(e.getAreaName())).filter(item -> item.getCpu() > 0 && "1".equals(item.getApplyType())).collect(Collectors.groupingBy(ConfigInfo::getAreaName));
        resultList.add(assembelr("Vcpu", cpuList));

        Map<String, List<ConfigInfo>> memoryList = configInfoList.stream().filter(e -> StringUtils.isNotBlank(e.getAreaName())).filter(item -> item.getMemory() > 0 && "1".equals(item.getApplyType())).collect(Collectors.groupingBy(ConfigInfo::getAreaName));
        resultList.add(assembelr("内存", memoryList));

        Map<String, List<ConfigInfo>> diskList = configInfoList.stream().filter(e -> StringUtils.isNotBlank(e.getAreaName())).filter(item -> CollectionUtils.isNotEmpty(item.getStorageList()) && "1".equals(item.getApplyType())).collect(Collectors.groupingBy(ConfigInfo::getAreaName));
        resultList.add(assembelr("存储", diskList));

        //裸金属
        Map<String, List<ConfigInfo>> luojins = configInfoList.stream().filter(e -> StringUtils.isNotBlank(e.getModel())).filter(item -> "2".equals(item.getApplyType())).collect(Collectors.groupingBy(ConfigInfo::getModel));

        for (Map.Entry<String, List<ConfigInfo>> map : luojins.entrySet()) {
            CalculationResourceStatisticsVO cpuVo = new CalculationResourceStatisticsVO();
            cpuVo.setId(SnowFlakeIdGenerator.getInstance().nextId());
            cpuVo.setResourceName(map.getKey());

            Map<String, List<ConfigInfo>> collect = map.getValue().stream().filter(e -> StringUtils.isNotBlank(e.getAreaName())).collect(Collectors.groupingBy(ConfigInfo::getAreaName));
            List<AreaVO> areaVOList = new ArrayList<>();
            for (Map.Entry<String, List<ConfigInfo>> map2 : collect.entrySet()) {
                int sum = iaasAreaSumMapper.queryServerNameAndAreaName(map.getKey(), map2.getKey());
                AreaVO areaVO = new AreaVO();
                areaVO.setId(SnowFlakeIdGenerator.getInstance().nextId());
                areaVO.setAreaName(map2.getKey());
                int assigned = (int) map2.getValue().stream().filter(item -> "2".equals(item.getState())).count();
                areaVO.setAssigned(assigned);
                int assigning = (int) map2.getValue().stream().filter(item -> !"2".equals(item.getState())).count();
                areaVO.setAssigning(assigning);
                if ((assigning + assigned) <= sum){
                    areaVO.setRemainingNumber(sum - (assigning + assigned));
                }
                areaVO.setSum(sum);
                areaVOList.add(areaVO);
            }
            cpuVo.setChildren(areaVOList);
            resultList.add(cpuVo);
        }
        return resultList;
    }

    @Override
    public List<CalculationResourceStatisticsVO> securityResourceUsageReport(Map<String, Object> parm) {
        List<ConfigInfo> configInfoList = applyReportMapper.securityResourceUsageReport();

        if (CollectionUtils.isEmpty(configInfoList)) {
            return new ArrayList<>();
        }
        List<CalculationResourceStatisticsVO> resultList = new ArrayList<>();

        //虚拟机 裸金属
        resultList.add(assembelr("数据库审计（DAS）", configInfoList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .filter(item -> ("1".equals(item.getApplyType()) || "2".equals(item.getApplyType())) && "1".equals(item.getIsDas()))
                .collect(Collectors.groupingBy(ConfigInfo::getAreaName))));

        resultList.add(assembelr("SSL VPN", configInfoList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .filter(item -> ("1".equals(item.getApplyType()) || "2".equals(item.getApplyType())) && "1".equals(item.getIsSslVpn()))
                .collect(Collectors.groupingBy(ConfigInfo::getAreaName))));

        resultList.add(assembelr("EDR", configInfoList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .filter(item -> ("1".equals(item.getApplyType()) || "2".equals(item.getApplyType())) && "1".equals(item.getIsEdr()))
                .collect(Collectors.groupingBy(ConfigInfo::getAreaName))));

        resultList.add(assembelr("日志审计", configInfoList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .filter(item -> ("1".equals(item.getApplyType()) || "2".equals(item.getApplyType())) && "1".equals(item.getIsLogAudit()))
                .collect(Collectors.groupingBy(ConfigInfo::getAreaName))));

        resultList.add(assembelr("漏洞扫描", configInfoList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .filter(item -> ("1".equals(item.getApplyType()) || "2".equals(item.getApplyType())) && "1".equals(item.getIsLoopholeScan()))
                .collect(Collectors.groupingBy(ConfigInfo::getAreaName))));

        resultList.add(assembelr("深信服运维安全管理系统软件", configInfoList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .filter(item -> ("1".equals(item.getApplyType()) || "2".equals(item.getApplyType())) && "1".equals(item.getIsSinfors()))
                .collect(Collectors.groupingBy(ConfigInfo::getAreaName))));

        resultList.add(assembelr("深信服配置核查系统软件", configInfoList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .filter(item -> ("1".equals(item.getApplyType()) || "2".equals(item.getApplyType())) && "1".equals(item.getIsSinforsCheck()))
                .collect(Collectors.groupingBy(ConfigInfo::getAreaName))));

        resultList.add(assembelr("防篡改", configInfoList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .filter(item -> ("1".equals(item.getApplyType()) || "2".equals(item.getApplyType())) && "1".equals(item.getIsPreventChange()))
                .collect(Collectors.groupingBy(ConfigInfo::getAreaName))));

        //安全资源
        List<ConfigInfo> collect = configInfoList.stream().filter(item -> "3".equals(item.getApplyType())).collect(Collectors.toList());
        resultList.add(assembelrSecurity("下一代防火墙", collect));
        resultList.add(assembelrSecurity("Web应用防火墙", collect));
        resultList.add(assembelrSecurity("入侵防御系统", collect));
        resultList.add(assembelrSecurity("应用交付（AD）", collect));
        resultList.add(assembelrSecurity("网行为管理", collect));
        return resultList;
    }

    @Override
    public List<Map<String,Object>> queryTowDep() {
        return applyReportMapper.queryTowDep();
    }

    @Override
    public List<Map<String,Object>> queryArea() {
        return applyReportMapper.queryArea();
    }




    @Override
    public List<Map<String, String>>  resourceTrackingStatement1(Map<String, String> map) {
        List <Map<String, String>> list = new ArrayList<>();

        List<String> dayByMonth = new ArrayList<>();
        String time = map.get("time").toString();
        if("month".equals(map.get("statistics"))){
            dayByMonth = IaasApplyReportServiceImpl.getDayByMonth(Integer.valueOf(time.substring(0,4)), Integer.valueOf(time.substring(5,7)));
        }else {
            dayByMonth = getLastDayOfMonth(Integer.valueOf(time.substring(0,4)));
        }

        Map<String,String> result1 = new HashMap<>();
        result1.put("businessName","税务");
        result1.put("resourceName","cpu");
        result1.put("type","1");
        result1.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result12 = new HashMap<>();
        result12.put("businessName","税务");
        result12.put("resourceName","内存");
        result12.put("type","1");
        result12.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result13 = new HashMap<>();
        result13.put("businessName","税务");
        result13.put("resourceName","存储数");
        result13.put("type","1");
        result13.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result14 = new HashMap<>();
        result14.put("businessName","税务");
        result14.put("resourceName","裸金属1");
        result14.put("type","1");
        result14.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result15 = new HashMap<>();
        result15.put("businessName","税务");
        result15.put("resourceName","裸金属2");
        result15.put("type","1");
        result15.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result16 = new HashMap<>();
        result16.put("businessName","税务");
        result16.put("resourceName","下一代防火墙");
        result16.put("type","2");
        result16.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result17 = new HashMap<>();
        result17.put("businessName","税务");
        result17.put("resourceName","web应用防火墙");
        result17.put("type","2");
        result17.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result18 = new HashMap<>();
        result18.put("businessName","税务");
        result18.put("resourceName","入侵防御系统");
        result18.put("type","2");
        result18.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result19 = new HashMap<>();
        result19.put("businessName","税务");
        result19.put("resourceName","应用交付(AD)");
        result19.put("type","2");
        result19.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result110 = new HashMap<>();
        result110.put("businessName","税务");
        result110.put("resourceName","上网行为管理");
        result110.put("type","2");
        result110.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result111 = new HashMap<>();
        result111.put("businessName","税务");
        result111.put("resourceName","数据库审计");
        result111.put("type","2");
        result111.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result112 = new HashMap<>();
        result112.put("businessName","税务");
        result112.put("resourceName","SSL VPN");
        result112.put("type","2");
        result112.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result113 = new HashMap<>();
        result113.put("businessName","税务");
        result113.put("resourceName","EDR");
        result113.put("type","2");
        result113.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result114 = new HashMap<>();
        result114.put("businessName","税务");
        result114.put("resourceName","日志审计");
        result114.put("type","2");
        result114.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result115 = new HashMap<>();
        result115.put("businessName","税务");
        result115.put("resourceName","漏洞扫描");
        result115.put("type","2");
        result115.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result116 = new HashMap<>();
        result116.put("businessName","税务");
        result116.put("resourceName","深信服运维安全管理系统软件");
        result116.put("type","2");
        result116.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result117 = new HashMap<>();
        result117.put("businessName","税务");
        result117.put("resourceName","深信服配置核查系统软件");
        result117.put("type","2");
        result117.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result118 = new HashMap<>();
        result118.put("businessName","税务");
        result118.put("resourceName","防篡改");
        result118.put("type","2");
        result118.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");




        Map<String,String> result2 = new HashMap<>();
        result2.put("businessName","集团");
        result2.put("resourceName","cpu");
        result2.put("type","1");
        result2.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result22 = new HashMap<>();
        result22.put("businessName","集团");
        result22.put("resourceName","内存");
        result22.put("type","1");
        result22.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result23 = new HashMap<>();
        result23.put("businessName","集团");
        result23.put("resourceName","存储数");
        result23.put("type","1");
        result23.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result24 = new HashMap<>();
        result24.put("businessName","集团");
        result24.put("resourceName","裸金属1");
        result24.put("type","1");
        result24.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result25 = new HashMap<>();
        result25.put("businessName","集团");
        result25.put("resourceName","裸金属2");
        result25.put("type","1");
        result25.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result26 = new HashMap<>();
        result26.put("businessName","集团");
        result26.put("resourceName","下一代防火墙");
        result26.put("type","2");
        result26.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result27 = new HashMap<>();
        result27.put("businessName","集团");
        result27.put("resourceName","web应用防火墙");
        result27.put("type","2");
        result27.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result28 = new HashMap<>();
        result28.put("businessName","集团");
        result28.put("resourceName","入侵防御系统");
        result28.put("type","2");
        result28.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result29 = new HashMap<>();
        result29.put("businessName","集团");
        result29.put("resourceName","应用交付(AD)");
        result29.put("type","2");
        result29.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result210 = new HashMap<>();
        result210.put("businessName","集团");
        result210.put("resourceName","上网行为管理");
        result210.put("type","2");
        result210.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result211 = new HashMap<>();
        result211.put("businessName","集团");
        result211.put("resourceName","数据库审计");
        result211.put("type","2");
        result211.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result212 = new HashMap<>();
        result212.put("businessName","集团");
        result212.put("resourceName","SSL VPN");
        result212.put("type","2");
        result212.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result213 = new HashMap<>();
        result213.put("businessName","集团");
        result213.put("resourceName","EDR");
        result213.put("type","2");
        result213.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result214 = new HashMap<>();
        result214.put("businessName","集团");
        result214.put("resourceName","日志审计");
        result214.put("type","2");
        result214.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result215 = new HashMap<>();
        result215.put("businessName","集团");
        result215.put("resourceName","漏洞扫描");
        result215.put("type","2");
        result215.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result216 = new HashMap<>();
        result216.put("businessName","集团");
        result216.put("resourceName","深信服运维安全管理系统软件");
        result216.put("type","2");
        result216.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result217 = new HashMap<>();
        result217.put("businessName","集团");
        result217.put("resourceName","深信服配置核查系统软件");
        result217.put("type","2");
        result217.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        Map<String,String> result218 = new HashMap<>();
        result218.put("businessName","集团");
        result218.put("resourceName","防篡改");
        result218.put("type","2");
        result218.put("id",SnowFlakeIdGenerator.getInstance().nextId()+"");

        for (String date :dayByMonth) {
            String tKey="";
            if("year".equals(map.get("statistics"))){
                tKey ="_" + date.substring(5, 7);
            }else {
                tKey ="_" + date.substring(8, 10);
            }
            String [] safety = {"nexts","web","ips","ad","ac","das","vpn","edr","log","scan","sinfors","sinfors_check","changes"};
            map.put("time",date);
            map.put("parentId", "39212873070952450");
            ResourceResultVO resultVOS = applyReportMapper.resourceTrackingStatement1(map);
            if(resultVOS == null){
                result1.put(tKey,"0");
                result12.put(tKey,"0");
            }else {
                result1.put(tKey, String.valueOf(resultVOS.getCpu()));
                result12.put(tKey, String.valueOf(resultVOS.getMemory()));
            }
            //存储
            ResourceResultVO resultVOS11 = applyReportMapper.resourceTrackingStatement2(map);
            if(resultVOS11 == null){
                result13.put(tKey,"0");
            }else {
                result13.put(tKey, String.valueOf(resultVOS11.getSystemDisk()));
            }
            //裸金属
            List<ResourceResultVO> resourceResultVOS = applyReportMapper.resourceTrackingStatement3(map);
            if(resourceResultVOS.size()==1){
                result14.put(tKey,"0");
                result15.put(tKey,"0");
            }
            if(resourceResultVOS.size()>0){
                for (ResourceResultVO vo:resourceResultVOS) {
                    if ("physical.2288".equals(vo.getDetailId())){
                        result14.put(tKey,String.valueOf(vo.getBmCon()));
                    }else if("physical.2488".equals(vo.getDetailId())){
                        result15.put(tKey,String.valueOf(vo.getBmCon()));
                    }

                }
            }else {
                result14.put(tKey,"0");
                result15.put(tKey,"0");
            }

            for (int i=0;i<safety.length;i++){
                ResourceResultSafetyVO resourceResultSafetyVO = applyReportMapper.resourceTrackingStatement4(safety[i], "39212873070952450",date);
                if(i==0){
                    if(resourceResultSafetyVO!=null){
                        result16.put(tKey,String.valueOf(resourceResultSafetyVO.getNext()));
                    }else {
                        result16.put(tKey,String.valueOf(0));
                    }
                };
                if(i==1){
                    if(resourceResultSafetyVO!=null) {
                        result17.put(tKey, String.valueOf(resourceResultSafetyVO.getWeb()));
                    }else {
                        result17.put(tKey,String.valueOf(0));
                    }
                };
                if(i==2){
                    if(resourceResultSafetyVO!=null) {
                        result18.put(tKey,String.valueOf(resourceResultSafetyVO.getIps()));
                    }else {
                        result18.put(tKey,String.valueOf(0));
                    }
                };
                if(i==3){
                    if(resourceResultSafetyVO!=null) {
                        result19.put(tKey, String.valueOf(resourceResultSafetyVO.getAd()));
                    }else {
                        result19.put(tKey,String.valueOf(0));
                    }
                };
                if(i==4){
                    if(resourceResultSafetyVO!=null) {
                        result110.put(tKey, String.valueOf(resourceResultSafetyVO.getAc()));
                    }else {
                        result110.put(tKey,String.valueOf(0));
                    }
                };
                if(i==5){
                    if(resourceResultSafetyVO!=null) {
                        result111.put(tKey, String.valueOf(resourceResultSafetyVO.getDas()));
                    }else {
                        result111.put(tKey,String.valueOf(0));
                    }
                };
                if(i==6){
                    if(resourceResultSafetyVO!=null) {
                        result112.put(tKey, String.valueOf(resourceResultSafetyVO.getVpn()));
                    }else {
                        result112.put(tKey,String.valueOf(0));
                    }
                };
                if(i==7){
                    if(resourceResultSafetyVO!=null) {
                        result113.put(tKey, String.valueOf(resourceResultSafetyVO.getEdr()));
                    }else {
                        result113.put(tKey,String.valueOf(0));
                    }
                };
                if(i==8){
                    if(resourceResultSafetyVO!=null) {
                        result114.put(tKey, String.valueOf(resourceResultSafetyVO.getLog()));
                    }else {
                        result114.put(tKey,String.valueOf(0));
                    }
                };
                if(i==9){
                    if(resourceResultSafetyVO!=null) {
                        result115.put(tKey, String.valueOf(resourceResultSafetyVO.getScan()));
                    }else {
                        result115.put(tKey,String.valueOf(0));
                    }
                };
                if(i==10){
                    if(resourceResultSafetyVO!=null) {
                        result116.put(tKey, String.valueOf(resourceResultSafetyVO.getSinfors()));
                    }else {
                        result116.put(tKey,String.valueOf(0));
                    }
                };
                if(i==11){
                    if(resourceResultSafetyVO!=null) {
                        result117.put(tKey, String.valueOf(resourceResultSafetyVO.getSinforsCheck()));
                    }else {
                        result117.put(tKey,String.valueOf(0));
                    }
                };
                if(i==12){
                    if(resourceResultSafetyVO!=null) {
                        result118.put(tKey, String.valueOf(resourceResultSafetyVO.getChanges()));
                    }else {
                        result118.put(tKey,String.valueOf(0));
                    }
                };
            }


//            result16.put(tKey,"0");result17.put(tKey,"0");result18.put(tKey,"0");
//            result19.put(tKey,"0");result110.put(tKey,"0");result111.put(tKey,"0");
//            result112.put(tKey,"0");result113.put(tKey,"0");result114.put(tKey,"0");
//            result115.put(tKey,"0");result116.put(tKey,"0");result117.put(tKey,"0");result118.put(tKey,"0");

            map.put("time",date);
            map.put("parentId", "39106200461475843");
            ResourceResultVO resultVOS2 = applyReportMapper.resourceTrackingStatement1(map);

            if(resultVOS2 == null){
                result2.put(tKey,"0");
                result22.put(tKey,"0");
            }else {
                result2.put(tKey, String.valueOf(resultVOS2.getCpu()));
                result22.put(tKey, String.valueOf(resultVOS2.getMemory()));
            }
            //存储
            ResourceResultVO resultVOS21 = applyReportMapper.resourceTrackingStatement2(map);
            if(resultVOS21 == null){
                result23.put(tKey,"0");
            }else {
                result23.put(tKey, String.valueOf(resultVOS21.getSystemDisk()));
            }
            //裸金属
            List<ResourceResultVO> resourceResultVOS2 = applyReportMapper.resourceTrackingStatement3(map);
            if(resourceResultVOS2.size()==1){
                result24.put(tKey,"0");
                result25.put(tKey,"0");
            }
            if(resourceResultVOS2.size()>0){
                for (ResourceResultVO vo:resourceResultVOS2) {
                    if ("physical.2288".equals(vo.getDetailId())){
                        result24.put(tKey,String.valueOf(vo.getBmCon()));
                    }else if("physical.2488".equals(vo.getDetailId())){
                        result25.put(tKey,String.valueOf(vo.getBmCon()));
                    }
                }
            }else{
            result24.put(tKey,"0");
            result25.put(tKey,"0");
            }
            for (int i=0;i<safety.length;i++){
                ResourceResultSafetyVO resourceResultSafetyVO = applyReportMapper.resourceTrackingStatement4(safety[i], "39106200461475843",date);
                if(i==0){
                    if(resourceResultSafetyVO!=null) {
                        result26.put(tKey, String.valueOf(resourceResultSafetyVO.getNext()));
                    }else {
                        result26.put(tKey, String.valueOf(0));
                    }
                };
                if(i==1){
                    if(resourceResultSafetyVO!=null) {
                        result27.put(tKey, String.valueOf(resourceResultSafetyVO.getWeb()));
                    }else {
                        result27.put(tKey, String.valueOf(0));
                    }
                };
                if(i==2){
                    if(resourceResultSafetyVO!=null) {
                        result28.put(tKey, String.valueOf(resourceResultSafetyVO.getIps()));
                    }else {
                        result28.put(tKey, String.valueOf(0));
                    }
                };
                if(i==3){
                    if(resourceResultSafetyVO!=null) {
                        result29.put(tKey, String.valueOf(resourceResultSafetyVO.getAd()));
                    }else {
                        result29.put(tKey, String.valueOf(0));
                    }
                };
                if(i==4){
                    if(resourceResultSafetyVO!=null) {
                        result210.put(tKey, String.valueOf(resourceResultSafetyVO.getAc()));
                    }else {
                        result210.put(tKey, String.valueOf(0));
                    }
                };
                if(i==5){
                    if(resourceResultSafetyVO!=null) {
                        result211.put(tKey, String.valueOf(resourceResultSafetyVO.getDas()));
                    }else {
                        result211.put(tKey, String.valueOf(0));
                    }
                };
                if(i==6){
                    if(resourceResultSafetyVO!=null) {
                        result212.put(tKey, String.valueOf(resourceResultSafetyVO.getVpn()));
                    }else {
                        result212.put(tKey, String.valueOf(0));
                    }
                };
                if(i==7){
                    if(resourceResultSafetyVO!=null) {
                        result213.put(tKey, String.valueOf(resourceResultSafetyVO.getEdr()));
                    }else {
                        result213.put(tKey, String.valueOf(0));
                    }
                };
                if(i==8){
                    if(resourceResultSafetyVO!=null) {
                        result214.put(tKey, String.valueOf(resourceResultSafetyVO.getLog()));
                    }else {
                        result214.put(tKey, String.valueOf(0));
                    }
                };
                if(i==9){
                    if(resourceResultSafetyVO!=null) {
                        result215.put(tKey, String.valueOf(resourceResultSafetyVO.getScan()));
                    }else {
                        result215.put(tKey, String.valueOf(0));
                    }
                };
                if(i==10){
                    if(resourceResultSafetyVO!=null) {
                        result216.put(tKey, String.valueOf(resourceResultSafetyVO.getSinfors()));
                    }else {
                        result216.put(tKey, String.valueOf(0));
                    }
                };
                if(i==11){
                    if(resourceResultSafetyVO!=null) {
                        result217.put(tKey, String.valueOf(resourceResultSafetyVO.getSinforsCheck()));
                    }else {
                        result217.put(tKey, String.valueOf(0));
                    }
                };
                if(i==12){
                    if(resourceResultSafetyVO!=null) {
                        result218.put(tKey, String.valueOf(resourceResultSafetyVO.getChanges()));
                    }else {
                        result218.put(tKey, String.valueOf(0));
                    }
                };
            }
        }


        list.add(result1);list.add(result12);list.add(result13);list.add(result14);list.add(result15);list.add(result16);
        list.add(result17);list.add(result18);list.add(result19);list.add(result110);list.add(result111);list.add(result112);
        list.add(result113);list.add(result114);list.add(result115);list.add(result116);list.add(result117);list.add(result118);
        list.add(result2);list.add(result22);list.add(result23);list.add(result24);list.add(result25);list.add(result26);
        list.add(result27);list.add(result28);list.add(result29);list.add(result210);list.add(result211);list.add(result212);
        list.add(result213);list.add(result214);list.add(result215);list.add(result216);list.add(result217);list.add(result218);
        return list;

    }

    @Override
    public List<Map<String,String>> cloudResourceAssignedStatistics2() {
        //目前就三个项目
        String [] projectIds = {"39105364583038978","39104991365480451","39652221528838144"};
        String [] azonIds ={"39102702814871557","39102702848425990","39102702873591810","39644519872335874","49175374311518213","49175374353461256"};
        //1.先获取部门对应的项目数量
        List<Map<String,String>> list  = new ArrayList<>();
        for (int i=0;i<projectIds.length;i++){
            for (int j=0;j<azonIds.length;j++) {
                List<DeptCountVO> deptCount = applyReportMapper.getDeptCount(projectIds[i].toString());
                    if(i==0){
                        Map<String,String> map = new HashMap<>();
                        map.put("secondDept","集团");
                        map.put("thirdlyDept","集团");
                        map.put("systemCon",deptCount.size()+"");
                        if(j==0){
                            CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                            CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                            map.put("azone","生产区虚拟机");
                            map.put("barecon1","0");
                            map.put("barecon2","0");
                            if(vmCpuAndCount!=null){
                                map.put("vcpu",vmCpuAndCount.getVcpu());
                                map.put("memory",vmCpuAndCount.getMemory());
                                map.put("vmcon",vmCpuAndCount.getVmcon());
                            }else {
                                map.put("vcpu","0");
                                map.put("memory","0");
                                map.put("vmcon","0");
                            }
                            if(vmDisk != null){
                                map.put("disk",vmDisk.getDisk());
                            }else {
                                map.put("disk","0");
                            }
                        }
                        if(j==1){
                            CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                            CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                            map.put("azone","互联网区");
                            map.put("barecon1","0");
                            map.put("barecon2","0");
                            if(vmCpuAndCount!=null){
                                map.put("vcpu",vmCpuAndCount.getVcpu());
                                map.put("memory",vmCpuAndCount.getMemory());
                                map.put("vmcon",vmCpuAndCount.getVmcon());
                            }else {
                                map.put("vcpu","0");
                                map.put("memory","0");
                                map.put("vmcon","0");
                            }
                            if(vmDisk != null){
                                map.put("disk",vmDisk.getDisk());
                            }else {
                                map.put("disk","0");
                            }
                        }
                        if(j==2){
                            CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                            CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                            map.put("azone","灾备区虚拟机");
                            map.put("barecon1","0");
                            map.put("barecon2","0");
                            if(vmCpuAndCount!=null){
                                map.put("vcpu",vmCpuAndCount.getVcpu());
                                map.put("memory",vmCpuAndCount.getMemory());
                                map.put("vmcon",vmCpuAndCount.getVmcon());
                            }else {
                                map.put("vcpu","0");
                                map.put("memory","0");
                                map.put("vmcon","0");
                            }
                            if(vmDisk != null){
                                map.put("disk",vmDisk.getDisk());
                            }else {
                                map.put("disk","0");
                            }
                        }
                        if(j==3){
                            List<CpuAndCountVO> bareMetalCount = applyReportMapper.getBareMetalCount(projectIds[i].toString(), azonIds[j].toString());
                            map.put("azone","生产区裸金属");
                            if(bareMetalCount!=null){
                                map.put("vcpu","0");
                                map.put("memory","0");
                                map.put("vmcon","0");
                                map.put("disk","0");
                                for (CpuAndCountVO vo:bareMetalCount){
                                    if("physical.2288".equals(vo.getDetail())){
                                        map.put("barecon1",vo.getIbmcon());
                                    }else if("physical.2488".equals(vo.getDetail())){
                                        map.put("barecon2",vo.getIbmcon());
                                    }
                                }
                            }else {
                                map.put("vcpu","0");
                                map.put("memory","0");
                                map.put("vmcon","0");
                                map.put("disk","0");
                                map.put("barecon1","0");
                                map.put("barecon2","0");
                            }
                        }
                        if(j==4){
                            List<CpuAndCountVO> bareMetalCount = applyReportMapper.getBareMetalCount(projectIds[i].toString(), azonIds[j].toString());
                            map.put("azone","灾备区裸金属");
                            if(bareMetalCount!=null){
                                map.put("vcpu","0");
                                map.put("memory","0");
                                map.put("vmcon","0");
                                map.put("disk","0");
                                for (CpuAndCountVO vo:bareMetalCount){
                                    if("physical.2288".equals(vo.getDetail())){
                                        map.put("barecon1",vo.getIbmcon());
                                    }else if("physical.2488".equals(vo.getDetail())){
                                        map.put("barecon2",vo.getIbmcon());
                                    }
                                }
                            }else {
                                map.put("vcpu","0");
                                map.put("memory","0");
                                map.put("vmcon","0");
                                map.put("disk","0");
                                map.put("barecon1","0");
                                map.put("barecon2","0");
                            }
                        }
                        if(j==5){
                            CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                            CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                            map.put("azone","测试区");
                            map.put("barecon1","0");
                            map.put("barecon2","0");
                            if(vmCpuAndCount!=null){
                                map.put("vcpu",vmCpuAndCount.getVcpu());
                                map.put("memory",vmCpuAndCount.getMemory());
                                map.put("vmcon",vmCpuAndCount.getVmcon());
                            }else {
                                map.put("vcpu","0");
                                map.put("memory","0");
                                map.put("vmcon","0");
                            }
                            if(vmDisk != null){
                                map.put("disk",vmDisk.getDisk());
                            }else {
                                map.put("disk","0");
                            }
                        }
                        list.add(map);
                    }
                if(i==1){
                    Map<String,String> map = new HashMap<>();
                    map.put("secondDept","税务");
                    map.put("thirdlyDept","税务1");
                    map.put("systemCon",deptCount.size()+"");
                    if(j==0){
                        CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                        CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","生产区虚拟机");
                        map.put("barecon1","0");
                        map.put("barecon2","0");
                        if(vmCpuAndCount!=null){
                            map.put("vcpu",vmCpuAndCount.getVcpu());
                            map.put("memory",vmCpuAndCount.getMemory());
                            map.put("vmcon",vmCpuAndCount.getVmcon());
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                        }
                        if(vmDisk != null){
                            map.put("disk",vmDisk.getDisk());
                        }else {
                            map.put("disk","0");
                        }
                    }
                    if(j==1){
                        CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                        CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","互联网区");
                        map.put("barecon1","0");
                        map.put("barecon2","0");
                        if(vmCpuAndCount!=null){
                            map.put("vcpu",vmCpuAndCount.getVcpu());
                            map.put("memory",vmCpuAndCount.getMemory());
                            map.put("vmcon",vmCpuAndCount.getVmcon());
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                        }
                        if(vmDisk != null){
                            map.put("disk",vmDisk.getDisk());
                        }else {
                            map.put("disk","0");
                        }
                    }
                    if(j==2){
                        CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                        CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","灾备区虚拟机");
                        map.put("barecon1","0");
                        map.put("barecon2","0");
                        if(vmCpuAndCount!=null){
                            map.put("vcpu",vmCpuAndCount.getVcpu());
                            map.put("memory",vmCpuAndCount.getMemory());
                            map.put("vmcon",vmCpuAndCount.getVmcon());
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                        }
                        if(vmDisk != null){
                            map.put("disk",vmDisk.getDisk());
                        }else {
                            map.put("disk","0");
                        }
                    }
                    if(j==3){
                        List<CpuAndCountVO> bareMetalCount = applyReportMapper.getBareMetalCount(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","生产区裸金属");
                        if(bareMetalCount!=null){
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                            map.put("disk","0");
                            for (CpuAndCountVO vo:bareMetalCount){
                                if("physical.2288".equals(vo.getDetail())){
                                    map.put("barecon1",vo.getIbmcon());
                                }else if("physical.2488".equals(vo.getDetail())){
                                    map.put("barecon2",vo.getIbmcon());
                                }
                            }
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                            map.put("disk","0");
                            map.put("barecon1","0");
                            map.put("barecon2","0");
                        }
                    }
                    if(j==4){

                        List<CpuAndCountVO> bareMetalCount = applyReportMapper.getBareMetalCount(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","灾备区裸金属");
                        if(bareMetalCount!=null){
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                            map.put("disk","0");
                            for (CpuAndCountVO vo:bareMetalCount){
                                if("physical.2288".equals(vo.getDetail())){
                                    map.put("barecon1",vo.getIbmcon());
                                }else if("physical.2488".equals(vo.getDetail())){
                                    map.put("barecon2",vo.getIbmcon());
                                }
                            }
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                            map.put("disk","0");
                            map.put("barecon1","0");
                            map.put("barecon2","0");
                        }
                    }
                    if(j==5){
                        CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                        CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","测试区");
                        map.put("barecon1","0");
                        map.put("barecon2","0");
                        if(vmCpuAndCount!=null){
                            map.put("vcpu",vmCpuAndCount.getVcpu());
                            map.put("memory",vmCpuAndCount.getMemory());
                            map.put("vmcon",vmCpuAndCount.getVmcon());
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                        }
                        if(vmDisk != null){
                            map.put("disk",vmDisk.getDisk());
                        }else {
                            map.put("disk","0");
                        }
                    }
                    list.add(map);
                }
                if(i==2){
                    Map<String,String> map = new HashMap<>();
                    map.put("secondDept","税务");
                    map.put("thirdlyDept","税务2");
                    map.put("systemCon",deptCount.size()+"");
                    if(j==0){
                        CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                        CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","生产区虚拟机");
                        map.put("barecon1","0");
                        map.put("barecon2","0");
                        if(vmCpuAndCount!=null){
                            map.put("vcpu",vmCpuAndCount.getVcpu());
                            map.put("memory",vmCpuAndCount.getMemory());
                            map.put("vmcon",vmCpuAndCount.getVmcon());
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                        }
                        if(vmDisk != null){
                            map.put("disk",vmDisk.getDisk());
                        }else {
                            map.put("disk","0");
                        }
                    }
                    if(j==1){
                        CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                        CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","互联网区");
                        map.put("barecon1","0");
                        map.put("barecon2","0");
                        if(vmCpuAndCount!=null){
                            map.put("vcpu",vmCpuAndCount.getVcpu());
                            map.put("memory",vmCpuAndCount.getMemory());
                            map.put("vmcon",vmCpuAndCount.getVmcon());
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                        }
                        if(vmDisk != null){
                            map.put("disk",vmDisk.getDisk());
                        }else {
                            map.put("disk","0");
                        }
                    }
                    if(j==2){
                        CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                        CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","灾备区虚拟机");
                        map.put("barecon1","0");
                        map.put("barecon2","0");
                        if(vmCpuAndCount!=null){
                            map.put("vcpu",vmCpuAndCount.getVcpu());
                            map.put("memory",vmCpuAndCount.getMemory());
                            map.put("vmcon",vmCpuAndCount.getVmcon());
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                        }
                        if(vmDisk != null){
                            map.put("disk",vmDisk.getDisk());
                        }else {
                            map.put("disk","0");
                        }
                    }
                    if(j==3){
                        List<CpuAndCountVO> bareMetalCount = applyReportMapper.getBareMetalCount(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","生产区裸金属");
                        if(bareMetalCount!=null){
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                            map.put("disk","0");
                            for (CpuAndCountVO vo:bareMetalCount){
                                if("physical.2288".equals(vo.getDetail())){
                                    map.put("barecon1",vo.getIbmcon());
                                }else if("physical.2488".equals(vo.getDetail())){
                                    map.put("barecon2",vo.getIbmcon());
                                }
                            }
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                            map.put("disk","0");
                            map.put("barecon1","0");
                            map.put("barecon2","0");
                        }
                    }
                    if(j==4){

                        List<CpuAndCountVO> bareMetalCount = applyReportMapper.getBareMetalCount(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","灾备区裸金属");
                        if(bareMetalCount!=null){
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                            map.put("disk","0");
                            for (CpuAndCountVO vo:bareMetalCount){
                                if("physical.2288".equals(vo.getDetail())){
                                    map.put("barecon1",vo.getIbmcon());
                                }else if("physical.2488".equals(vo.getDetail())){
                                    map.put("barecon2",vo.getIbmcon());
                                }
                            }
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                            map.put("disk","0");
                            map.put("barecon1","0");
                            map.put("barecon2","0");
                        }
                    }
                    if(j==5){
                        CpuAndCountVO vmCpuAndCount = applyReportMapper.getVmCpuAndCount(projectIds[i].toString(), azonIds[j].toString());
                        CpuAndCountVO vmDisk = applyReportMapper.getVmDisk(projectIds[i].toString(), azonIds[j].toString());
                        map.put("azone","测试区");
                        map.put("barecon1","0");
                        map.put("barecon2","0");
                        if(vmCpuAndCount!=null){
                            map.put("vcpu",vmCpuAndCount.getVcpu());
                            map.put("memory",vmCpuAndCount.getMemory());
                            map.put("vmcon",vmCpuAndCount.getVmcon());
                        }else {
                            map.put("vcpu","0");
                            map.put("memory","0");
                            map.put("vmcon","0");
                        }
                        if(vmDisk != null){
                            map.put("disk",vmDisk.getDisk());
                        }else {
                            map.put("disk","0");
                        }
                    }
                    list.add(map);
                }

            }

        }




        return list;
    }

    @Override
    public List<StatisticsVO> cloudResourceStatisticsNew(Map<String, String> parm) {
        //1.获取虚拟机cpu/内存/磁盘 在途数量
        List<InTransitCMDVO> inTransitCMD = applyReportMapper.getInTransitCMD(parm);
        //2.获取裸金属在途数量
        List<InTransitBMVO> inTransitBM = applyReportMapper.getInTransitBM(parm);
        //3.获取虚拟机已分配cpu/内存/磁盘数量
        List<DistributionVMVO> distributionVM = applyReportMapper.getDistributionVM(parm);
        //4.获取裸金属已分配数量
        List<InTransitBMVO> distributionBM = applyReportMapper.getDistributionBM(parm);

        List<StatisticsVO> list = new ArrayList<>();


        StatisticsVO statisticsVO2 = new StatisticsVO();
        statisticsVO2.setRemark("集团");
        if(inTransitCMD.size()>0){
            for (InTransitCMDVO cmdvo:inTransitCMD) {
                if("集团".equals(cmdvo.getRemark())){
                    statisticsVO2.setId(cmdvo.getId());
                    statisticsVO2.setInCpu(cmdvo.getCpu());
                    statisticsVO2.setInMemory(cmdvo.getMemory());
                    statisticsVO2.setInDisk(cmdvo.getDisk());
                }
            }
        }else {
            statisticsVO2.setInCpu(0l);
            statisticsVO2.setInMemory(0l);
            statisticsVO2.setInDisk(0l);
        }

        if(inTransitBM.size()>0){
            for (InTransitBMVO bmvo:inTransitBM){
                if ("集团".equals(bmvo.getRemark())){
                    if ("physical.2288".equals(bmvo.getModel())){
                        statisticsVO2.setInBM1(bmvo.getCon());
                    }else if("physical.2488".equals(bmvo.getModel())){
                        statisticsVO2.setInBM2(bmvo.getCon());
                    }
                }
            }
        }else {
            statisticsVO2.setInBM1(0l);
            statisticsVO2.setInBM2(0l);
        }

        if(distributionVM.size()>0){
            for (DistributionVMVO vmvo:distributionVM) {
                if ("集团".equals(vmvo.getRemark())){
                    statisticsVO2.setDisCpu(vmvo.getCpu());
                    statisticsVO2.setDisDisk(vmvo.getDisk());
                    statisticsVO2.setDisMemory(vmvo.getMemory());
                    statisticsVO2.setVmcon(vmvo.getVmcon());
                }
            }
        }else {
            statisticsVO2.setDisCpu(0l);
            statisticsVO2.setDisDisk(0l);
            statisticsVO2.setDisMemory(0l);
            statisticsVO2.setVmcon(0l);
        }

        if(distributionBM.size()>0){
            for (InTransitBMVO bmvo:distributionBM) {
                if ("集团".equals(bmvo.getRemark())){
                    if ("physical.2288".equals(bmvo.getModel())){
                        statisticsVO2.setDisBM1(bmvo.getCon());
                    }else if("physical.2488".equals(bmvo.getModel())){
                        statisticsVO2.setDisBM2(bmvo.getCon());
                    }
                }
            }
        }else {
            statisticsVO2.setDisBM1(0l);
            statisticsVO2.setDisBM2(0l);
        }

        list.add(statisticsVO2);

        StatisticsVO statisticsVO1 = new StatisticsVO();
        statisticsVO1.setRemark("税务");
        if(inTransitCMD.size()>0){
            for (InTransitCMDVO cmdvo:inTransitCMD) {
                if("税务".equals(cmdvo.getRemark())){
                    statisticsVO1.setId(cmdvo.getId());
                    statisticsVO1.setInCpu(cmdvo.getCpu());
                    statisticsVO1.setInMemory(cmdvo.getMemory());
                    statisticsVO1.setInDisk(cmdvo.getDisk());
                }
            }
        }else {
            statisticsVO1.setInCpu(0l);
            statisticsVO1.setInMemory(0l);
            statisticsVO1.setInDisk(0l);
        }

        if(inTransitBM.size()>0){
            for (InTransitBMVO bmvo:inTransitBM){
                if ("税务".equals(bmvo.getRemark())){
                    if ("physical.2288".equals(bmvo.getModel())){
                        statisticsVO1.setInBM1(bmvo.getCon());
                    }else if("physical.2488".equals(bmvo.getModel())){
                        statisticsVO1.setInBM2(bmvo.getCon());
                    }
                }
            }
        }else {
            statisticsVO1.setInBM1(0l);
            statisticsVO1.setInBM2(0l);
        }

        if(distributionVM.size()>0){
            for (DistributionVMVO vmvo:distributionVM) {
                if ("税务".equals(vmvo.getRemark())){
                    statisticsVO1.setDisCpu(vmvo.getCpu());
                    statisticsVO1.setDisDisk(vmvo.getDisk());
                    statisticsVO1.setDisMemory(vmvo.getMemory());
                    statisticsVO1.setVmcon(vmvo.getVmcon());
                }
            }
        }else {
            statisticsVO1.setDisCpu(0l);
            statisticsVO1.setDisDisk(0l);
            statisticsVO1.setDisMemory(0l);
            statisticsVO1.setVmcon(0l);
        }


        if(distributionBM.size()>0){
            for (InTransitBMVO bmvo:distributionBM) {
                if ("税务".equals(bmvo.getRemark())){
                    if ("physical.2288".equals(bmvo.getModel())){
                        statisticsVO1.setDisBM1(bmvo.getCon());
                    }else if("physical.2488".equals(bmvo.getModel())){
                        statisticsVO1.setDisBM2(bmvo.getCon());
                    }
                }
            }
        }else {
            statisticsVO1.setDisBM1(0l);
            statisticsVO1.setDisBM2(0l);
        }
        list.add(statisticsVO1);



        list = list.stream().map(e -> {
            e.setInBM1(Optional.ofNullable(e.getInBM1()).orElse(0L));
            e.setInBM2(Optional.ofNullable(e.getInBM2()).orElse(0L));
            e.setInCpu(Optional.ofNullable(e.getInCpu()).orElse(0L));
            e.setInDisk(Optional.ofNullable(e.getInDisk()).orElse(0L));
            e.setInMemory(Optional.ofNullable(e.getInMemory()).orElse(0L));
            e.setDisBM1(Optional.ofNullable(e.getDisBM1()).orElse(0L));
            e.setDisBM2(Optional.ofNullable(e.getDisBM2()).orElse(0L));
            e.setDisCpu(Optional.ofNullable(e.getDisCpu()).orElse(0L));
            e.setDisMemory(Optional.ofNullable(e.getDisMemory()).orElse(0L));
            e.setDisDisk(Optional.ofNullable(e.getDisDisk()).orElse(0L));
            e.setVmcon(Optional.ofNullable(e.getVmcon()).orElse(0L));

            return e;
        }).collect(Collectors.toList());

        StatisticsVO statisticsVO3 = new StatisticsVO();

        statisticsVO3.setRemark("合计");
        statisticsVO3.setId(SnowFlakeIdGenerator.getInstance().nextId());
        statisticsVO3.setVmcon(statisticsVO1.getVmcon()+statisticsVO2.getVmcon());
        statisticsVO3.setDisCpu(statisticsVO1.getDisCpu()+statisticsVO2.getDisCpu());
        statisticsVO3.setDisMemory(statisticsVO1.getDisMemory()+statisticsVO2.getDisMemory());
        statisticsVO3.setDisDisk(statisticsVO1.getDisDisk()+statisticsVO2.getDisDisk());
        statisticsVO3.setDisBM1(statisticsVO1.getDisBM1()+statisticsVO2.getDisBM1());
        statisticsVO3.setDisBM2(statisticsVO1.getDisBM2()+statisticsVO2.getDisBM2());
        statisticsVO3.setInCpu(statisticsVO1.getInCpu()+statisticsVO2.getInCpu());
        statisticsVO3.setInMemory(statisticsVO1.getInMemory()+statisticsVO2.getInMemory());
        statisticsVO3.setInDisk(statisticsVO1.getInDisk()+statisticsVO2.getInDisk());
        statisticsVO3.setInBM1(statisticsVO1.getInBM1()+statisticsVO2.getInBM1());
        statisticsVO3.setInBM2(statisticsVO1.getInBM2()+statisticsVO2.getInBM2());
        list.add(statisticsVO3);
        return list;
    }

    @Override
    public List<EmployVO> calculationResourceStatisticsNew() {

        List<EmployVO> list = new ArrayList<>();
        String [] remark1 = {"生产区虚拟机","灾备区虚拟机","互联网区","测试区"};
        String [] remark2 = {"生产区裸金属","灾备区裸金属"};
        String [] type1 ={"vCpu","memory","disk"};
        String [] type2 ={"physical.2288","physical.2488"};
        for (int i=0;i<remark1.length;i++){
            for (int j=0;j<type1.length;j++){
                EmployVO employVO = new EmployVO();
                EmployVmVO employVmDistribution = applyReportMapper.getEmployVmDistribution(type1[j], remark1[i]);
                EmployVmVO employInTransitVm = applyReportMapper.getEmployInTransitVm(type1[j], remark1[i]);
                EmployVmVO employVmBmSum = applyReportMapper.getEmployVmBmSum(type1[j], remark1[i]);
                employVO.setRemark(remark1[i]);
                if(j==0){
                    employVO.setResourceName("vCPU");
//                    Optional.ofNullable(employVmDistribution).ifPresent(e -> employVO.setDistribution(Optional.ofNullable(employVmDistribution.getCpu()).orElse(0L)));
//                    Optional.ofNullable(employInTransitVm).ifPresent(e -> employVO.setInTransit(Optional.ofNullable(employInTransitVm.getCpu()).orElse(0L)));

                    if (employVmDistribution != null) {
                        employVO.setDistribution(Optional.ofNullable(employVmDistribution.getCpu()).orElse(0L));
                    } else {
                        employVO.setDistribution(0L);
                    }

                    if (employInTransitVm != null) {
                        employVO.setInTransit(Optional.ofNullable(employInTransitVm.getCpu()).orElse(0L));
                    } else {
                        employVO.setInTransit(0L);
                    }
                }else if(j==1){
                    employVO.setResourceName("内存");
//                    Optional.ofNullable(employVmDistribution).ifPresent(e -> employVO.setDistribution(Optional.ofNullable(employVmDistribution.getMemory()).orElse(0L)));

                    if (employVmDistribution != null) {
                        employVO.setDistribution(Optional.ofNullable(employVmDistribution.getMemory()).orElse(0L));
                    } else {
                        employVO.setDistribution(0L);
                    }

                    if (employInTransitVm != null) {
                        employVO.setInTransit(Optional.ofNullable(employInTransitVm.getMemory()).orElse(0L));
                    } else {
                        employVO.setInTransit(0L);
                    }
                }else if(j==2){
//                    Optional.ofNullable(employVmDistribution).ifPresent(e -> employVO.setDistribution( Optional.ofNullable(employVmDistribution.getDisk()).orElse(0L)));
//                    Optional.ofNullable(employInTransitVm).ifPresent(e -> employVO.setInTransit(Optional.ofNullable(employInTransitVm.getDisk()).orElse(0L)));
                    employVO.setResourceName("存储");
                    if (employVmDistribution != null) {
                        employVO.setDistribution(Optional.ofNullable(employVmDistribution.getDisk()).orElse(0L));
                    } else {
                        employVO.setDistribution(0L);
                    }

                    if (employInTransitVm != null) {
                        employVO.setInTransit(Optional.ofNullable(employInTransitVm.getDisk()).orElse(0L));
                    } else {
                        employVO.setInTransit(0L);
                    }
                }
                if(employVmBmSum !=null){
                    employVO.setTotal(employVmBmSum.getAreaSum());
                }else {
                    employVO.setTotal(0l);
                }

                employVO.setRemain(employVO.getTotal()-employVO.getDistribution()-employVO.getInTransit());
                list.add(employVO);
            }
        }

        for (int i=0;i<remark2.length;i++){
            for (int j=0;j<type2.length;j++){
                EmployVO employVO = new EmployVO();
                EmployBmVO employBmDistribution = applyReportMapper.getEmployBmDistribution(type2[j], remark2[i]);
                EmployBmVO employInTransitBM = applyReportMapper.getEmployInTransitBM(type2[j], remark2[i]);
                EmployVmVO employVmBmSum = applyReportMapper.getEmployVmBmSum(type2[j], remark2[i]);
                if(j==0){
                    employVO.setResourceName("裸金属型号1");
                }else if(j==1){
                    employVO.setResourceName("裸金属型号2");
                }

                employVO.setRemark(remark2[i]);
                if(employBmDistribution!=null){
//                    Optional.ofNullable(employBmDistribution).ifPresent(e -> employVO.setDistribution(Optional.ofNullable(employBmDistribution.getBmcon()).orElse(0L)));
                    employVO.setDistribution(Optional.ofNullable(employBmDistribution.getBmcon()).orElse(0L));
                }else {
                    employVO.setDistribution(0l);
                }
                if(employInTransitBM != null){
//                    Optional.ofNullable(employInTransitBM).ifPresent(e -> employVO.setInTransit(Optional.ofNullable(employInTransitBM.getBmcon()).orElse(0L)));
                    employVO.setInTransit(Optional.ofNullable(employInTransitBM.getBmcon()).orElse(0L));
                }else {
                    employVO.setInTransit(0l);
                }
                if(employVmBmSum != null){
//                    Optional.ofNullable(employVmBmSum).ifPresent(e ->  employVO.setTotal(Optional.ofNullable(employVmBmSum.getAreaSum()).orElse(0L)));
                    employVO.setTotal(Optional.ofNullable(employVmBmSum.getAreaSum()).orElse(0L));
                }else {
                    employVO.setTotal(0l);
                }
                employVO.setRemain(employVO.getTotal()-employVO.getDistribution()-employVO.getInTransit());
                list.add(employVO);
            }
        }
        Collections.sort(list,new Comparator<EmployVO>() {
            public int compare(EmployVO o1, EmployVO o2) {
                return o1.getResourceName().compareTo(o2.getResourceName());
            }

        });

        return list;
    }

    private void buildArea(LinkedHashSet<String> areaSet, int count) {

    }

    private CalculationResourceStatisticsVO assembelrSecurity(String resourceName, List<ConfigInfo> mapPar) {

        CalculationResourceStatisticsVO cpuVo = new CalculationResourceStatisticsVO();
        cpuVo.setId(SnowFlakeIdGenerator.getInstance().nextId());
        cpuVo.setResourceName(resourceName);

        List<AreaVO> areaVOList = new ArrayList<>();
        AreaVO areaVO = new AreaVO();
        areaVO.setId(SnowFlakeIdGenerator.getInstance().nextId());
        int sum = 0;
        if ("下一代防火墙".equals(resourceName)) {
            sum = iaasAreaSumMapper.queryServerNameAndAreaName(resourceName, "");
            int assigned = (int) mapPar.stream().filter(item -> "2".equals(item.getState()) && "1".equals(item.getIsNextNfw())).count();
            areaVO.setAssigned(assigned);
            int assigning = (int) mapPar.stream().filter(item -> !"2".equals(item.getState()) && "1".equals(item.getIsNextNfw())).count();
            areaVO.setAssigning(assigning);
            if ((assigned + assigning) <= sum){
                areaVO.setRemainingNumber(sum - (assigned + assigning));
            }

        } else if ("Web应用防火墙".equals(resourceName)) {
            sum = iaasAreaSumMapper.queryServerNameAndAreaName(resourceName, "");
            int assigned = (int) mapPar.stream().filter(item -> "2".equals(item.getState()) && "1".equals(item.getIsWebNfw())).count();
            areaVO.setAssigned(assigned);
            int assigning = (int) mapPar.stream().filter(item -> !"2".equals(item.getState()) && "1".equals(item.getIsWebNfw())).count();
            areaVO.setAssigning(assigning);
            if ((assigned + assigning) <= sum){
                areaVO.setRemainingNumber(sum - (assigned + assigning));
            }

        } else if ("入侵防御系统".equals(resourceName)) {
            sum = iaasAreaSumMapper.queryServerNameAndAreaName(resourceName, "");
            int assigned = (int) mapPar.stream().filter(item -> "2".equals(item.getState()) && "1".equals(item.getIsIps())).count();
            areaVO.setAssigned(assigned);
            int assigning = (int) mapPar.stream().filter(item -> !"2".equals(item.getState()) && "1".equals(item.getIsIps())).count();
            areaVO.setAssigning(assigning);
            if ((assigned + assigning) <= sum){
                areaVO.setRemainingNumber(sum - (assigned + assigning));
            }

        } else if ("应用交付（AD）".equals(resourceName)) {
            sum = iaasAreaSumMapper.queryServerNameAndAreaName(resourceName, "");
            int assigned = (int) mapPar.stream().filter(item -> "2".equals(item.getState()) && "1".equals(item.getIsAd())).count();
            areaVO.setAssigned(assigned);
            int assigning = (int) mapPar.stream().filter(item -> !"2".equals(item.getState()) && "1".equals(item.getIsAd())).count();
            areaVO.setAssigning(assigning);
            if ((assigned + assigning) <= sum){
                areaVO.setRemainingNumber(sum - (assigned + assigning));
            }

        } else if ("网行为管理".equals(resourceName)) {
            sum = iaasAreaSumMapper.queryServerNameAndAreaName(resourceName, "");
            int assigned = (int) mapPar.stream().filter(item -> "2".equals(item.getState()) && "1".equals(item.getIsSinforAc())).count();
            areaVO.setAssigned(assigned);
            int assigning = (int) mapPar.stream().filter(item -> !"2".equals(item.getState()) && "1".equals(item.getIsSinforAc())).count();
            areaVO.setAssigning(assigning);
            if ((assigned + assigning) <= sum){
                areaVO.setRemainingNumber(sum - (assigned + assigning));
            }

        }
        areaVO.setSum(sum);
        areaVOList.add(areaVO);

        return cpuVo;
    }

    private CalculationResourceStatisticsVO assembelr(String resourceName, Map<String, List<ConfigInfo>> mapPar) {
        CalculationResourceStatisticsVO cpuVo = new CalculationResourceStatisticsVO();
        cpuVo.setId(SnowFlakeIdGenerator.getInstance().nextId());
        cpuVo.setResourceName(resourceName);

        List<AreaVO> areaVOList = new ArrayList<>();
        for (Map.Entry<String, List<ConfigInfo>> map : mapPar.entrySet()) {
            int sum = Optional.ofNullable(iaasAreaSumMapper.queryServerNameAndAreaName(resourceName, map.getKey())).orElse(0);
            AreaVO areaVO = new AreaVO();
            areaVO.setId(SnowFlakeIdGenerator.getInstance().nextId());
            areaVO.setAreaName(map.getKey());
            int assigned = (int) map.getValue().stream().filter(item -> "2".equals(item.getState())).count();
            areaVO.setAssigned(assigned);
            int assigning = (int) map.getValue().stream().filter(item -> !"2".equals(item.getState())).count();
            areaVO.setAssigning(assigning);
            if ((assigned + assigning) <= sum){
                areaVO.setRemainingNumber(sum - (assigned + assigning));
            }
            areaVO.setSum(sum);
            areaVOList.add(areaVO);
        }

        cpuVo.setChildren(areaVOList);
        return cpuVo;
    }

    private void handleAssignedTotalTree(List<IaasApplyReport> tree, List<CloudResourceAssignedStatisticsVO> resourceStatisticsVOS) {
        for (IaasApplyReport report : tree) {
            CloudResourceAssignedStatisticsVO vo = new CloudResourceAssignedStatisticsVO();
            if (report.getParentId() != null) {
                vo.setId(report.getBusinessId());
                vo.setName(applyReportMapper.queryParentName(report.getParentId()));
            }else {
                vo.setId(report.getBusinessId());
                vo.setName(report.getBusinessGroupName());
            }

            List<ApplyInfo> applyInfoList = report.getApplyInfoList();

            assembler(vo, report, applyInfoList);
            if (CollectionUtils.isNotEmpty(report.getChildren())) {
                List<IaasApplyReport> childrens = report.getChildren();
                for (IaasApplyReport children : childrens) {
                    assembler(vo, children, children.getApplyInfoList());
                }
            }
            resourceStatisticsVOS.add(vo);
        }
    }

    private void assembler(CloudResourceAssignedStatisticsVO vo, IaasApplyReport report, List<ApplyInfo> applyInfoList) {
        if (CollectionUtils.isNotEmpty(applyInfoList)) {
            List<ThreeDepartment> threeDepartments = new ArrayList<>();
            for (ApplyInfo applyInfo : applyInfoList) {
                ThreeDepartment department = new ThreeDepartment();
                department.setId(applyInfo.getApplyId());
                department.setThreeName(report.getBusinessGroupName());
                department.setSystemNum(report.childrenNum());

                List<ConfigInfo> vmAssigned = applyInfo.getConfigInfoList().stream().filter(item -> "1".equals(item.getApplyType()) && "2".equals(item.getState())).collect(Collectors.toList());

                //虚拟机分区
                Map<String, List<ConfigInfo>> areaList = vmAssigned.stream().collect(Collectors.groupingBy(ConfigInfo::getAreaName));

                //裸金属
                List<ConfigInfo> luojinshuList = applyInfo.getConfigInfoList().stream().filter(item -> "2".equals(item.getApplyType()) && "2".equals(item.getState())).collect(Collectors.toList());
                    Map<String, List<ConfigInfo>> aluojinshuAreaList = luojinshuList.stream().filter(e -> StringUtils.isNotBlank(e.getAreaName())).collect(Collectors.groupingBy(ConfigInfo::getAreaName));

                List<AreaInfoVO> areaInfoVOS = new ArrayList<>();
                for (Map.Entry<String, List<ConfigInfo>> map : areaList.entrySet()) {
                    AreaInfoVO areaInfoVO = new AreaInfoVO();
                    areaInfoVO.setId(map.getValue().get(0).getConfigId());
                    areaInfoVO.setArea(map.getKey());
                    areaInfoVO.setCpu(map.getValue().stream().mapToInt(ConfigInfo::getCpu).sum());
                    areaInfoVO.setMemory(map.getValue().stream().mapToInt(ConfigInfo::getMemory).sum());
                    areaInfoVO.setDisk(map.getValue().stream().mapToInt(item -> item.storage()).sum());
                    areaInfoVO.setVmNum(map.getValue().size());

                    for (Map.Entry<String, List<ConfigInfo>> mapluo : aluojinshuAreaList.entrySet()) {
                        if (map.getKey().equals(mapluo.getKey())) {
                            Map<String, List<ConfigInfo>> model = mapluo.getValue().stream().collect(Collectors.groupingBy(ConfigInfo::getModel));

                            List<BareMetalAssignedVO> bareMetalVOS = new ArrayList<>();
                            for (Map.Entry<String, List<ConfigInfo>> mapmodel : model.entrySet()) {
                                BareMetalAssignedVO bareMetalVO = new BareMetalAssignedVO();
                                bareMetalVO.setBareMetalName(mapmodel.getKey());
                                bareMetalVO.setBareMetal(mapmodel.getValue().stream().filter(item -> "2".equals(item.getState())).count());
                                bareMetalVOS.add(bareMetalVO);
                            }
                        }
                    }
                    areaInfoVOS.add(areaInfoVO);
                }
                department.setChildren(areaInfoVOS);
                threeDepartments.add(department);
            }

            Map<String, List<ThreeDepartment>> collect = threeDepartments.stream().collect(Collectors.groupingBy(ThreeDepartment::getThreeName));
            List<ThreeDepartment> departments = new ArrayList<>();
            for (Map.Entry<String, List<ThreeDepartment>> mapluo : collect.entrySet()) {
                ThreeDepartment department = new ThreeDepartment();
                department.setId(mapluo.getValue().get(0).getId());
                department.setThreeName(mapluo.getKey());
                List<AreaInfoVO> children = new ArrayList<>();
                for (ThreeDepartment dd : mapluo.getValue()) {
                    children.addAll(dd.getChildren());
                }
                department.setChildren(children);
                department.setSystemNum(mapluo.getValue().size());
                departments.add(department);
            }
            vo.setChildren(departments);
        }
    }

    private void handleTotalTree(String type, List<IaasApplyReport> tree, List<CloudResourceStatisticsVO> resourceStatisticsVOS) {
        for (IaasApplyReport report : tree) {
            List<ConfigInfo> configInfoList = new ArrayList<>();
            if ("1".equals(type)) {
                CloudResourceStatisticsVO vo = new CloudResourceStatisticsVO();
                vo.setName(report.getBusinessGroupName());

                handleTreeChildren(report.getChildren(), configInfoList);

                calculation(vo, configInfoList);
                resourceStatisticsVOS.add(vo);
            } else {
                if (CollectionUtils.isEmpty(report.getChildren())) {
                    CloudResourceStatisticsVO vo = new CloudResourceStatisticsVO();
                    vo.setName(report.getBusinessGroupName());

                    handleConfig(report.getApplyInfoList(), configInfoList);

                    calculation(vo, configInfoList);
                    resourceStatisticsVOS.add(vo);
                } else {
                    handleTotalTree(type, report.getChildren(), resourceStatisticsVOS);
                }
            }
        }
    }

    private void calculation(CloudResourceStatisticsVO vo, List<ConfigInfo> configInfoList) {
        if (CollectionUtils.isNotEmpty(configInfoList)) {
            //取出虚拟机
            List<ConfigInfo> vmList = configInfoList.stream().filter(item -> "1".equals(item.getApplyType())).collect(Collectors.toList());
            vo.setVmNum(vmList.size());

            //虚拟机已分配
            List<ConfigInfo> vmAssigned = vmList.stream().filter(item -> "2".equals(item.getState())).collect(Collectors.toList());
            vo.setCpuAssigned(vmAssigned.stream().mapToInt(ConfigInfo::getCpu).sum());
            vo.setMemoryAssigned(vmAssigned.stream().mapToInt(ConfigInfo::getMemory).sum());
            vo.setStorageAssigned(vmAssigned.stream().mapToInt(item -> item.storage()).sum());

            List<ConfigInfo> vm = vmList.stream().filter(item -> !"2".equals(item.getState())).collect(Collectors.toList());
            vo.setCpu(vm.stream().mapToInt(ConfigInfo::getCpu).sum());
            vo.setMemory(vm.stream().mapToInt(ConfigInfo::getMemory).sum());
            vo.setStorage(vm.stream().mapToInt(item -> item.storage()).sum());

            //根据型号取出裸金属
            List<ConfigInfo> luojinshuList = configInfoList.stream().filter(item -> "2".equals(item.getApplyType())).collect(Collectors.toList());

            Map<String, List<ConfigInfo>> collect = luojinshuList.stream().filter(e -> StringUtils.isNotBlank(e.getModel())).collect(Collectors.groupingBy(ConfigInfo::getModel));

            List<BareMetalVO> bareMetalVOS = new ArrayList<>();
            for (Map.Entry<String, List<ConfigInfo>> map : collect.entrySet()) {
                BareMetalVO bareMetalVO = new BareMetalVO();
                bareMetalVO.setBareMetalName(map.getKey());
                bareMetalVO.setBareMetalAssigned(map.getValue().stream().filter(item -> "2".equals(item.getState())).count());
                bareMetalVO.setBareMetal(map.getValue().stream().filter(item -> !"2".equals(item.getState())).count());
                bareMetalVOS.add(bareMetalVO);
            }
            vo.setBareMetalVOS(bareMetalVOS);
        }
    }

    private void handleTreeChildren(List<IaasApplyReport> children, List<ConfigInfo> configInfoList) {
        for (IaasApplyReport report : children) {
            if (CollectionUtils.isEmpty(report.getChildren())) {
                handleConfig(report.getApplyInfoList(), configInfoList);
            } else {
                handleTreeChildren(report.getChildren(), configInfoList);
            }
        }
    }

    private List<ConfigInfo> handleConfig(List<ApplyInfo> applyInfoList, List<ConfigInfo> configInfoList) {
        for (ApplyInfo applyInfo : applyInfoList) {
            configInfoList.addAll(applyInfo.getConfigInfoList());
        }
        return configInfoList;
    }

    public List<IaasApplyReport> getTree(List<IaasApplyReport> menu) {
        List<IaasApplyReport> list = new ArrayList<>();
        List<IaasApplyReport> list2 = new ArrayList<>();
        for (IaasApplyReport vo : menu) {
            if (vo.getParentId() == null) {
                vo.setChildren(getChild(vo.getBusinessId(), menu));
                list.add(vo);
            } else {
                list2.add(vo);
            }
        }

        if (CollectionUtils.isEmpty(list)) {
            return list2;
        } else {
            return list;
        }
    }

    public List<IaasApplyReport> getChild(Long id, List<IaasApplyReport> queryData) {
        List<IaasApplyReport> lists = new ArrayList<>();
        for (IaasApplyReport vo : queryData) {
            if (id.equals(vo.getParentId()) && CollectionUtils.isNotEmpty(vo.getApplyInfoList())) {
                vo.setChildren(getChild(vo.getBusinessId(), queryData));
                lists.add(vo);
            }
        }
        return lists;
    }


    /**
     * 根据年份和月分生成一个月每天日期
     * @param yearParam
     * @param monthParam
     * @return
     */
    public static List<String> getDayByMonth(int yearParam,int monthParam){
        List list = new ArrayList();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        aCalendar.set(yearParam,monthParam,1);
        int year = aCalendar.get(Calendar.YEAR);//年份
        int month = aCalendar.get(Calendar.MONTH) + 1;//月份
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String aDate=null;
            if(month<10&&i<10){
                aDate = String.valueOf(year)+"-0"+month+"-0"+i;
            }
            if(month<10&&i>=10){
                aDate = String.valueOf(year)+"-0"+month+"-"+i;
            }
            if(month>=10&&i<10){
                aDate = String.valueOf(year)+"-"+month+"-0"+i;
            }
            if(month>=10&&i>=10){
                aDate = String.valueOf(year)+"-"+month+"-"+i;
            }

            list.add(aDate);
        }
        return list;
    }





    public static List getLastDayOfMonth(int year) {

        Integer time[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List times = new ArrayList();
        for(Integer integer : time) {
            if(integer<10){
                if(integer==2){
                    times.add(year+"-0"+integer+"-28");
                }else {
                    times.add(year+"-0"+integer+"-30");
                }
            }else {
                times.add(year+"-"+integer+"-30");
            }

        }


      return times;
    }





}
