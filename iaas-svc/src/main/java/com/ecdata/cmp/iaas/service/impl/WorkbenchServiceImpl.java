package com.ecdata.cmp.iaas.service.impl;

import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.APPResourceVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupResourceCapacityVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupResourceStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupUserDataVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.BusinessGroupUserVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostApplyCountDataVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostApplyCountVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostDistributionPieDataNewVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostDistributionPieDataVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.HostDistributionPieVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.ResourcePoolStatisticsVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.UserBusinessGroupResourceVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.UserResourceVO;
import com.ecdata.cmp.iaas.entity.dto.workbench.VirtualMachineCapacityVO;
import com.ecdata.cmp.iaas.mapper.WorkbenchMapper;
import com.ecdata.cmp.iaas.service.WorkbenchService;
import com.ecdata.cmp.iaas.utils.BusinessGroupTreeUtil;
import com.ecdata.cmp.iaas.utils.BusinessGroupVMTreeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkbenchServiceImpl implements WorkbenchService {
    @Autowired
    private WorkbenchMapper workbenchMapper;

    @Override
    public HostDistributionPieVO hostDistributionPieDataMap(List<Long> userBusinessGroup) {
        List<HostDistributionPieDataVO> hostDistributionPieDataVOS = workbenchMapper.queryHostDistribution(userBusinessGroup);

        HostDistributionPieVO pieVO = new HostDistributionPieVO();

        if (CollectionUtils.isNotEmpty(hostDistributionPieDataVOS)) {
            pieVO.setSum(hostDistributionPieDataVOS.stream().mapToInt(HostDistributionPieDataVO::getCount).sum());
            pieVO.setData(hostDistributionPieDataVOS);
        }
        return pieVO;
    }

    @Override
    public HostDistributionPieVO hostDistributionPieDataMapNew(List<Long> userBusinessGroup) {
        List<HostDistributionPieDataNewVO> hostDistributionPieDataNewVOS = workbenchMapper.queryHostDistributionNew(userBusinessGroup);

        HostDistributionPieVO pieVO = new HostDistributionPieVO();

        if (CollectionUtils.isEmpty(hostDistributionPieDataNewVOS)) {
            return pieVO;
        }

        BusinessGroupVMTreeUtil util = new BusinessGroupVMTreeUtil();
        List<HostDistributionPieDataNewVO> treeList = util.getTree(hostDistributionPieDataNewVOS);

        List<HostDistributionPieDataVO> data = new ArrayList<>();

        for (HostDistributionPieDataNewVO vo : treeList) {
            HostDistributionPieDataVO pieDataVO = new HostDistributionPieDataVO();
            pieDataVO.setItem(vo.getName());

            int childVmSum = sumVMCount(vo.getChildren()) + vo.vmSum();
            pieDataVO.setCount(childVmSum);//?????????????????????????????????????????????????????????

            data.add(pieDataVO);
        }

        pieVO.setSum(data.stream().mapToInt(HostDistributionPieDataVO::getCount).sum());
        pieVO.setData(data);

        return pieVO;
    }

    private int sumVMCount(List<HostDistributionPieDataNewVO> list) {
        int total = 0;
        for (HostDistributionPieDataNewVO vo : list) {
            List<HostDistributionPieDataNewVO> children = vo.getChildren();
            total += vo.vmSum();
            if (CollectionUtils.isNotEmpty(children)) {
                sumVMCount(children);
            }
        }
        return total;
    }

    @Override
    public BusinessGroupUserVO queryBusinessGroupUserCount(List<Long> userBusinessGroup) {
        List<BusinessGroupUserDataVO> dataVOS = workbenchMapper.queryBusinessGroupUserCount(userBusinessGroup);

        BusinessGroupUserVO userVO = new BusinessGroupUserVO();
        if (CollectionUtils.isNotEmpty(dataVOS)) {
            userVO.setSum(dataVOS.stream().mapToInt(BusinessGroupUserDataVO::getLitres).sum());
            userVO.setData(dataVOS);
        }
        return userVO;
    }

    @Override
    public List<HostApplyCountVO> queryBusinessApplyCount(List<Long> userBusinessGroup) {
        List<HostApplyCountDataVO> hostApplyCountDataVOS = workbenchMapper.queryBusinessApplyCount(userBusinessGroup);

        //????????????????????????
        Map<String, List<HostApplyCountDataVO>> dateMap = hostApplyCountDataVOS.stream().collect(Collectors.groupingBy(HostApplyCountDataVO::getDateStr));

        //?????????????????????????????????
        List<HostApplyCountVO> hostApplyCountVOS = new ArrayList<>();
        for (Map.Entry<String, List<HostApplyCountDataVO>> map : dateMap.entrySet()) {
            HostApplyCountVO applyCountVO = new HostApplyCountVO();
            applyCountVO.setDate(map.getKey());
            applyCountVO.setData(handleBusinessApply(map.getValue()));

            hostApplyCountVOS.add(applyCountVO);
        }

        //??????????????????
        Collections.sort(hostApplyCountVOS, Comparator.comparing(HostApplyCountVO::getDate));
        return hostApplyCountVOS;
    }

    @Override
    public List<BusinessGroupResourceStatisticsVO> queryBusinessGroupResourceStatistics(List<Long> userBusinessGroup) {
        List<BusinessGroupStatisticsVO> businessGroupStatisticsVOS = workbenchMapper.queryBusinessGroupResourceStatistics(userBusinessGroup);

        List<BusinessGroupResourceStatisticsVO> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(businessGroupStatisticsVOS)) {
            return resultList;
        }

        BusinessGroupTreeUtil util = new BusinessGroupTreeUtil();
        List<BusinessGroupResourceStatisticsVO> objects = util.getTree(businessGroupStatisticsVOS);

        resultList.addAll(objects);
        return resultList;
    }

    @Override
    public List<UserBusinessGroupResourceVO> queryUserBusinessGroupResourceStatistics(List<Long> userBusinessGroup) {

        List<UserResourceVO> userResourceVOS = workbenchMapper.queryUserBusinessGroupResourceStatistics(userBusinessGroup);
        List<UserBusinessGroupResourceVO> resultList = new ArrayList<>();

        if (CollectionUtils.isEmpty(userResourceVOS)) {
            return resultList;
        }

        for (UserResourceVO userResourceVO : userResourceVOS) {
            assemblerUserBusinessGroupResourceVO(userResourceVO, resultList);
        }
        return resultList;
    }

    @Override
    public List<UserBusinessGroupResourceVO> queryUserBusinessGroupResourceStatisticsNew(List<Long> userBusinessGroup) {
        List<APPResourceVO> appResourceVOS = workbenchMapper.queryUserBusinessGroupResourceStatisticsNew(userBusinessGroup);

        List<UserBusinessGroupResourceVO> resultList = new ArrayList<>();

        if (CollectionUtils.isEmpty(appResourceVOS)) {
            return resultList;
        }

        for (APPResourceVO appResourceVO : appResourceVOS) {
            UserBusinessGroupResourceVO vo = new UserBusinessGroupResourceVO();
            vo.setGroupName(appResourceVO.getBusinessGroupName());
            vo.setResourceNum(CollectionUtils.isEmpty(appResourceVO.getAppvmvoList()) ? 0 : appResourceVO.getAppvmvoList().size());
            resultList.add(vo);
        }
        return resultList;
    }

    @Override
    public List<VirtualMachineCapacityVO> queryVirtualMachineCapacity(List<Long> userBusinessGroup) {
        return workbenchMapper.queryVirtualMachineCapacity(userBusinessGroup);
    }

    @Override
    public BusinessGroupResourceCapacityVO queryBusinessGroupResourceCapacity(List<Long> userBusinessGroup) {
        List<BusinessGroupStatisticsVO> businessGroupStatisticsVOS = workbenchMapper.queryBusinessGroupResourceStatistics(userBusinessGroup);

        BusinessGroupResourceCapacityVO resultCapacityVO = new BusinessGroupResourceCapacityVO();
        List<BusinessGroupResourceStatisticsVO> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(businessGroupStatisticsVOS)) {
            return resultCapacityVO;
        }

        //??????????????????
        for (BusinessGroupStatisticsVO vo : businessGroupStatisticsVOS) {
            resultList.add(assemblerBusinessGroupResourceStatisticsVO(vo));
        }

        //?????????cpu??????
        List<BusinessGroupResourceStatisticsVO> cpuTop5 = resultList.stream().sorted(Comparator.comparing(BusinessGroupResourceStatisticsVO::getCpu).reversed()).limit(5).collect(Collectors.toList());

        //?????????????????????
        List<BusinessGroupResourceStatisticsVO> memoryTop5 = resultList.stream().sorted(Comparator.comparing(BusinessGroupResourceStatisticsVO::getMemory).reversed()).limit(5).collect(Collectors.toList());

        resultCapacityVO.setCpuTop5(cpuTop5);
        resultCapacityVO.setMemoryTop5(memoryTop5);
        return resultCapacityVO;
    }

    //?????????????????????????????????
    private void assemblerUserBusinessGroupResourceVO(UserResourceVO userResourceVO, List<UserBusinessGroupResourceVO> resultList) {
        List<BusinessGroupStatisticsVO> businessGroupStatisticsVOS = userResourceVO.getBusinessGroupStatisticsVOS();

        if (CollectionUtils.isEmpty(businessGroupStatisticsVOS)) {
            return;
        }

        for (BusinessGroupStatisticsVO businessGroupStatisticsVO : businessGroupStatisticsVOS) {
            UserBusinessGroupResourceVO resultVO = new UserBusinessGroupResourceVO();
            resultVO.setUserName(userResourceVO.getName());
            resultVO.setGroupName(businessGroupStatisticsVO.getBusinessGroupName());
            List<ResourcePoolStatisticsVO> poolStatisticsVOS = businessGroupStatisticsVO.getPoolStatisticsVOS();
            if (CollectionUtils.isNotEmpty(poolStatisticsVOS)) {
                List<IaasVirtualMachineVO> list = new ArrayList<>();
                for (ResourcePoolStatisticsVO poolVO : poolStatisticsVOS) {
                    List<IaasVirtualMachineVO> iaasVirtualMachineVOList = poolVO.getIaasVirtualMachineVOList();
                    if (CollectionUtils.isNotEmpty(iaasVirtualMachineVOList)) {
                        list.addAll(iaasVirtualMachineVOList);
                    }
                }
                resultVO.setResourceNum(list.size());
            }
            resultList.add(resultVO);
        }
    }

    //???????????????????????????
    private BusinessGroupResourceStatisticsVO assemblerBusinessGroupResourceStatisticsVO(BusinessGroupStatisticsVO vo) {
        BusinessGroupResourceStatisticsVO statisticsVO = new BusinessGroupResourceStatisticsVO();
        statisticsVO.setKey(vo.getId());//?????????id
        statisticsVO.setName(vo.getBusinessGroupName());//???????????????

        List<ResourcePoolStatisticsVO> poolStatisticsVOS = vo.getPoolStatisticsVOS();

        if (CollectionUtils.isNotEmpty(poolStatisticsVOS)) {
            statisticsVO.setResourceNum(poolStatisticsVOS.size());
            statisticsVO.setCpu(vo.cpuTotal());
            statisticsVO.setMemory(vo.memoryTotal());
            statisticsVO.setVirtual(vo.vmTotal());
            statisticsVO.setVirtualUse(vo.vmUsed());
        }
        return statisticsVO;
    }

    private HashMap<String, Integer> handleBusinessApply(List<HostApplyCountDataVO> applyCountDataVOList) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        if (CollectionUtils.isEmpty(applyCountDataVOList)) {
            return map;
        }

        for (HostApplyCountDataVO vo : applyCountDataVOList) {
            map.put(vo.getBusinessGroupName(), vo.getTotal());
        }

        return map;
    }
}
