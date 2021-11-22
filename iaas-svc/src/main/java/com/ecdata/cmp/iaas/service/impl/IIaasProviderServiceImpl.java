package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.IaasProvider;
import com.ecdata.cmp.iaas.entity.dto.*;
import com.ecdata.cmp.iaas.entity.dto.response.provider.AreaResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.ClusterNetworkResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.ClusterResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.DatastoreResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.HostResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.ProviderResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.ResourcePoolResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.VirtualMachineResponse;
import com.ecdata.cmp.iaas.mapper.IaasAreaMapper;
import com.ecdata.cmp.iaas.mapper.IaasClusterMapper;
import com.ecdata.cmp.iaas.mapper.IaasProviderMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualDataCenterMapper;
import com.ecdata.cmp.iaas.service.IIaasProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @title: iaasProvider impl
 * @Author: shig
 * @description: 供应商实现类
 * @Date: 2019/11/12 4:35 下午
 */
@Slf4j
@Service
public class IIaasProviderServiceImpl extends ServiceImpl<IaasProviderMapper, IaasProvider> implements IIaasProviderService {

    @Autowired
    private IaasAreaMapper iaasAreaMapper;

    @Autowired
    private IaasClusterMapper iaasClusterMapper;

    @Autowired
    private IaasVirtualDataCenterMapper iaasVirtualDataCenterMapper;

    @Override
    public IaasProvider getInfoByProvider(IaasProvider iaasProvider) {
        return baseMapper.getInfoByProvider(iaasProvider);
    }

    @Override
    public void updateIaasProvider(Long id, Long createUser) {
        baseMapper.updateIaasProvider(id, createUser);
    }

    @Override
    public IPage<IaasProviderVO> queryIaasProviderPage(Page<IaasProviderVO> page, String keyword, String type) {
        return baseMapper.queryIaasProviderPage(page, keyword, type);
    }

    @Override
    public ContainerImageResourceVO queryIaasProviderInfo(IaasProviderVO providerVO) {
        ProviderResponse provider = baseMapper.queryDataCenter(providerVO);

        if (provider == null) {
            log.error("获取供应商信息为空!");
            return null;
        }

        List<AreaResponse> areaList = provider.getAreaList();
        if (CollectionUtils.isEmpty(areaList)) {
            log.error("获取区域信息为空!");
            return null;
        }

        ContainerImageResourceVO resourceVO = new ContainerImageResourceVO();

        //处理资源统计
        resourceVO.setResourceStatistics(handleResourceStatistics(areaList));

        //处理数据中心概况
        resourceVO.setDataCenterOverviewVOList(handleDataCenterOverview(areaList));

        //处理群集和主机概况
        resourceVO.setClusterAndHostOverview(handleClusterAndHost(areaList));

        //处理数据存储概况
        resourceVO.setDataDatastoreVOList(handleDataDatastore(areaList));

        return resourceVO;
    }

    @Override
    public HuaWeiContainerImageResourceVO queryHuaWeiResourceInfo(Long providerId) {
        HuaWeiContainerImageResourceVO result = new HuaWeiContainerImageResourceVO();

        IaasProviderVO iaasProvider = new IaasProviderVO();
        iaasProvider.setId(providerId);
        ProviderResponse providerResponse = baseMapper.queryDataCenter(iaasProvider);

        //集群主机信息
        List<ClusterResponse> clusters = providerResponse.clusters();

        //数据中心概况
        DataCenterOverviewVO dataCenterOverviewVO = new DataCenterOverviewVO();
        dataCenterOverviewVO.setAreaName("私有云");
        dataCenterOverviewVO.setClusterNum(clusters.size());//集群个数
        dataCenterOverviewVO.setHostNum(providerResponse.hostNum());//主机个数
        dataCenterOverviewVO.setVirtualNum(providerResponse.vmNum());//虚拟机个数
        dataCenterOverviewVO.setCpuTotal(providerResponse.cpuTotal());
        dataCenterOverviewVO.setCpuUsed(providerResponse.cpuUsed());
        dataCenterOverviewVO.setMemoryTotal(providerResponse.memoryTotal());
        dataCenterOverviewVO.setMemoryUsed(providerResponse.memoryUsed());
        dataCenterOverviewVO.setDatastoreTotal(providerResponse.diskTotal());
        dataCenterOverviewVO.setDatastoreUsed(providerResponse.diskUsed());

        result.setDataCenterOverviewVO(dataCenterOverviewVO);

        //集群和宿主机概况
        List<ClusterAndHostOverviewVO> clusterAndHostOverviewVOArrayList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(clusters)) {
            for (ClusterResponse clusterResponse : clusters) {
                ClusterAndHostOverviewVO clusterAndHostOverviewVO = new ClusterAndHostOverviewVO();
                clusterAndHostOverviewVO.setClusterDataCenter(providerResponse.getAreaList().get(0).getAreaName());
                clusterAndHostOverviewVO.setClusterName(clusterResponse.getClusterName());
                clusterAndHostOverviewVO.setClusterCpuTotal(clusterResponse.getCpuTotal());
                clusterAndHostOverviewVO.setClusterCpuReserved(clusterResponse.getCpuUsed());
                clusterAndHostOverviewVO.setClusterMemoryTotal(clusterResponse.getMemoryTotal());
                clusterAndHostOverviewVO.setClusterMemoryReserved(clusterResponse.getMemoryUsed());
                clusterAndHostOverviewVO.setClusterSpaceTotal(clusterResponse.getDiskTotal());
                clusterAndHostOverviewVO.setClusterSpaceReserved(clusterResponse.getDiskUsed());
                List<HostResponse> hostList = clusterResponse.getHostList();
                if (CollectionUtils.isNotEmpty(hostList)) {
                    List<HostOverviewVO> hostOverviewList = new ArrayList<>();
                    for (HostResponse hostResponse : hostList) {
                        HostOverviewVO hostOverviewVO = new HostOverviewVO();
                        hostOverviewVO.setHostNname(hostResponse.getHostName());
                        hostOverviewVO.setHostCpuTotal(hostResponse.getCpuTotal());
                        hostOverviewVO.setHostCpuUsed(hostResponse.getCpuUsed());
                        hostOverviewVO.setHostMemoryTotal(hostResponse.getMemoryTotal());
                        hostOverviewVO.setHostMemoryUsed(hostResponse.getMemoryUsed());
                        hostOverviewVO.setHostSpaceTotal(hostResponse.getDiskTotal());
                        hostOverviewVO.setHostSpaceUsed(hostResponse.getDiskUsed());
                        hostOverviewList.add(hostOverviewVO);
                    }
                    clusterAndHostOverviewVO.setHostOverviewList(hostOverviewList);
                }
                clusterAndHostOverviewVOArrayList.add(clusterAndHostOverviewVO);
            }
        }
        result.setClusterAndHostOverview(clusterAndHostOverviewVOArrayList);

        //vdc信息
        List<IaasVirtualDataCenterVO> vdcs = iaasVirtualDataCenterMapper.queryIaasVirtualDataCentersByClusterIds(providerId);
        result.setDataCenterVOList(vdcs);
        return result;
    }

    private double doubleSplit(double value, int num) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    @Override
    public ProviderResponse queryIaasProviderHuaWeiInfo(IaasProviderVO iaasProvider) {
        return baseMapper.queryDataCenter(iaasProvider);
    }

    /**
     * 处理数据存储概况
     *
     * @param areaList
     */
    private List<DataDatastoreVO> handleDataDatastore(List<AreaResponse> areaList) {
        List<DataDatastoreVO> dataDatastoreVOList = new ArrayList<>();

        for (AreaResponse area : areaList) {
            List<DatastoreResponse> datastoreList = area.hostDatastoreList();
            for (DatastoreResponse datastore : datastoreList) {
                dataDatastoreVOList.add(assemblerDataDatastoreVO(area.getAreaName(), datastore));
            }
        }

        return dataDatastoreVOList;
    }

    private DataDatastoreVO assemblerDataDatastoreVO(String areaName, DatastoreResponse datastore) {
        DataDatastoreVO vo = new DataDatastoreVO();
        vo.setAreaName(areaName);
        vo.setDatastoreName(datastore.getDatastoreName());
        vo.setDriveType(datastore.getDriveType());
        vo.setSpaceTotal(datastore.getSpaceTotal());
        vo.setSpaceUsed(datastore.getSpaceUsed());
        vo.setResourcePoolNum(datastore.resourcePoolDatastoreList());
        vo.setSpaceTotalAllocate(datastore.spaceTotalAllocate());
        vo.setSpaceUsedAllocate(datastore.spaceUsedAllocate());
        return vo;
    }

    /**
     * 处理数据中心概况
     *
     * @param areaList
     * @return
     */
    private List<DataCenterOverviewVO> handleDataCenterOverview(List<AreaResponse> areaList) {
        List<DataCenterOverviewVO> dataCenterOverviewVOS = new ArrayList<>();

        for (AreaResponse area : areaList) {
            dataCenterOverviewVOS.add(assemblerDataCenterOverviewVO(area));
        }
        return dataCenterOverviewVOS;
    }

    /**
     * 组装数据中心
     *
     * @param area
     * @return
     */
    private DataCenterOverviewVO assemblerDataCenterOverviewVO(AreaResponse area) {
        DataCenterOverviewVO dataCenterOverviewVO = new DataCenterOverviewVO();

        dataCenterOverviewVO.setAreaName(area.getAreaName());
        dataCenterOverviewVO.setClusterNum(area.clusterList().size());
        dataCenterOverviewVO.setHostNum(area.hostList().size());
        dataCenterOverviewVO.setCpuTotal(area.hostCpuTotal());
        dataCenterOverviewVO.setCpuUsed(area.hostCpuUsed());
        dataCenterOverviewVO.setMemoryTotal(area.hostMemoryTotal());
        dataCenterOverviewVO.setMemoryUsed(area.hostMemoryUsed());
        dataCenterOverviewVO.setDatastoreTotal(area.hostDatastoreSpaceTotal());
        dataCenterOverviewVO.setDatastoreUsed(area.hostDatastoreSpaceUsed());
//        dataCenterOverviewVO.setVirtualNum();//todo 虚拟机表未建
        return dataCenterOverviewVO;
    }

    /**
     * 容器资源统计
     *
     * @param areaList
     * @return
     */
    private ResourceStatisticsVO handleResourceStatistics(List<AreaResponse> areaList) {
        ResourceStatisticsVO resourceStatisticsVO = new ResourceStatisticsVO();

        List<ClusterResponse> clusterResult = new ArrayList<>();
        List<HostResponse> hostResult = new ArrayList<>();
        List<DatastoreResponse> datastoreResult = new ArrayList<>();
        List<ClusterNetworkResponse> clusterNetworkResult = new ArrayList<>();
        List<VirtualMachineResponse> virtualMachineResponseList = new ArrayList<>();
        for (AreaResponse area : areaList) {
            List<ClusterResponse> clusters = area.clusterList();
            //集群个数
            clusterResult.addAll(clusters);

            //标准交换机个数
            clusterNetworkResult.addAll(clusterNetworkeList(clusters));

            //主机个数
            List<HostResponse> hosts = area.hostList();
            hostResult.addAll(hosts);

            //数据存储个数
            datastoreResult.addAll(datastoreList(hosts));

            //虚拟机总数
            virtualMachineResponseList.addAll(virtualMachine(datastoreResult));
        }

        resourceStatisticsVO.setClusterNum(clusterResult.size());
        resourceStatisticsVO.setHostNum(hostResult.size());
        resourceStatisticsVO.setDatastoreNum(datastoreResult.size());
        resourceStatisticsVO.setNetworkNum(clusterNetworkResult.size());
        resourceStatisticsVO.setVirtualNum(virtualMachineResponseList.size());
        return resourceStatisticsVO;
    }

    private List<VirtualMachineResponse> virtualMachine(List<DatastoreResponse> datastores) {
        if (CollectionUtils.isEmpty(datastores)) {
            return Collections.emptyList();
        }

        List<VirtualMachineResponse> result = new ArrayList<>();
        for (DatastoreResponse datastore : datastores) {
            result.addAll(datastore.getVirtualMachineResponseList());
        }

        return result;
    }

    private List<ClusterNetworkResponse> clusterNetworkeList(List<ClusterResponse> clusters) {
        if (CollectionUtils.isEmpty(clusters)) {
            return Collections.emptyList();
        }

        List<ClusterNetworkResponse> clusterNetworks = new ArrayList<>();

        for (ClusterResponse cluster : clusters) {
            clusterNetworks.addAll(cluster.getClusterNetworkList());
        }

        return clusterNetworks;
    }

    private List<DatastoreResponse> datastoreList(List<HostResponse> hosts) {
        if (CollectionUtils.isEmpty(hosts)) {
            return Collections.emptyList();
        }

        List<DatastoreResponse> datastores = new ArrayList<>();

        for (HostResponse host : hosts) {
            datastores.addAll(host.getDatastoreList());
        }

        return datastores;
    }

    /**
     * 处理群集和主机概况
     *
     * @param areaList
     * @return
     */
    private List<ClusterAndHostOverviewVO> handleClusterAndHost(List<AreaResponse> areaList) {

        List<ClusterAndHostOverviewVO> ClusterAndHostOverviewVOS = new ArrayList<>();

        for (AreaResponse area : areaList) {
            List<ClusterResponse> clusterList = area.getClusterList();
            if (CollectionUtils.isEmpty(clusterList)) {
                continue;
            }

            for (ClusterResponse cluster : clusterList) {
                List<DatastoreResponse> datastoreList = cluster.hostDatastoreList();
                ClusterAndHostOverviewVOS.add(assemblerClusterAndHostOverviewVO(cluster, area));
            }

        }
        return ClusterAndHostOverviewVOS;

    }

    /**
     * 群集和主机概况
     *
     * @param area
     * @return
     */
    private ClusterAndHostOverviewVO assemblerClusterAndHostOverviewVO(ClusterResponse cluster, AreaResponse area) {
        ClusterAndHostOverviewVO clusterAndHostOverviewVO = new ClusterAndHostOverviewVO();

        List<HostOverviewVO> hostOverviewVOList = new ArrayList<>();

        List<HostResponse> hostList = cluster.getHostList();
        for (HostResponse host : hostList) {
            HostOverviewVO hostOverviewVO = new HostOverviewVO();
            hostOverviewVO.setHostNname(host.getHostName());
            hostOverviewVO.setHostType(host.getExsiFullVersion());
            hostOverviewVO.setHostDataCenter(area.getAreaName());
            hostOverviewVO.setHostCpuTotal(host.getCpuTotal());
            hostOverviewVO.setHostCpuUsed(host.getCpuUsed());
            hostOverviewVO.setHostMemoryTotal(host.getMemoryTotal());
            hostOverviewVO.setHostMemoryUsed(host.getMemoryUsed());
            hostOverviewVO.setHostSpaceTotal(host.hostDatastoreSpaceTotal());
            hostOverviewVO.setHostSpaceUsed(host.hostDatastoreSpaceUsed());
            hostOverviewVOList.add(hostOverviewVO);
        }
        clusterAndHostOverviewVO.setClusterName(cluster.getClusterName());
        ResourcePoolResponse resourcePool = cluster.getResourcePool();
        if (!StringUtils.isEmpty(resourcePool)) {
            clusterAndHostOverviewVO.setResourcePoolTotal(1);
        } else {
            clusterAndHostOverviewVO.setResourcePoolTotal(0);
        }
        //虚拟机总数 todo 未创建表
        clusterAndHostOverviewVO.setClusterCpuReserved(cluster.hostCpuUsed());
        clusterAndHostOverviewVO.setClusterCpuTotal(cluster.hostCpuTotal());
        clusterAndHostOverviewVO.setClusterMemoryReserved(cluster.hostMemoryUsed());
        clusterAndHostOverviewVO.setClusterMemoryTotal(cluster.hostMemoryTotal());
        clusterAndHostOverviewVO.setClusterSpaceReserved(cluster.hostDatastoreSpaceUsed());
        clusterAndHostOverviewVO.setClusterSpaceTotal(cluster.hostDatastoreSpaceTotal());
        clusterAndHostOverviewVO.setHostOverviewList(hostOverviewVOList);

        clusterAndHostOverviewVO.setClusterDataCenter(area.getAreaName());
        return clusterAndHostOverviewVO;
    }


    /**
     * 去重供应商商
     *
     * @param providerName
     * @return
     */
    @Override
    public List<IaasProvider> disProviderName(String providerName) {
        return baseMapper.disProviderName(providerName);
    }

    @Override
    public List<IaasProviderVO> providerlistByMap(Map map) {
        return baseMapper.providerlistByMap(map);
    }

    @Override
    public List<IaasVirtualDataCenterVO> queryIaasVDCInfoByProviderId(Long providerId) {
        List<IaasVirtualDataCenterVO> vdcs = iaasVirtualDataCenterMapper.queryIaasVirtualDataCentersByClusterIds(providerId);

        List<BareMetalVO> bareMetalVOS = iaasVirtualDataCenterMapper.queryBareMetalList();
        log.info("bareMetalVOS ={}",bareMetalVOS);
        if(bareMetalVOS.size()>0&&vdcs.size()>0){
            for (IaasVirtualDataCenterVO vo1 :vdcs){
                List<IaasProjectVO> children = vo1.getChildren();
                for (IaasProjectVO vo2 : children){

                    List<IaasVirtualMachineVO> children1 = vo2.getChildren();
                        for (BareMetalVO vo4 : bareMetalVOS){
                            log.info("vo2 id={}",vo2.getId());
                            if(vo2.getId()!=null) {
                                log.info("getProjectId id={}", vo4.getProjectId());
                                if ((vo2.getId().toString()).equals(vo4.getProjectId())) {
                                    IaasVirtualMachineVO vm = new IaasVirtualMachineVO();
                                    vm.setVmName(vo4.getValue());
                                    vm.setOsName(vo4.getDetailName());
                                    vm.setBusinessGroupName(vo4.getBusinessGroupName());
                                    children1.add(vm);
                                }
                            }
                        }
                }
            }

        }

        vdcs = vdcs.stream().peek(e -> {
            List<IaasProjectVO> children = e.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                IaasProjectVO child = children.get(0);
                if (child != null && StringUtils.isEmpty(child.getProjectName())) {
                    e.setChildren(null);
                }
            }
        }).collect(Collectors.toList());

        return vdcs;
    }

    @Override
    public List<IaasProviderVO> getCloudPlatformEntrance() {
        //获取集联
        return baseMapper.getCloudPlatformEntrance();
    }
}