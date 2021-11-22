package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.iaas.entity.dto.IaasProviderVO;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import com.ecdata.cmp.iaas.entity.dto.request.VDCRequest;
import com.ecdata.cmp.iaas.entity.dto.request.VMRequest;
import com.ecdata.cmp.iaas.entity.dto.request.VSphereRequest;
import com.ecdata.cmp.iaas.entity.dto.response.provider.cascade.ProviderCascade;

import java.io.IOException;
import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-14 14:47
 */
public interface ProviderService {
    /**
     * 同步VSphere和华为数据
     *
     * @param request
     * @return
     */
    BaseResponse syncVSphereData(VSphereRequest request) throws IOException;

    /**
     * 同步华为vdc信息
     *
     * @param request
     */
    BaseResponse syncVDCData(VDCRequest request);

    /**
     * 同步华为数据
     *
     * @param request
     * @return
     */
    BaseResponse syncHuaWeiData(VSphereRequest request);

    /**
     * 更新vdc用户名密码
     *
     * @param request
     * @return
     */
    BaseResponse updateVDCInfo(VDCRequest request);

    /**
     * 同步项目下的虚拟机和虚拟机磁盘
     *
     * @param vmRequest
     * @return
     */
    BaseResponse syncProjectVM(VMRequest vmRequest);

    BaseResponse addVMByVmKeyList(ResourcePoolVO vmRequest, List<String> reomveBusiIds);
    /**
     * 同步集群下的宿主机
     *
     * @param vmRequest
     * @return
     */
    BaseResponse syncPhysicalHost(VMRequest vmRequest);

    /**
     * 同步集群下的宿主机到虚拟机监控表(定时任务)
     *
     * @return
     */
    void autoSyncPhysicalHostToMonitor(String username, String password);

    /**
     * 同步集群下的资源(定时任务)
     *
     * @param vmRequest
     * @return
     */
    BaseResponse syncClusterResource(VMRequest vmRequest);

    /**
     * 校验华为用户名密码是否正确
     *
     * @param iaasProviderVO
     * @param type
     * @return
     */
    String verificationUsernameAndPwd(IaasProviderVO iaasProviderVO, int type);

    /**
     * 自动同步虚拟机信息到虚拟机监控表
     *
     * @param username
     * @param password
     * @param projectKey
     */
    void autoSyncVMToMonitor(String username, String password, String projectKey, Long providerId);

    /**
     * 根据供应商id查出级联信息
     *
     * @param providerId
     * @return
     */
    ProviderCascade queryProviderCascadeByProvider(Long providerId);
}
