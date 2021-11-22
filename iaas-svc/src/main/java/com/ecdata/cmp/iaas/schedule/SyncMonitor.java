package com.ecdata.cmp.iaas.schedule;

import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.request.VMRequest;
import com.ecdata.cmp.iaas.mapper.IaasClusterMapper;
import com.ecdata.cmp.iaas.mapper.IaasProviderMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineMapper;
import com.ecdata.cmp.iaas.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class SyncMonitor {
    @Autowired
    private ProviderService providerService;

    @Autowired
    private IaasClusterMapper iaasClusterMapper;

    @Autowired
    private IaasVirtualMachineMapper iaasVirtualMachineMapper;

    @Autowired
    private IaasProviderMapper iaasProviderMapper;

    /**
     * 同步虚拟机到虚拟机监控表 每2小时同步一次
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void autoSynVMToMonitor() {
//        Sign.setCurrentTenantId(10000L);
//        //查询出项目下同步过的虚拟机
//        List<Map<String, Object>> list = iaasVirtualMachineMapper.queryVDCAndProjectInfo();
//        if (CollectionUtils.isNotEmpty(list)) {
//            for (Map<String, Object> vo : list) {
//                if (vo.get("username") != null && vo.get("password") != null && vo.get("projectKey") != null) {
//                    providerService.autoSyncVMToMonitor((String) vo.get("username"), (String) vo.get("password"), (String) vo.get("projectKey"), (Long) vo.get("providerId"));
//                }
//            }
//        }
        log.info("同步虚拟机到虚拟机监控表！");
    }

    /**
     * 同步主机到虚拟机监控表 每2小时同步一次
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void autoSyncPhysicalHostToMonitor() {
//        Sign.setCurrentTenantId(10000L);
//        providerService.autoSyncPhysicalHostToMonitor("", "");
        log.info("同步主机到虚拟机监控表！");
    }

    /**
     * 同步集群到虚拟机监控表 每2小时同步一次
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void syncClusterResource() {
//        Sign.setCurrentTenantId(10000L);
//        List<IaasClusterVo> clusterVos = iaasClusterMapper.getInfoByClusterVO(new IaasClusterVo());
//        if (CollectionUtils.isNotEmpty(clusterVos)) {
//            for (IaasClusterVo vo : clusterVos) {
//                IaasProviderVO iaasProviderVO = iaasProviderMapper.queryProviderByAreaId(vo.getAreaId());
//                if (iaasProviderVO == null || StringUtils.isBlank(iaasProviderVO.getOcInterfaceUsername())
//                        || StringUtils.isBlank(iaasProviderVO.getOcInterfacePassword())) {
//                    continue;
//                }
//                VMRequest vmRequest = new VMRequest();
//                vmRequest.setUsername(iaasProviderVO.getOcInterfaceUsername());
//                vmRequest.setPassword(iaasProviderVO.getOcInterfacePassword());
//                vmRequest.setClusterKey(vo.getClusterKey());
//                providerService.syncClusterResource(vmRequest);
//            }
            log.info("同步集群到虚拟机监控表！");
//        }
    }
}
