package com.ecdata.cmp.iaas.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.api.StringResponse;
import com.ecdata.cmp.common.auth.AuthContext;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.ecdata.cmp.huawei.client.AvailableZoneClient;
import com.ecdata.cmp.huawei.client.HostVolumesClient;
import com.ecdata.cmp.huawei.client.ImageServiceClient;
import com.ecdata.cmp.huawei.client.OauthTokenClient;
import com.ecdata.cmp.huawei.client.PhysicalHostClient;
import com.ecdata.cmp.huawei.client.ProjectsClient;
import com.ecdata.cmp.huawei.client.RegionsClient;
import com.ecdata.cmp.huawei.client.VDCVirtualMachineClient;
import com.ecdata.cmp.huawei.client.VdcsClient;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZone;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZoneResource;
import com.ecdata.cmp.huawei.dto.region.RegionEntity;
import com.ecdata.cmp.huawei.dto.response.AvailableZoneResourceResponse;
import com.ecdata.cmp.huawei.dto.response.HostVolumesVOListResponse;
import com.ecdata.cmp.huawei.dto.response.ImagesListResponse;
import com.ecdata.cmp.huawei.dto.response.PhysicalHostListResponse;
import com.ecdata.cmp.huawei.dto.response.ProjectsListResponse;
import com.ecdata.cmp.huawei.dto.response.RegionEntityResponse;
import com.ecdata.cmp.huawei.dto.response.TokenInfoResponse;
import com.ecdata.cmp.huawei.dto.response.VDCVirtualMachineListResponse;
import com.ecdata.cmp.huawei.dto.response.VdcsListResponse;
import com.ecdata.cmp.huawei.dto.token.TokenDTO;
import com.ecdata.cmp.huawei.dto.token.TokenInfoVO;
import com.ecdata.cmp.huawei.dto.vo.AvailbleZoneReqVO;
import com.ecdata.cmp.huawei.dto.vo.HostVolumesVO;
import com.ecdata.cmp.huawei.dto.vo.ImagesVO;
import com.ecdata.cmp.huawei.dto.vo.PhysicalHostVO;
import com.ecdata.cmp.huawei.dto.vo.ProjectsVO;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VdcsVO;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.iaas.entity.IaasArea;
import com.ecdata.cmp.iaas.entity.IaasCluster;
import com.ecdata.cmp.iaas.entity.IaasClusterNetwork;
import com.ecdata.cmp.iaas.entity.IaasHost;
import com.ecdata.cmp.iaas.entity.IaasHostDatastore;
import com.ecdata.cmp.iaas.entity.IaasHostNetwork;
import com.ecdata.cmp.iaas.entity.IaasImageService;
import com.ecdata.cmp.iaas.entity.IaasProject;
import com.ecdata.cmp.iaas.entity.IaasProvider;
import com.ecdata.cmp.iaas.entity.IaasResourcePool;
import com.ecdata.cmp.iaas.entity.IaasVirtualDataCenter;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachine;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineDisk;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineMonitor;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachineRight;
import com.ecdata.cmp.iaas.entity.dto.IaasProjectVO;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualDataCenterVO;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import com.ecdata.cmp.iaas.entity.dto.request.VDCRequest;
import com.ecdata.cmp.iaas.entity.dto.request.VMRequest;
import com.ecdata.cmp.iaas.entity.dto.request.VSphereRequest;
import com.ecdata.cmp.iaas.entity.dto.response.provider.AreaResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.ClusterNetworkResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.ClusterResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.DatastoreResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.HostResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.SyncDataResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.VSphereResponse;
import com.ecdata.cmp.iaas.entity.dto.response.provider.cascade.ProviderCascade;
import com.ecdata.cmp.iaas.mapper.IaasAreaMapper;
import com.ecdata.cmp.iaas.mapper.IaasClusterMapper;
import com.ecdata.cmp.iaas.mapper.IaasClusterNetworkMapper;
import com.ecdata.cmp.iaas.mapper.IaasHostDatastoreMapper;
import com.ecdata.cmp.iaas.mapper.IaasHostMapper;
import com.ecdata.cmp.iaas.mapper.IaasHostNetworkMapper;
import com.ecdata.cmp.iaas.mapper.IaasImageServiceMapper;
import com.ecdata.cmp.iaas.mapper.IaasProjectMapper;
import com.ecdata.cmp.iaas.mapper.IaasProviderMapper;
import com.ecdata.cmp.iaas.mapper.IaasResourcePoolMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualDataCenterMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineDiskMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineMonitorMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineRightMapper;
import com.ecdata.cmp.iaas.service.IProjectService;
import com.ecdata.cmp.iaas.service.IaasVirtualMachineService;
import com.ecdata.cmp.iaas.service.ProviderService;
import com.ecdata.cmp.iaas.utils.MathUtils;
import com.ecdata.cmp.user.client.SysPythonClient;
import com.ecdata.cmp.user.dto.response.PythonListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-14 14:47
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProviderServiceImpl implements ProviderService {
    private static final String SYNC_TYPE_VSPHERE = "vsphere";
    private static final String SYNC_TYPE_HWY = "hwy";
    private static final String vcenterSyncUrl = "/v1/vcenter_summary/get";

    private final IaasAreaMapper sysAreaMapper;
    private final IaasClusterMapper sysClusterMapper;
    private final IaasHostMapper sysHostMapper;
    private final IaasHostDatastoreMapper sysHostDatastoreMapper;
    private final IaasHostNetworkMapper sysHostNetworkMapper;
    private final IaasProviderMapper iaasProviderMapper;
    private final IaasClusterNetworkMapper iaasClusterNetworkMapper;
    private final IaasVirtualDataCenterMapper iaasVirtualDataCenterMapper;
    private final IaasProjectMapper iaasProjectMapper;
    private final SysPythonClient sysPythonClient;
    private final OauthTokenClient oauthTokenClient;
    private final VdcsClient vdcsClient;
    private final RegionsClient regionsClient;
    private final ProjectsClient projectsClient;
    private final HostVolumesClient hostVolumesClient;
    private final VDCVirtualMachineClient vdcVirtualMachineClient;
    private final PhysicalHostClient physicalHostClient;
    private final AvailableZoneClient availableZoneClient;
    private final ImageServiceClient imageServiceClient;
    private final IaasVirtualMachineMapper iaasVirtualMachineMapper;
    private final IaasVirtualMachineDiskMapper iaasVirtualMachineDiskMapper;
    private final IaasVirtualMachineMonitorMapper iaasVirtualMachineMonitorMapper;
    private final IaasImageServiceMapper iaasImageServiceMapper;
    private final IaasVirtualMachineRightMapper iaasVirtualMachineRightMapper;
    private final IaasResourcePoolMapper iaasResourcePoolMapper;
    private final IaasVirtualMachineService machineService;


    @Autowired
    private IProjectService projectService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse syncVSphereData(VSphereRequest request) throws IOException {
        BaseResponse baseResponse = new BaseResponse();

        String data = "";

        //1.vcenter同步请求数据
        if (SYNC_TYPE_VSPHERE.equals(request.getSyncType())) {
            PythonListResponse pythonUrl = sysPythonClient.list(AuthContext.getAuthz(), 1);
            if (pythonUrl == null && CollectionUtils.isEmpty(pythonUrl.getData())) {
                baseResponse.setCode(201);
                baseResponse.setMessage("pythonUrl为空!");
                return baseResponse;
            }

            String address = pythonUrl.getData().get(0).getAddress();

            String url = address + vcenterSyncUrl + "?" +
                    "address=" + request.address() +
                    "&username=" + request.username() +
                    "&password=" + request.password();

            OkHttpClient client = new OkHttpClient();

            Call call = client.newCall(new Request.Builder().url(url).build());

            Response response = call.execute();
            if (response.isSuccessful()) {
                data = response.body().string();
            }

            //2.华为同步请求数据
        } else if (SYNC_TYPE_HWY.equals(request.getSyncType())) {
            //5个地址和用户名密码

        }

        //3.数据接取进行落入本地数据库
        if (StringUtils.isBlank(data)) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取同步数据为空!");
            return baseResponse;
        }

        //获取供应商信息
        IaasProvider iaasProvider = iaasProviderMapper.selectById(request.getProviderId());
        if (iaasProvider == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("供应商信息为空!");
            return baseResponse;
        }

        SyncDataResponse syncDataResponse = JSON.parseObject(data, SyncDataResponse.class);

        //保存同步数据
        saveSyncData(iaasProvider, syncDataResponse.getData());

        baseResponse.setMessage("同步成功!");
        return baseResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse syncVDCData(VDCRequest request) {
        String authz = AuthContext.getAuthz();
        BaseResponse baseResponse = new BaseResponse();

        if (request == null
//                || StringUtils.isBlank(request.getUsername())
//                || StringUtils.isBlank(request.getPassword())
                ) {
            baseResponse.setCode(201);
            baseResponse.setMessage("请输入正确信息!");
            return baseResponse;
        }

        baseResponse.setCode(200);
        baseResponse.setMessage("同步成功!");
        return baseResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse syncHuaWeiData(VSphereRequest request) {
        String authz = AuthContext.getAuthz();
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateVDCInfo(VDCRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        IaasVirtualDataCenter dataCenter = new IaasVirtualDataCenter();
        dataCenter.setId(request.getVdcId());
        dataCenter.setUsername(request.getUsername());
        dataCenter.setPassword(request.getPassword());
        dataCenter.setUpdateTime(DateUtil.getNow());
        dataCenter.setUpdateUser(Sign.getUserId());

        iaasVirtualDataCenterMapper.updateById(dataCenter);
        return baseResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse addVMByVmKeyList(ResourcePoolVO vmRequest, List<String> reomveBusiIds) {
        BaseResponse baseResponse = new BaseResponse();
        List<String> vmKeyList = new ArrayList<>(vmRequest.getVmKeyList());

        QueryWrapper<IaasVirtualMachine> query = new QueryWrapper<>();
        query.eq("pool_id", vmRequest.getId());
        query.eq("is_deleted", 0);
        List<IaasVirtualMachine> iaasVirtualMachines = iaasVirtualMachineMapper.selectList(query);
        List<String> localVMKeyList = iaasVirtualMachines.stream()
                .map(e -> {
                    return e.getVmKey();
                }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(localVMKeyList)) {
            List<String> needRemoveList = new ArrayList<>(localVMKeyList);
            //vmKeyList为空 只移除本地纳管虚拟机
            if (CollectionUtils.isEmpty(vmKeyList)) {

                removeLocalVM(needRemoveList, reomveBusiIds);
                return baseResponse;
            }

            //解除本地 解绑的纳管的 vmList
            needRemoveList.removeAll(vmKeyList);
            removeLocalVM(needRemoveList, reomveBusiIds);
        } else if (CollectionUtils.isEmpty(localVMKeyList) && CollectionUtils.isEmpty(vmKeyList)) {
            baseResponse.setCode(201);
            baseResponse.setMessage("没有可以纳管的虚拟机");
            return baseResponse;
        }

        //纳管虚拟机
        String authz = AuthContext.getAuthz();


        IaasProviderVO iaasProviderVO = iaasProviderMapper.queryIaasProviderVOById(vmRequest.getProviderId());
        if (iaasProviderVO == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商信息为空!");
            return baseResponse;
        }

        //1.通过用户名密码获取供应商token
        TokenInfoResponse tokenResponse = getToken(authz, iaasProviderVO);

//        if (tokenResponse == null) {
//            baseResponse.setCode(201);
//            baseResponse.setMessage("获取供应商token错误!");
//            return baseResponse;
//        }

        //通过用户名密码获取vdc token
        IaasVirtualDataCenter ivdc = iaasVirtualDataCenterMapper.selectById(vmRequest.getVdcId());
        IaasProject iaasProject = projectService.queryIaasProjectById(vmRequest.getProjectId());
        String projectKey = iaasProject.getProjectKey();

//        StringResponse tokenByVdcUser = null;
//        try {
//            tokenByVdcUser = oauthTokenClient.getTokenByVdcUser(authz,
//                    projectKey,
//                    ivdc.getDomainName(), ivdc.getUsername(), ivdc.getPassword());
//            //pid.toString
////                    iaasProviderVO.getDomainName(),//"vdc_JT",    //
////                    iaasProviderVO.getAuthUsername(),//"vdcjtadmin",
////                    iaasProviderVO.getAuthPassword());//"Huawei12#$");
//        } catch (Exception e) {
//            log.info("获取vdc token错误！");
//        }


        RequestVO requestVO = new RequestVO();
        requestVO.setOmToken(null);//omToken
        requestVO.setOcToken(null);//vdc token
        requestVO.setProjectId(vmRequest.getProjectKey());

        //获取虚拟机信息
        VDCVirtualMachineListResponse vmResponse = null;
        try {
            vmResponse = vdcVirtualMachineClient.getVirtualMachineList(authz, requestVO);
        } catch (Exception e) {
            log.error("获取项目下的虚拟机信息异常！", e);
        }

        if (vmResponse == null || vmResponse.getCode() != 0 || CollectionUtils.isEmpty(vmResponse.getData())) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取虚拟机信息错误!");
            return baseResponse;
        }

        //获取磁盘信息
        HostVolumesVOListResponse diskResponse = null;
        try {
            diskResponse = hostVolumesClient.getHostVolumesList(authz, requestVO);
        } catch (Exception e) {
            log.error("获取项目下的虚拟机磁盘信息异常！", e);
        }


        List<VirtualMachineVO> huaWeiVMList = vmResponse.getData();
        huaWeiVMList = huaWeiVMList.stream().filter(huaweiVM -> vmKeyList.contains(huaweiVM.getNativeId())).collect(Collectors.toList());

        String[] businessIds = null;
        if (StringUtils.isNotBlank(vmRequest.getBusinessIds())) {
            businessIds = vmRequest.getBusinessIds().split(",");
        }

        //处理虚拟机和虚拟机磁盘信息
        handleVMInfo(vmRequest.getProviderId(), vmRequest.getProjectId(), null, businessIds, vmRequest.getId(), huaWeiVMList, diskResponse);

        //自动填充资源池-计算资源 vcpu 内存 虚机数量的已分配容量
        handlePool(vmRequest.getId(), huaWeiVMList);

        return baseResponse;
    }


    @Transactional(rollbackFor = Exception.class)
    public BaseResponse removeLocalVM(List<String> vmKeyList, List<String> reomveBusiIds) {
        BaseResponse baseResponse = new BaseResponse();
        if (!machineService.getVmByKey(vmKeyList)) {
            baseResponse.setCode(201);
            baseResponse.setMessage("虚拟机解绑失败");
            return baseResponse;
        }
        return baseResponse;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse syncProjectVM(VMRequest vmRequest) {
        String authz = AuthContext.getAuthz();
        BaseResponse baseResponse = new BaseResponse();

        //查出供应商信息获取token
        IaasProviderVO iaasProviderVO = iaasProviderMapper.queryIaasProviderVOById(vmRequest.getProviderId());
        if (iaasProviderVO == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商信息为空!");
            return baseResponse;
        }

        //1.通过用户名密码获取供应商token
        TokenInfoResponse tokenResponse = getToken(authz, iaasProviderVO.getUsername(), iaasProviderVO.getPassword());

        if (tokenResponse == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商token错误!");
            return baseResponse;
        }

        //通过用户名密码获取vdc token
        StringResponse tokenByVdcUser = null;
        try {
            tokenByVdcUser = oauthTokenClient.getTokenByVdcUser(authz,
                    vmRequest.getProjectKey(),
                    vmRequest.getDomainName(),
                    vmRequest.getUsername(),
                    vmRequest.getPassword());
        } catch (Exception e) {
            log.info("获取vdc token错误！");
        }

        TokenInfoVO tokenInfoVO = tokenResponse.getData();
        RequestVO requestVO = new RequestVO();
        requestVO.setOmToken(tokenInfoVO.getOmToken().getAccessSession());//omToken
        requestVO.setOcToken(tokenByVdcUser == null ? "" : tokenByVdcUser.getData());//vdc token
        requestVO.setProjectId(vmRequest.getProjectKey());

        //获取虚拟机信息
        VDCVirtualMachineListResponse vmResponse = null;
        try {
            vmResponse = vdcVirtualMachineClient.getVirtualMachineList(authz, requestVO);
        } catch (Exception e) {
            log.error("获取项目下的虚拟机信息异常！", e);
        }

        if (vmResponse == null || vmResponse.getCode() != 0 || CollectionUtils.isEmpty(vmResponse.getData())) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取虚拟机信息错误!");
            return baseResponse;
        }

        //获取磁盘信息
        HostVolumesVOListResponse diskResponse = null;
        try {
            diskResponse = hostVolumesClient.getHostVolumesList(authz, requestVO);
        } catch (Exception e) {
            log.error("获取项目下的虚拟机磁盘信息异常！", e);
        }

        //处理虚拟机和虚拟机磁盘信息
        handleVMInfo(vmRequest.getProviderId(), vmRequest.getProjectId(), vmRequest.getBusinessId(), vmRequest.getBusinessIds(), vmRequest.getPoolId(), vmResponse.getData(), diskResponse);

        //自动填充资源池-计算资源 vcpu 内存 虚机数量的已分配容量
        handlePool(vmRequest.getPoolId(), vmResponse.getData());
        return baseResponse;
    }

    public void handlePool(Long poolId, List<VirtualMachineVO> data) {
        IaasResourcePool pool = new IaasResourcePool();
        pool.setId(poolId);
        pool.setUpdateTime(DateUtil.getNow());
        pool.setUpdateUser(Sign.getUserId());

        double cpuUsage = 0;
        double memoryUsage = 0;
        for (VirtualMachineVO vmVO : data) {
            String flavor = vmVO.getFlavor();//4U16G|4vCPU|16GB
            String cpu = "";
            String memory = "";
            if (StringUtils.isNoneBlank(flavor) && flavor.split("|") != null) {
                String split = flavor.split("\\|")[0];
                cpu = split.substring(0, split.indexOf("U"));
                memory = split.substring(split.indexOf("U") + 1, split.indexOf("G"));
            }

            cpuUsage += StringUtils.isBlank(cpu) ? 0 : (double) (((Integer.valueOf(cpu) * vmVO.getCpuUsage()) / 100));
            memoryUsage += (double) (((Integer.valueOf(memory) * vmVO.getMemoryUsage()) / 100));
        }


        pool.setVcpuUsedAllocate(cpuUsage);//cpu已分配容量
        pool.setMemoryUsedAllocate(memoryUsage);//memory已分配容量
        pool.setVmUsedAllocate(data.size());//虚拟机个数

        iaasResourcePoolMapper.updateById(pool);
        log.info("更新资源池计算资源已分配容量成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse syncPhysicalHost(VMRequest vmRequest) {
        String authz = AuthContext.getAuthz();
        BaseResponse baseResponse = new BaseResponse();

        //获取token
        TokenInfoResponse tokenResponse = getToken(authz, vmRequest.getUsername(), vmRequest.getPassword());

        if (tokenResponse == null || tokenResponse.getCode() != 0 || tokenResponse.getData() == null || StringUtils.isBlank(tokenResponse.getData().getOcToken())) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商token错误!");
            return baseResponse;
        }

        //获取宿主机信息
        RequestVO requestVO = new RequestVO();
        requestVO.setOcToken(tokenResponse.getData().getOcToken());
        PhysicalHostListResponse physicalHostListResponse = null;
        try {
            physicalHostListResponse = physicalHostClient.getPhysicalhostList(authz, requestVO);
        } catch (Exception e) {
            log.error("获取宿主机信息错误！", e);
        }

        if (physicalHostListResponse == null || physicalHostListResponse.getCode() != 0 || CollectionUtils.isEmpty(physicalHostListResponse.getData())) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取供应商token错误!");
            return baseResponse;
        }

        handlePhysicalHostInfo(physicalHostListResponse.getData());
        return baseResponse;
    }

    @Override
    public void autoSyncPhysicalHostToMonitor(String username, String password) {
        String authz = AuthContext.getAuthz();
        //获取token
        TokenInfoResponse tokenResponse = getToken(authz, username, password);

        if (tokenResponse == null || tokenResponse.getCode() != 0 || tokenResponse.getData() == null || StringUtils.isBlank(tokenResponse.getData().getOcToken())) {
            log.info("获取供应商token错误!");
            return;
        }

        //获取宿主机信息
        RequestVO requestVO = new RequestVO();
        requestVO.setOcToken(tokenResponse.getData().getOcToken());
        PhysicalHostListResponse physicalHostListResponse = null;
        try {
            physicalHostListResponse = physicalHostClient.getPhysicalhostList(authz, requestVO);
        } catch (Exception e) {
            log.error("获取宿主机信息错误！", e);
            return;
        }

        if (physicalHostListResponse == null || physicalHostListResponse.getCode() != 0 || CollectionUtils.isEmpty(physicalHostListResponse.getData())) {
            log.error("获取供应商token错误!");
            return;
        }

        handleSaveMonitor(physicalHostListResponse.getData());
    }

    private void handleSaveMonitor(List<PhysicalHostVO> physicalHostVOS) {
        List<PhysicalHostVO> list = new ArrayList<>();
        log.info("获取宿主机信息：" + DateUtil.getNowStr());
        log.info("获取宿主机信息数量：" + physicalHostVOS.size());
        log.info("获取宿主机信息：" + physicalHostVOS.toString());
        //根据宿主机中的集群key,匹配到就证明有效
        for (PhysicalHostVO physicalHostVO : physicalHostVOS) {
            IaasCluster iaasCluster = sysClusterMapper.queryIaasClusterByKey(physicalHostVO.getAzoneName());
            if (iaasCluster == null) {
                continue;
            }
            list.add(physicalHostVO);
        }

        log.info("匹配宿主机信息数量：" + physicalHostVOS.size());
        log.info("匹配宿主机信息：" + physicalHostVOS.toString());
        //和集群匹配到的数据
        for (PhysicalHostVO physicalHostVO : list) {
            if (StringUtils.isBlank(physicalHostVO.getAzoneName())) {
                continue;
            }

            IaasCluster iaasCluster = sysClusterMapper.queryIaasClusterByKey(physicalHostVO.getAzoneName());
            if (iaasCluster == null) {
                continue;
            }
            Long providerId = iaasProviderMapper.queryProviderIdByClusterId(iaasCluster.getId());

            IaasHost iaasHost = sysHostMapper.queryIaasHostByKey(physicalHostVO.getId());

            if (iaasHost == null) {
                continue;
            }
            //zyx写的
//            List<IaasVirtualMachine> virtualMachines = iaasVirtualMachineMapper.queryVMByHostId(iaasHost.getId());
//            log.info("psi vms");
//            if (CollectionUtils.isNotEmpty(virtualMachines)) {
//                for (IaasVirtualMachine ivm : virtualMachines) {
//                    if (null != ivm) {
            saveMonitor(providerId, null, iaasHost.getId(), iaasCluster.getId(), physicalHostVO);
//                    }
//                }

//            }


        }
    }

    private void saveMonitor(Long providerId, Long vmId, Long hostId, Long clusterId, PhysicalHostVO vo) {
        IaasVirtualMachineMonitor monitor = IaasVirtualMachineMonitor.builder()
                .id(SnowFlakeIdGenerator.getInstance().nextId())
                .clusterId(clusterId)
//                .vmId(vmId)
                .hostId(hostId)
                .providerId(providerId)
                .type(3L)
                .vcpuTotal(StringUtils.isBlank(vo.getTotalVcpuCores()) ? 0 : Double.valueOf(vo.getTotalVcpuCores()))
                .vcpuUsed(StringUtils.isBlank(vo.getUsedCpu()) ? 0 : Double.valueOf(vo.getUsedCpu()))
                .memoryTotal(StringUtils.isBlank(vo.getTotalVmemoryMB()) ? 0 : Double.valueOf(vo.getTotalVmemoryMB()))
                .memoryUsed(StringUtils.isBlank(vo.getAllocatedVmemoryMB()) ? 0 : Double.valueOf(vo.getAllocatedVmemoryMB()))
                .diskTotal(StringUtils.isBlank(vo.getTotalDisk()) ? 0 : Double.valueOf(vo.getTotalDisk()))
                .diskUsed(StringUtils.isBlank(vo.getUsedDisk()) ? 0 : Double.valueOf(vo.getUsedDisk()))
                .cpuUsageRate(MathUtils.divDouble(vo.getUsedCpu(), vo.getTotalVcpuCores(), 4) * 100)
                .memoryUsageRate(MathUtils.divDouble(vo.getAllocatedVmemoryMB(), vo.getTotalVmemoryMB(), 4) * 100)
                .createTime(DateUtil.getNow())
//                .createUser(Sign.getUserId())
                .isDeleted(false)
                .build();

        iaasVirtualMachineMonitorMapper.insert(monitor);
        log.info("保存虚拟机监控信息成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse syncClusterResource(VMRequest vmRequest) {
        String authz = AuthContext.getAuthz();
        BaseResponse baseResponse = new BaseResponse();

        //获取运维面token
        TokenInfoResponse tokenResponse = getToken(authz, vmRequest.getUsername(), vmRequest.getPassword());

//        if (tokenResponse == null || tokenResponse.getCode() != 0 || tokenResponse.getData() == null || StringUtils.isBlank(tokenResponse.getData().getOcToken())) {
//            baseResponse.setCode(201);
//            baseResponse.setMessage("获取供应商token错误!");
//            return baseResponse;
//        }

        AvailbleZoneReqVO zoneReqVO = new AvailbleZoneReqVO();
        zoneReqVO.setOmToken(null);
        zoneReqVO.setAzoneId(vmRequest.getClusterKey());

        AvailableZoneResourceResponse zoneResourceResponse = null;
        try {
            zoneResourceResponse = availableZoneClient.getAvailableZoneResource(authz, zoneReqVO);
        } catch (Exception e) {
            log.error("获取集群资源信息错误！", e);
        }

        if (zoneResourceResponse == null || zoneResourceResponse.getCode() != 0 || zoneResourceResponse.getData() == null) {
            baseResponse.setCode(201);
            baseResponse.setMessage("获取集群资源信息错误!");
            return baseResponse;
        }

        //处理集群下的资源信息
        handleClusterRecourse(vmRequest.getClusterKey(), zoneResourceResponse.getData());

        return baseResponse;
    }

    private void handleVMImage(Long vdcId, List<ImagesVO> list) {
        for (ImagesVO imageVo : list) {
            saveImageService(vdcId, imageVo);
        }
    }

    private void saveImageService(Long vdcId, ImagesVO imageVo) {
        IaasImageService queryImage = iaasImageServiceMapper.queryImageByKey(imageVo.getId());

        if (queryImage == null) {
            IaasImageService save = IaasImageService.builder()
                    .id(SnowFlakeIdGenerator.getInstance().nextId())
                    .imageKey(imageVo.getId())
                    .name(imageVo.getName())
                    .vdcId(vdcId)
                    .visibility(imageVo.getVisibility())
                    .containerFormat(imageVo.getContainerFormat())
                    .diskFormat(imageVo.getDiskFormat())
                    .platform(imageVo.getPlatform())
                    .minDisk(imageVo.getMinDisk())
                    .minRam(imageVo.getMinRam())
                    .osBit(imageVo.getOsBit())
                    .osType(imageVo.getOsType())
                    .status(imageVo.getStatus())
                    .tag(imageVo.getTag())
                    .createTime(DateUtil.getNow())
                    .createUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .startMode(imageVo.getHwFirmwareType())
                    .diskDeviceType(imageVo.getHwFirmwareType())
                    .cpuFramework(imageVo.getArchitecture())
                    .systemDiskSize(StringUtils.isBlank(imageVo.getVirtualSize()) ? 0 : Double.valueOf(imageVo.getVirtualSize()))
                    .osVersion(imageVo.getOsVersion())
                    .createdAt(imageVo.getCreatedAt())
                    .isDeleted(false)
                    .build();
            iaasImageServiceMapper.insert(save);
            log.info("保存虚拟机镜像列表成功！");
        } else {
            queryImage.setImageKey(imageVo.getId());
            queryImage.setName(imageVo.getName());
            queryImage.setVdcId(vdcId);
            queryImage.setPlatform(imageVo.getPlatform());
            queryImage.setVisibility(imageVo.getVisibility());
            queryImage.setContainerFormat(imageVo.getContainerFormat());
            queryImage.setDiskFormat(imageVo.getDiskFormat());
            queryImage.setMinDisk(imageVo.getMinDisk());
            queryImage.setMinRam(imageVo.getMinRam());
            queryImage.setOsBit(imageVo.getOsBit());
            queryImage.setOsType(imageVo.getOsType());
            queryImage.setStatus(imageVo.getStatus());
            queryImage.setTag(imageVo.getTag());
            queryImage.setUpdateTime(DateUtil.getNow());
            queryImage.setUpdateUser(Sign.getUserId());
            queryImage.setStartMode(imageVo.getHwFirmwareType());
            queryImage.setDiskDeviceType(imageVo.getHwFirmwareType());
            queryImage.setCpuFramework(imageVo.getArchitecture());
            queryImage.setSystemDiskSize(StringUtils.isBlank(imageVo.getVirtualSize()) ? 0 : Double.valueOf(imageVo.getVirtualSize()));
            queryImage.setOsVersion(imageVo.getOsVersion());
            queryImage.setCreatedAt(imageVo.getCreatedAt());

            iaasImageServiceMapper.updateById(queryImage);
            log.info("更新虚拟机镜像列表成功！");
        }

    }

    @Override
    public String verificationUsernameAndPwd(IaasProviderVO iaasProviderVO, int type) {
        String authz = AuthContext.getAuthz();
        String token = "";
        try {
            if (2 == type) {//校验供应商用户名密码
                //获取token
//                TokenInfoResponse tokenResponse = getToken(authz, iaasProviderVO.getUsername(), iaasProviderVO.getPassword());
//                if (tokenResponse == null || tokenResponse.getCode() != 0 || tokenResponse.getData() == null || StringUtils.isBlank(tokenResponse.getData().getOcToken())) {
//                    return token;
//                }

                //2020-01-05 验证华为用户名/密码 获取token
                TokenInfoResponse tokenResponse = getToken(authz, iaasProviderVO);
                if (tokenResponse == null || tokenResponse.getCode() != 0 || tokenResponse.getData() == null || StringUtils.isBlank(tokenResponse.getData().getOcToken())) {
                    return token;
                }

                TokenInfoVO data = tokenResponse.getData();
                String moToken = data.getOcToken();
                if (StringUtils.isNotBlank(moToken)) {
                    return token = moToken;
                }
            } else if (3 == type) {//校验vdc用户名密码
                StringResponse stringResponse = oauthTokenClient.CheckVdcUser(authz, iaasProviderVO.getDomainName(), iaasProviderVO.getUsername(), iaasProviderVO.getPassword());

                if (stringResponse == null || stringResponse.getCode() != 0 || StringUtils.isBlank(stringResponse.getData())) {
                    return token;
                }

                return stringResponse.getData();
            }
        } catch (Exception e) {

        }

        return token;
    }

    @Override
    public void autoSyncVMToMonitor(String username, String password, String projectKey, Long providerId) {
        String authz = AuthContext.getAuthz();

        //1.通过用户名密码获取供应商token
//        TokenInfoResponse tokenResponse = getToken(authz, username, password);

//        if (tokenResponse == null) {
//            log.error("获取供应商token错误!");
//            return;
//        }

//        TokenInfoVO tokenInfoVO = tokenResponse.getData();
        RequestVO requestVO = new RequestVO();
        requestVO.setOmToken(null);//omToken
        requestVO.setProjectId(projectKey);

        //获取虚拟机信息
        VDCVirtualMachineListResponse vmResponse = null;
        try {
            vmResponse = vdcVirtualMachineClient.getVirtualMachineList(authz, requestVO);
        } catch (Exception e) {
            log.error("获取项目下的虚拟机信息异常！", e);
        }

        if (vmResponse == null || vmResponse.getCode() != 0 || CollectionUtils.isEmpty(vmResponse.getData())) {
            log.warn("获取虚拟机信息错误!");
            return;
        }
        //处理虚拟机和虚拟机磁盘信息
        handleTaskVMInfo(providerId, vmResponse.getData());
    }

    @Override
    public ProviderCascade queryProviderCascadeByProvider(Long providerId) {
        return iaasProviderMapper.queryProviderCascadeByProvider(providerId);
    }

    private void handleTaskVMInfo(Long providerId, List<VirtualMachineVO> vmList) {
        for (VirtualMachineVO vmVO : vmList) {
            IaasVirtualMachine queryVirtualMachine = iaasVirtualMachineMapper.queryIaasVirtualMachineByKey(vmVO.getNativeId());

            //获取集群id
            IaasCluster iaasCluster = sysClusterMapper.queryIaasClusterByKey(vmVO.getAzoneName());

            //获取主机id
            IaasHost iaasHost = sysHostMapper.queryIaasHostByKey(vmVO.getPhysicalHostId());

            //保存或更新虚拟机信息
            saveVMToMonitor(providerId, queryVirtualMachine.getId(), iaasCluster.getId(), iaasHost.getId(), vmVO);
        }
    }

    private void saveVMToMonitor(long providerId, long vmId, long clusterId, long hostId, VirtualMachineVO vo) {
        String flavor = vo.getFlavor();//4U16G|4vCPU|16GB
        String cpu = "";
        String memory = "";
        if (StringUtils.isNoneBlank(flavor) && flavor.split("|") != null) {
            String split = flavor.split("\\|")[0];
            cpu = split.substring(0, split.indexOf("U"));
            memory = split.substring(split.indexOf("U") + 1, split.indexOf("G"));
        }

        double cpuUsage = StringUtils.isBlank(cpu) ? 0 : (double) (((Integer.valueOf(cpu) * vo.getCpuUsage()) / 100));
        double memoryUsage = (double) (((Integer.valueOf(memory) * vo.getMemoryUsage()) / 100));

        IaasVirtualMachineMonitor monitor = IaasVirtualMachineMonitor.builder()
                .id(SnowFlakeIdGenerator.getInstance().nextId())
                .vmId(vmId)
                .clusterKey(vo.getAzoneName())
                .clusterId(clusterId)
                .hostId(hostId)
                .providerId(providerId)
                .type(1L)
                .cpuUsageRate((double) vo.getCpuUsage())
                .vcpuTotal(StringUtils.isBlank(cpu) ? 0 : Integer.valueOf(cpu))
                .vcpuUsed(cpuUsage)
                .memoryTotal(StringUtils.isBlank(memory) ? 0 : Integer.valueOf(memory))
                .memoryUsed(memoryUsage)
                .memoryUsageRate(MathUtils.divDouble(String.valueOf(memoryUsage), String.valueOf(memory), 4) * 100)
                .createTime(DateUtil.getNow())
//                .createUser(Sign.getUserId())
                .isDeleted(false)
                .build();

        iaasVirtualMachineMonitorMapper.insert(monitor);
        log.info("保存虚拟机监控信息成功！");
    }

    private void handleClusterRecourse(String clusterKey, AvailableZoneResource data) {
        IaasCluster iaasCluster = sysClusterMapper.queryIaasClusterByKey(clusterKey);
        //更新集群表存
        if (iaasCluster == null) {
            log.warn("获取集群信息为空！clusterKey=", clusterKey);
            return;
        }

        updateClusterInfo(iaasCluster, data);

        Long providerId = iaasProviderMapper.queryProviderIdByClusterId(iaasCluster.getId());
        if (providerId == null) {
            log.warn("获取供应商id为空！clusterId=", iaasCluster.getId());
            return;
        }

        // iaas虚拟机监控表 集群类型
        saveMachineMonitor(providerId, iaasCluster == null ? null : iaasCluster.getId(), clusterKey, data);
    }

    private void updateClusterInfo(IaasCluster iaasCluster, AvailableZoneResource data) {
        iaasCluster.setCpuTotal(StringUtils.isBlank(data.cpuTotal()) ? 0 : Double.valueOf(data.cpuTotal()));
        iaasCluster.setCpuUsed(StringUtils.isBlank(data.cpuUsed()) ? 0 : Double.valueOf(data.cpuUsed()));
        iaasCluster.setMemoryTotal(StringUtils.isBlank(data.memoryTotal()) ? 0 : Double.valueOf(data.memoryTotal()));
        iaasCluster.setMemoryUsed(StringUtils.isBlank(data.memoryUsed()) ? 0 : Double.valueOf(data.memoryUsed()));
        iaasCluster.setDiskTotal(data.diskTotal());
        iaasCluster.setDiskUsed(data.diskUsed());
        iaasCluster.setVmNum(data.vmNum());
        iaasCluster.setUpdateTime(DateUtil.getNow());

        sysClusterMapper.updateById(iaasCluster);

        log.info("更新集群信息成功！");
    }

    private void saveMachineMonitor(Long providerId, Long clusterId, String clusterKey, AvailableZoneResource data) {
        double cpuTotal = StringUtils.isBlank(data.cpuTotal()) ? 0 : Double.valueOf(data.cpuTotal());
        double cpuUsed = StringUtils.isBlank(data.cpuUsed()) ? 0 : Double.valueOf(data.cpuUsed());
        double memoryTotal = StringUtils.isBlank(data.memoryTotal()) ? 0 : Double.valueOf(data.memoryTotal());
        double memoryUsed = StringUtils.isBlank(data.memoryUsed()) ? 0 : Double.valueOf(data.memoryUsed());

        IaasVirtualMachineMonitor monitor = IaasVirtualMachineMonitor.builder()
                .id(SnowFlakeIdGenerator.getInstance().nextId())
                .clusterId(clusterId)
                .clusterKey(clusterKey)
                .providerId(providerId)
                .type(2L)
                .vcpuTotal(cpuTotal)
                .vcpuUsed(cpuUsed)
                .memoryTotal(memoryTotal)
                .memoryUsed(memoryUsed)
                .cpuUsageRate(MathUtils.divDouble(data.cpuUsed(), data.cpuTotal(), 4) * 100)
                .memoryUsageRate(MathUtils.divDouble(data.memoryUsed(), data.memoryTotal(), 4) * 100)
                .diskTotal(data.diskTotal())
                .diskUsed(data.diskUsed())
                .createTime(DateUtil.getNow())
//                .createUser(Sign.getUserId())
                .isDeleted(false)
                .build();

        iaasVirtualMachineMonitorMapper.insert(monitor);
        log.info("保存虚拟机监控信息成功！");
    }

    private void handlePhysicalHostInfo(List<PhysicalHostVO> physicalHostVOS) {

        for (PhysicalHostVO physicalHostVO : physicalHostVOS) {
            if (StringUtils.isBlank(physicalHostVO.getAzoneName())) {
                continue;
            }

            IaasCluster iaasCluster = sysClusterMapper.queryIaasClusterByKey(physicalHostVO.getAzoneName());
            if (iaasCluster == null) {
                continue;
            }

            savePhysicalHost(iaasCluster.getId(), physicalHostVO);
        }
    }

    private void savePhysicalHost(Long clusterId, PhysicalHostVO physicalHostVO) {
        IaasHost queryHost = sysHostMapper.queryIaasHostByKey(physicalHostVO.getId());
        if (queryHost == null) {
            IaasHost saveHost = IaasHost.builder()
                    .id(SnowFlakeIdGenerator.getInstance().nextId())
                    .clusterId(clusterId)
                    .hostName(physicalHostVO.getName())
                    .hostKey(physicalHostVO.getId())
                    .type(1)
                    .resId(physicalHostVO.getResId())
                    .ipAddress(physicalHostVO.getIpAddress())
                    .state(physicalHostVO.getStatus())
                    .cpuTotal(StringUtils.isBlank(physicalHostVO.getTotalVcpuCores()) ? 0 : Integer.valueOf(physicalHostVO.getTotalVcpuCores()))
                    .cpuUsed(StringUtils.isBlank(physicalHostVO.getUsedCpu()) ? 0 : Integer.valueOf(physicalHostVO.getUsedCpu()))
                    .memoryTotal(StringUtils.isBlank(physicalHostVO.getTotalVmemoryMB()) ? 0 : Integer.valueOf(physicalHostVO.getTotalVmemoryMB()))
                    .memoryUsed(StringUtils.isBlank(physicalHostVO.getAllocatedVmemoryMB()) ? 0 : Integer.valueOf(physicalHostVO.getAllocatedVmemoryMB()))
                    .diskTotal(StringUtils.isBlank(physicalHostVO.getTotalDisk()) ? 0 : Integer.valueOf(physicalHostVO.getTotalDisk()))
                    .diskUsed(StringUtils.isBlank(physicalHostVO.getUsedDisk()) ? 0 : Integer.valueOf(physicalHostVO.getUsedDisk()))
                    .vcpu(StringUtils.isBlank(physicalHostVO.getTotalCpu()) ? 0 : Integer.valueOf(physicalHostVO.getTotalCpu()))
                    .dataCenter(physicalHostVO.getLogicalRegionName())
                    .createTime(DateUtil.getNow())
                    .createUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .isDeleted(false)
                    .build();

            sysHostMapper.insert(saveHost);

            log.info("保存主机信息成功！");
        } else {
            queryHost.setClusterId(clusterId);
            queryHost.setHostName(physicalHostVO.getName());
            queryHost.setHostKey(physicalHostVO.getId());
            queryHost.setType(1);
            queryHost.setIpAddress(physicalHostVO.getIpAddress());
            queryHost.setState(physicalHostVO.getStatus());
            queryHost.setCpuTotal(StringUtils.isBlank(physicalHostVO.getTotalVcpuCores()) ? 0 : Integer.valueOf(physicalHostVO.getTotalVcpuCores()));
            queryHost.setCpuUsed(StringUtils.isBlank(physicalHostVO.getUsedCpu()) ? 0 : Integer.valueOf(physicalHostVO.getUsedCpu()));
            queryHost.setMemoryTotal(StringUtils.isBlank(physicalHostVO.getTotalVmemoryMB()) ? 0 : Integer.valueOf(physicalHostVO.getTotalVmemoryMB()));
            queryHost.setMemoryUsed(StringUtils.isBlank(physicalHostVO.getAllocatedVmemoryMB()) ? 0 : Integer.valueOf(physicalHostVO.getAllocatedVmemoryMB()));
            queryHost.setVcpu(StringUtils.isBlank(physicalHostVO.getTotalCpu()) ? 0 : Integer.valueOf(physicalHostVO.getTotalCpu()));
            queryHost.setDiskTotal(StringUtils.isBlank(physicalHostVO.getTotalDisk()) ? 0 : Integer.valueOf(physicalHostVO.getTotalDisk()));
            queryHost.setDiskUsed(StringUtils.isBlank(physicalHostVO.getUsedDisk()) ? 0 : Integer.valueOf(physicalHostVO.getUsedDisk()));
            queryHost.setDataCenter(physicalHostVO.getLogicalRegionName());
            queryHost.setUpdateTime(DateUtil.getNow());
            queryHost.setUpdateUser(Sign.getUserId());

            sysHostMapper.updateById(queryHost);
            log.info("更新主机信息成功！");
        }
    }

    public void handleVMInfo(Long providerId, Long projectId, Long businessId, String[] businessIds, Long poolId, List<VirtualMachineVO> vmList, HostVolumesVOListResponse diskResponse) {
        List<HostVolumesVO> diskList = new ArrayList<>();
        if (diskResponse != null && diskResponse.getCode() == 0 && CollectionUtils.isNotEmpty(diskResponse.getData())) {
            diskList.addAll(diskResponse.getData());
        }

        for (VirtualMachineVO vmVO : vmList) {
            if (vmVO == null) {
                continue;
            }
            IaasVirtualMachine queryVirtualMachine = iaasVirtualMachineMapper.queryIaasVirtualMachineByKey(vmVO.getNativeId());

            long vmId = queryVirtualMachine == null ? SnowFlakeIdGenerator.getInstance().nextId() : queryVirtualMachine.getId();

            //保存或更新虚拟机信息
            saveVM(providerId, projectId, vmId, businessId, businessIds, poolId, vmVO, queryVirtualMachine);

            for (HostVolumesVO diskVO : diskList) {
                if (vmVO.getNativeId().equals(diskVO.serverId())) {
                    //保存或更新虚拟机磁盘信息
                    saveVMDisk(vmId, diskVO);
                }
            }
        }

    }

    private void saveVMDisk(long vmId, HostVolumesVO diskVO) {
        IaasVirtualMachineDisk queryDisk = iaasVirtualMachineDiskMapper.queryVMDiskBy(diskVO.getId());
        if (queryDisk == null) {
            IaasVirtualMachineDisk saveDisk = IaasVirtualMachineDisk.builder()
                    .id(SnowFlakeIdGenerator.getInstance().nextId())
                    .vmId(vmId)
                    .diskName(diskVO.getDisplayName())
                    .diskType(diskVO.getVolumeType())
                    .diskTotal(StringUtils.isBlank(diskVO.getSize()) ? 0 : Double.valueOf(diskVO.getSize()))
//                    .diskUsed()
                    .status(diskVO.getStatus())
                    .remark(diskVO.getDisplayDescription())
                    .diskKey(diskVO.getId())
                    .createTime(DateUtil.getNow())
                    .createUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .isDeleted(false)
                    .build();

            iaasVirtualMachineDiskMapper.insert(saveDisk);
            log.info("保存虚拟磁盘机信息成功！");
        } else {
            queryDisk.setDiskName(diskVO.getDisplayName());
            queryDisk.setDiskType(diskVO.getVolumeType());
            queryDisk.setDiskTotal(StringUtils.isBlank(diskVO.getSize()) ? 0 : Double.valueOf(diskVO.getSize()));
            queryDisk.setStatus(diskVO.getStatus());
            queryDisk.setRemark(diskVO.getDisplayDescription());
            queryDisk.setDiskKey(diskVO.getId());
            queryDisk.setUpdateTime(DateUtil.getNow());
            queryDisk.setUpdateUser(Sign.getUserId());

            iaasVirtualMachineDiskMapper.updateById(queryDisk);
            log.info("更新虚拟磁盘机信息成功！");
        }
    }

    public void saveVM(Long providerId, Long projectId, long vmId, Long businessId, String[] businessIds, Long poolId, VirtualMachineVO vmVO, IaasVirtualMachine queryVirtualMachine) {
        String flavor = vmVO.getFlavor();//4U16G|4vCPU|16GB
        String cpu = "";
        String memory = "";
        if (StringUtils.isNoneBlank(flavor) && flavor.split("|") != null) {
            String split = flavor.split("\\|")[0];
            cpu = split.substring(0, split.indexOf("U"));
            memory = split.substring(split.indexOf("U") + 1, split.indexOf("G"));
        }

        double cpuUsage = StringUtils.isBlank(cpu) ? 0 : (double) (((Integer.valueOf(cpu) * vmVO.getCpuUsage()) / 100));
        double memoryUsage = (double) (((Integer.valueOf(memory) * vmVO.getMemoryUsage()) / 100));

        //获取集群id
        IaasCluster iaasCluster = sysClusterMapper.queryIaasClusterByKey(vmVO.getAzoneName());

        //获取主机id
        IaasHost iaasHost = sysHostMapper.queryIaasHostByKey(vmVO.getPhysicalHostId());

        if (queryVirtualMachine == null) {
            IaasVirtualMachine saveVM = IaasVirtualMachine.builder()
                    .id(vmId)
                    .imageName(vmVO.getImageName())
                    .vmName(vmVO.getName() == null ? "" : ((String) vmVO.getName().get("value")))
                    .vmKey(vmVO.getNativeId())
                    .clusterId(iaasCluster == null ? null : iaasCluster.getId())
                    .hostId(iaasHost == null ? null : iaasHost.getId())
                    .resId(vmVO.resId())
                    .osName(vmVO.getOsVersion())
//                    .businessGroupId(businessId)
                    .poolId(poolId)
                    .providerId(providerId)
                    .vcpuTotal(StringUtils.isBlank(cpu) ? 0 : Integer.valueOf(cpu))
                    .vcpuUsed(cpuUsage)
                    .diskUsage(vmVO.getDiskUsage())
                    .memoryTotal(StringUtils.isBlank(memory) ? 0 : Integer.valueOf(memory))
                    .memoryUsed(memoryUsage)
                    .state(vmVO.getStatus())
                    .ipAddress(vmVO.getPrivateIps())
                    .projectId(projectId)
                    .createTime(DateUtil.getNow())
                    .createUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .isDeleted(false)
                    .azoneId(vmVO.getAzoneId())
                    .azoneName(vmVO.getAzoneName())
                    .build();

            iaasVirtualMachineMapper.insert(saveVM);
            log.info("保存虚拟机信息成功！");

            //保存虚拟机和业务组关联关系
            saveVMRight(vmId, businessIds);
        } else {
            queryVirtualMachine.setVmKey(vmVO.getNativeId());
            queryVirtualMachine.setImageName(vmVO.getImageName());
            queryVirtualMachine.setVmName(vmVO.getName() == null ? "" : ((String) vmVO.getName().get("value")));
            queryVirtualMachine.setClusterId(iaasCluster == null ? null : iaasCluster.getId());
            queryVirtualMachine.setHostId(iaasHost == null ? null : iaasHost.getId());
            queryVirtualMachine.setResId(vmVO.resId());
            queryVirtualMachine.setOsName(vmVO.getOsVersion());
//            queryVirtualMachine.setBusinessGroupId(businessId);
            queryVirtualMachine.setPoolId(poolId);
            queryVirtualMachine.setProviderId(providerId);
            queryVirtualMachine.setProjectId(projectId);
            queryVirtualMachine.setDiskUsage(vmVO.getDiskUsage());
            queryVirtualMachine.setVcpuTotal(StringUtils.isBlank(cpu) ? 0 : Integer.valueOf(cpu));
            queryVirtualMachine.setVcpuUsed(cpuUsage);
            queryVirtualMachine.setMemoryTotal(StringUtils.isBlank(memory) ? 0 : Integer.valueOf(memory));
            queryVirtualMachine.setMemoryUsed(memoryUsage);
            queryVirtualMachine.setState(vmVO.getStatus());
            queryVirtualMachine.setIpAddress(vmVO.getPrivateIps());
            queryVirtualMachine.setUpdateTime(DateUtil.getNow());
            queryVirtualMachine.setUpdateUser(Sign.getUserId());
            queryVirtualMachine.setAzoneId(vmVO.getAzoneId());
            queryVirtualMachine.setAzoneName(vmVO.getAzoneName());

            iaasVirtualMachineMapper.updateById(queryVirtualMachine);
            log.info("更新虚拟机信息成功！");
        }
    }

    private void saveVMRight(long vmId, String[] businessIds) {
        if (businessIds != null && businessIds.length > 0) {
            for (int i = 0; i < businessIds.length; i++) {
                if (StringUtils.isNotBlank(businessIds[i])) {
                    IaasVirtualMachineRight build = IaasVirtualMachineRight.builder()
                            .id(SnowFlakeIdGenerator.getInstance().nextId())
                            .vmId(vmId)
                            .type(5)
                            .relateId(Long.valueOf(businessIds[i]))
                            .createTime(DateUtil.getNow())
                            .createUser(Sign.getUserId())
                            .build();

                    iaasVirtualMachineRightMapper.insert(build);
                    log.info("保存业务组与虚拟机之间的关系成功！");
                }

            }

        }
    }

    /**
     * 新版获取token 参数前端传入
     *
     * @param authz
     * @param providerVO
     * @return
     */
    public TokenInfoResponse getToken(String authz, IaasProviderVO providerVO) {
        TokenInfoResponse tokenResponse = null;
//        try {
//            tokenResponse = oauthTokenClient.getTokenAll(authz, providerVO);
//
//            if (tokenResponse.getCode() != 0 || tokenResponse.getData() == null) {
//                return null;
//            }
//        } catch (Exception e) {
//            log.error("获取供应商token错误!", e);
//            return null;
//        }

        return tokenResponse;
    }

    private TokenInfoResponse getToken(String authz, String userName, String password) {
        TokenInfoResponse tokenResponse = null;
//        try {
//            tokenResponse = oauthTokenClient.getTokenByUser(authz,
//                    "",
//                    "domainName",
//                    userName,
//                    password);
//
//            if (tokenResponse.getCode() != 0 || tokenResponse.getData() == null) {
//                return null;
//            }
//        } catch (Exception e) {
//            log.error("获取供应商token错误!", e);
//            return null;
//        }

        return tokenResponse;
    }

    private void handleVDCProjects(String moToken, Long vdcId, VDCRequest request, List<ProjectsVO> projectsVOList) {
        IaasVirtualDataCenter dataCenter = new IaasVirtualDataCenter();
        dataCenter.setId(vdcId);
        dataCenter.setUsername(request.getUsername());
        dataCenter.setPassword(request.getPassword());
        dataCenter.setUpdateUser(Sign.getUserId());
        dataCenter.setUpdateTime(DateUtil.getNow());

        iaasVirtualDataCenterMapper.updateById(dataCenter);

        for (ProjectsVO vo : projectsVOList) {
            saveProject(moToken, vdcId, vo);
        }
    }

    private void saveProject(String moToken, Long vdcId, ProjectsVO vo) {
        IaasProject queryProject = iaasProjectMapper.queryIaasProjectByKey(vo.getId());
        if (queryProject == null) {
            IaasProject project = IaasProject.builder()
                    .id(SnowFlakeIdGenerator.getInstance().nextId())
                    .projectKey(vo.getId())
                    .projectName(vo.getName())
                    .remark(vo.getDescription())
                    .vdcId(vdcId)
                    .token(moToken)
                    .tokenTime(DateUtil.getNow())
                    .createUser(Sign.getUserId())
                    .createTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .isDeleted(false)
                    .build();

            iaasProjectMapper.insert(project);
            log.info("保存项目成功！projectKey=", vo.getId());
        } else {
            queryProject.setProjectKey(vo.getId());
            queryProject.setProjectName(vo.getName());
            queryProject.setRemark(vo.getDescription());
            queryProject.setVdcId(vdcId);
            queryProject.setToken(moToken);
            queryProject.setTokenTime(DateUtil.getNow());
            queryProject.setUpdateUser(Sign.getUserId());
            queryProject.setUpdateTime(DateUtil.getNow());

            iaasProjectMapper.updateById(queryProject);
            log.info("更新项目成功！", queryProject.getId());
        }
    }

    //处理华为返还的区域集群信息
    private void handleAreaAndClusterInfo(Long providerId, RegionEntity data) {
        if (StringUtils.isBlank(data.getId())) {
            log.error("区域key(id)为空!");
            return;
        }

        //处理区域信息
        IaasArea queryIaasArea = sysAreaMapper.queryIaasAreaByKey(data.getId());

        long sysAreaId = queryIaasArea == null ? SnowFlakeIdGenerator.getInstance().nextId() : queryIaasArea.getId();

        //保存区域信息
        saveArea(sysAreaId, providerId, data.getName(), data.getId(), queryIaasArea);

        //处理集群信息
        List<AvailableZone> availableZones = data.availableZones();

        if (CollectionUtils.isNotEmpty(availableZones)) {
            for (AvailableZone zone : availableZones) {
                IaasCluster queryIaasCluster = sysClusterMapper.queryIaasClusterByKey(zone.getAz_id());
                //保存集群信息
                saveHuaWeiCluster(sysAreaId, zone, queryIaasCluster);
            }
        }
    }

    private void saveHuaWeiCluster(long sysAreaId, AvailableZone zone, IaasCluster queryIaasCluster) {
        if (queryIaasCluster == null) {
            IaasCluster sysCluster = IaasCluster.builder()
                    .id(SnowFlakeIdGenerator.getInstance().nextId())
                    .areaId(sysAreaId)
                    .clusterName(zone.getName())
                    .remark(zone.getDescription())
                    .clusterKey(zone.getAz_id())
                    .createUser(Sign.getUserId())
                    .createTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .isDeleted(false)
                    .build();

            sysClusterMapper.insert(sysCluster);

            log.info("save sys cluster is successful");
        } else {
            queryIaasCluster.setClusterKey(zone.getAz_id());
            queryIaasCluster.setClusterName(zone.getDescription());
            queryIaasCluster.setRemark(zone.getName());
            queryIaasCluster.setUpdateTime(DateUtil.getNow());
            queryIaasCluster.setUpdateUser(Sign.getUserId());
            sysClusterMapper.updateById(queryIaasCluster);
            log.info("update sys cluster is successful", queryIaasCluster.getId());
        }
    }

    private void handleVDCInfo(Long providerId, List<VdcsVO> data) {
        for (VdcsVO vo : data) {
            saveVdc(providerId, vo);
        }
    }

    private void saveVdc(Long providerId, VdcsVO vo) {
        IaasVirtualDataCenter queryIaasVirtualDataCenter = iaasVirtualDataCenterMapper.queryIaasVirtualDataCenterByKey(vo.getId());

        if (queryIaasVirtualDataCenter == null) {
            IaasVirtualDataCenter dataCenter = new IaasVirtualDataCenter();
            dataCenter.setId(SnowFlakeIdGenerator.getInstance().nextId());
            dataCenter.setVdcName(vo.getName());
            dataCenter.setDomainName(vo.getDomainName());
            dataCenter.setVdcKey(vo.getId());
            dataCenter.setRemark(vo.getDescription());
            dataCenter.setProviderId(providerId);
            dataCenter.setCreateUser(Sign.getUserId());
            dataCenter.setCreateTime(DateUtil.getNow());
            dataCenter.setUpdateUser(Sign.getUserId());
            dataCenter.setUpdateTime(DateUtil.getNow());
            dataCenter.setDeleted(false);

            iaasVirtualDataCenterMapper.insert(dataCenter);
            log.info("保存VDC信息成功!");
        } else {
            queryIaasVirtualDataCenter.setVdcName(vo.getName());
            queryIaasVirtualDataCenter.setVdcKey(vo.getId());
            queryIaasVirtualDataCenter.setDomainName(vo.getDomainName());
            queryIaasVirtualDataCenter.setRemark(vo.getDescription());
            queryIaasVirtualDataCenter.setUpdateUser(Sign.getUserId());
            queryIaasVirtualDataCenter.setUpdateTime(DateUtil.getNow());

            iaasVirtualDataCenterMapper.updateById(queryIaasVirtualDataCenter);
            log.info("保存VDC信息成功!");
        }
    }

    private void saveVDCSyncData(Long vdcId, IaasVirtualDataCenterVO virtualDataCenterResponse) {
        IaasVirtualDataCenter iaasVirtualDataCenter = new IaasVirtualDataCenter();
        BeanUtils.copyProperties(virtualDataCenterResponse, iaasVirtualDataCenter);

        iaasVirtualDataCenter.setId(vdcId);
        iaasVirtualDataCenter.setUpdateUser(Sign.getUserId());
        iaasVirtualDataCenter.setUpdateTime(DateUtil.getNow());

        iaasVirtualDataCenterMapper.updateById(iaasVirtualDataCenter);
        log.info("vdc信息更新成功!");

        //获取vdc下项目信息
        List<IaasProjectVO> iaasProjectVOList = virtualDataCenterResponse.getChildren();

        if (CollectionUtils.isEmpty(iaasProjectVOList)) {
            log.info("vdc下项目信息为空!");
            return;
        }

        for (IaasProjectVO projectVO : iaasProjectVOList) {
            //通过key查询，有数据update，否则add
            IaasProjectVO queryProjectVO = iaasProjectMapper.queryIaasProject(projectVO);
            if (queryProjectVO == null) {
                saveIaasProject(vdcId, projectVO);
            } else {
                updateIaasProject(projectVO, queryProjectVO);
            }
        }
    }

    private void updateIaasProject(IaasProjectVO projectVO, IaasProjectVO queryProjectVO) {
        IaasProject project = new IaasProject();
        BeanUtils.copyProperties(projectVO, project);

        project.setId(queryProjectVO.getId());
        project.setUpdateUser(Sign.getUserId());
        project.setUpdateTime(DateUtil.getNow());

        iaasProjectMapper.updateById(project);
        log.info("更新项目成功！", queryProjectVO.getId());
    }

    private void saveIaasProject(Long vdcId, IaasProjectVO projectVO) {
        IaasProject project = new IaasProject();
        BeanUtils.copyProperties(projectVO, project);
        long id = SnowFlakeIdGenerator.getInstance().nextId();

        project.setId(id);
        project.setVdcId(vdcId);
        project.setCreateUser(Sign.getUserId());
        project.setCreateTime(DateUtil.getNow());
        project.setUpdateUser(Sign.getUserId());
        project.setUpdateTime(DateUtil.getNow());
        project.setDeleted(false);

        iaasProjectMapper.insert(project);
        log.info("保存项目成功！", id);
    }

    public void saveSyncData(IaasProvider iaasProvider, VSphereResponse vSphereResponse) {

        List<AreaResponse> areaList = vSphereResponse.getAreaData();

        if (CollectionUtils.isEmpty(areaList)) {
            log.error("区域数据为空！");
            return;
        }

        //接收集群和主机下的网络，然后存储在iaas_host_network
        List<ClusterNetworkResponse> clusterNetworkResponses = new ArrayList<>();
        List<ClusterNetworkResponse> hostNetworkResponses = new ArrayList<>();

        for (AreaResponse area : areaList) {
            String areaName = area.getAreaName();
            String areaKey = area.getAreaKey();
            if (StringUtils.isBlank(areaName) && StringUtils.isBlank(areaKey)) {
                log.error("区域名称和区域关键key都为空！");
                continue;
            }

            IaasArea queryIaasArea = sysAreaMapper.queryIaasAreaByKey(areaKey);

            long sysAreaId = queryIaasArea == null ? SnowFlakeIdGenerator.getInstance().nextId() : queryIaasArea.getId();

            //保存区域信息
            saveArea(sysAreaId, iaasProvider.getId(), areaName, areaKey, queryIaasArea);

            //获取集群信息
            List<ClusterResponse> clusterList = area.getClusterList();
            if (CollectionUtils.isEmpty(areaList)) {
                log.error("集群数据为空！");
                continue;
            }

            for (ClusterResponse cluster : clusterList) {
                String clusterName = cluster.getClusterName();
                String clusterKey = cluster.getClusterKey();
                if (StringUtils.isBlank(clusterName) && StringUtils.isBlank(clusterKey)) {
                    log.error("集群名称为空！");
                    continue;
                }

                IaasCluster queryIaasCluster = sysClusterMapper.queryIaasClusterByKey(clusterKey);

                long sysClusterId = queryIaasCluster == null ? SnowFlakeIdGenerator.getInstance().nextId() : queryIaasCluster.getId();

                //保存集群信息
                saveCluster(sysAreaId, sysClusterId, clusterName, clusterKey, queryIaasCluster);

                //保存网络信息
                List<ClusterNetworkResponse> clusterNetworks = cluster.getClusterNetworkList();
                if (CollectionUtils.isNotEmpty(clusterNetworks)) {
                    for (ClusterNetworkResponse network : clusterNetworks) {
                        String name = network.getName();
                        String type = network.getType();
                        String networkKey = network.getNetworkKey();
                        network.setId(sysClusterId);
                        if (StringUtils.isBlank(areaName) && StringUtils.isBlank(areaKey) && StringUtils.isBlank(networkKey)) {
                            log.error("网络名称、网络类型和网络关键key都为空！");
                            continue;
                        }
                        //保存主机网络
                        saveNetwork(sysClusterId, networkKey, type, name);

                        clusterNetworkResponses.add(network);
                    }
                }

                //获取主机信息
                List<HostResponse> hostList = cluster.getHostList();
                if (CollectionUtils.isEmpty(hostList)) {
                    log.error("主机信息为空！");
                    continue;
                }

                for (HostResponse host : hostList) {
                    IaasHost queryIaasHost = sysHostMapper.queryIaasHostByKey(host.getHostKey());

                    long sysHostId = queryIaasHost == null ? SnowFlakeIdGenerator.getInstance().nextId() : queryIaasHost.getId();

                    //保存主机信息
                    saveHost(sysHostId, sysClusterId, host, queryIaasHost);

                    List<DatastoreResponse> datastoreList = host.getDatastoreList();
                    if (CollectionUtils.isEmpty(datastoreList)) {
                        log.error("主机存储信息为空！");
                        continue;
                    }
                    for (DatastoreResponse datastore : datastoreList) {
                        //保存主机存储
                        saveSysHostDatastore(sysHostId, datastore);
                    }

                    //获取主机下的网络,把主机id设置进去
                    List<ClusterNetworkResponse> hostNetworkList = host.getHostNetworkList();

                    if (CollectionUtils.isEmpty(hostNetworkList)) {
                        continue;
                    }

                    for (ClusterNetworkResponse hostNetwork : hostNetworkList) {
                        hostNetwork.setId(sysHostId);
                        hostNetworkResponses.add(hostNetwork);
                    }
                }

                //获取vdc信息
                List<IaasVirtualDataCenterVO> virtualDataCenterVOList = cluster.getVirtualDataCenterVOList();
                if (CollectionUtils.isEmpty(virtualDataCenterVOList)) {
                    log.error("VDC信息为空！");
                    continue;
                }

                for (IaasVirtualDataCenterVO virtualDataCenterVO : virtualDataCenterVOList) {
                    saveIaasVirtualDataCenter(sysClusterId, virtualDataCenterVO);
                }
            }
            //主机与网络关系中间表
            if (CollectionUtils.isNotEmpty(clusterNetworkResponses) && CollectionUtils.isNotEmpty(hostNetworkResponses)) {
                for (ClusterNetworkResponse clusterNewtork : clusterNetworkResponses) {
                    if (clusterNewtork != null && StringUtils.isNotBlank(clusterNewtork.getNetworkKey())) {
                        for (ClusterNetworkResponse hostNewtork : hostNetworkResponses) {
                            if (clusterNewtork.getNetworkKey().equals(hostNewtork.getNetworkKey())) {
                                saveSysHostNetwork(clusterNewtork.getId(), hostNewtork.getId());
                            }
                        }
                    }

                }
            }
        }

    }

    //保存VDC信息
    private void saveIaasVirtualDataCenter(long sysClusterId, IaasVirtualDataCenterVO virtualDataCenterVO) {
        IaasVirtualDataCenter queryIaasVirtualDataCenter = iaasVirtualDataCenterMapper.queryIaasVirtualDataCenterByKey(virtualDataCenterVO.getVdcKey());

        if (queryIaasVirtualDataCenter == null) {
            IaasVirtualDataCenter dataCenter = new IaasVirtualDataCenter();
            BeanUtils.copyProperties(virtualDataCenterVO, dataCenter);

            dataCenter.setId(SnowFlakeIdGenerator.getInstance().nextId());
            dataCenter.setClusterId(sysClusterId);
            dataCenter.setCreateUser(Sign.getUserId());
            dataCenter.setCreateTime(DateUtil.getNow());
            dataCenter.setUpdateUser(Sign.getUserId());
            dataCenter.setUpdateTime(DateUtil.getNow());
            dataCenter.setDeleted(false);

            iaasVirtualDataCenterMapper.insert(dataCenter);
            log.info("保存VDC信息成功!");
        } else {
            BeanUtils.copyProperties(virtualDataCenterVO, queryIaasVirtualDataCenter);
            queryIaasVirtualDataCenter.setUpdateUser(Sign.getUserId());
            queryIaasVirtualDataCenter.setUpdateTime(DateUtil.getNow());

            iaasVirtualDataCenterMapper.updateById(queryIaasVirtualDataCenter);
            log.info("保存VDC信息成功!");
        }

    }

    //保存网络
    private void saveNetwork(long sysClusterId, String networkKey, String type, String networkName) {
        IaasClusterNetwork clusterNetwork = iaasClusterNetworkMapper.queryIaasNetWorkByKey(networkKey);
        if (clusterNetwork == null) {
            IaasClusterNetwork build = IaasClusterNetwork.builder()
                    .id(SnowFlakeIdGenerator.getInstance().nextId())
                    .clusterId(sysClusterId)
                    .networkKey(networkKey)
                    .type(type)
                    .networkName(networkName)
                    .createUser(Sign.getUserId())
                    .createTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .isDeleted(false)
                    .build();

            iaasClusterNetworkMapper.insert(build);
            log.info("save sys cluster network is successful");
        } else {
            clusterNetwork.setType(type);
            clusterNetwork.setNetworkName(networkName);
            clusterNetwork.setNetworkKey(networkKey);
            clusterNetwork.setUpdateTime(DateUtil.getNow());
            clusterNetwork.setUpdateUser(Sign.getUserId());

            iaasClusterNetworkMapper.updateById(clusterNetwork);
            log.info("update sys cluster network is successful");
        }
    }

    public void saveSysHostDatastore(long hostId, DatastoreResponse datastore) {
        IaasHostDatastore queryHostDatastore = sysHostDatastoreMapper.queryHostDatastoreByKey(datastore.getDatastoreKey());

        if (queryHostDatastore == null) {
            IaasHostDatastore build = IaasHostDatastore.builder()
                    .id(SnowFlakeIdGenerator.getInstance().nextId())
                    .hostId(hostId)
                    .driveType(datastore.getDriveType())
                    .datastoreName(datastore.getDatastoreName())
                    .datastoreKey(datastore.getDatastoreKey())
                    .spaceTotal(datastore.getSpaceTotal())
                    .spaceUsed(datastore.getSpaceUsed())
                    .createUser(Sign.getUserId())
                    .createTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .isDeleted(false)
                    .build();

            sysHostDatastoreMapper.insert(build);
            log.info("save sys host datastore is successful");
        } else {
            BeanUtils.copyProperties(datastore, queryHostDatastore);
            queryHostDatastore.setUpdateUser(Sign.getUserId());
            queryHostDatastore.setUpdateTime(DateUtil.getNow());

            sysHostDatastoreMapper.updateById(queryHostDatastore);
            log.info("update sys host datastore is successful");
        }
    }

    public void saveSysHostNetwork(long hostId, Long networkId) {
        IaasHostNetwork queryIaasHostNetwork = sysHostNetworkMapper.queryHostNetworkByHostIdAndNetworkId(hostId, networkId);
        if (queryIaasHostNetwork == null) {
            IaasHostNetwork build = IaasHostNetwork.builder()
                    .id(SnowFlakeIdGenerator.getInstance().nextId())
                    .hostId(hostId)
                    .networkId(networkId)
                    .createUser(Sign.getUserId())
                    .createTime(DateUtil.getNow())
                    .build();

            sysHostNetworkMapper.insert(build);
            log.info("save sys host network is successful");
        }
    }

    public void saveHost(long sysHostId, long clusterId, HostResponse host, IaasHost queryIaasHost) {
        if (queryIaasHost == null) {
            IaasHost sysHost = new IaasHost();
            BeanUtils.copyProperties(host, sysHost);
            sysHost.setId(sysHostId);
            sysHost.setClusterId(clusterId);
            sysHost.setCreateTime(DateUtil.getNow());
            sysHost.setCreateUser(Sign.getUserId());
            sysHost.setUpdateUser(Sign.getUserId());
            sysHost.setUpdateTime(DateUtil.getNow());
            sysHost.setIsDeleted(false);

            sysHostMapper.insert(sysHost);
            log.info("save sys host is successful");
        } else {
            BeanUtils.copyProperties(host, queryIaasHost);
            queryIaasHost.setUpdateUser(Sign.getUserId());
            queryIaasHost.setUpdateTime(DateUtil.getNow());

            sysHostMapper.updateById(queryIaasHost);
            log.info("update sys host is successful");
        }
    }

    public void saveCluster(Long areaId, Long sysClusterId, String clusterName, String clusterKey, IaasCluster queryIaasCluster) {
        if (queryIaasCluster == null) {
            IaasCluster sysCluster = IaasCluster.builder()
                    .id(sysClusterId)
                    .areaId(areaId)
                    .clusterName(clusterName)
                    .clusterKey(clusterKey)
                    .createUser(Sign.getUserId())
                    .createTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .isDeleted(false)
                    .build();

            sysClusterMapper.insert(sysCluster);

            log.info("save sys cluster is successful", sysClusterId);
        } else {
            queryIaasCluster.setClusterKey(clusterKey);
            queryIaasCluster.setClusterName(clusterName);
            queryIaasCluster.setUpdateTime(DateUtil.getNow());
            queryIaasCluster.setUpdateUser(Sign.getUserId());
            sysClusterMapper.updateById(queryIaasCluster);
            log.info("update sys cluster is successful", queryIaasCluster.getId());
        }
    }

    public void saveArea(long sysAreaId, long iaasProviderId, String areaName, String areaKey, IaasArea queryIaasArea) {
        if (queryIaasArea == null) {
            IaasArea sysArea = IaasArea.builder()
                    .areaName(areaName)
                    .areaKey(areaKey)
                    .id(sysAreaId)
                    .providerId(iaasProviderId)
                    .createUser(Sign.getUserId())
                    .createTime(DateUtil.getNow())
                    .updateUser(Sign.getUserId())
                    .updateTime(DateUtil.getNow())
                    .isDeleted(false)
                    .build();
            sysAreaMapper.insert(sysArea);

            log.info("save sys area is successful");
        } else {
            queryIaasArea.setAreaKey(areaKey);
            queryIaasArea.setAreaName(areaName);
            queryIaasArea.setUpdateTime(DateUtil.getNow());
            queryIaasArea.setUpdateUser(Sign.getUserId());
            sysAreaMapper.updateById(queryIaasArea);
            log.info("update sys area is successful!", queryIaasArea.getId());
        }
    }
}
