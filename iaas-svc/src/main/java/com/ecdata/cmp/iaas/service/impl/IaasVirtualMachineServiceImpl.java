package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.iaas.entity.IaasVirtualMachine;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import com.ecdata.cmp.iaas.entity.dto.IaasHostDatastoreVO;
import com.ecdata.cmp.iaas.entity.dto.IaasHostVO;
import com.ecdata.cmp.iaas.entity.dto.IaasResourcePoolVO;
import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.vm.BMGroupVO;
import com.ecdata.cmp.iaas.entity.dto.vm.VMCountVO;
import com.ecdata.cmp.iaas.entity.dto.vm.VMGroupVO;
import com.ecdata.cmp.iaas.mapper.IaasClusterMapper;
import com.ecdata.cmp.iaas.mapper.IaasHostDatastoreMapper;
import com.ecdata.cmp.iaas.mapper.IaasHostMapper;
import com.ecdata.cmp.iaas.mapper.IaasProviderMapper;
import com.ecdata.cmp.iaas.mapper.IaasResourcePoolMapper;
import com.ecdata.cmp.iaas.mapper.IaasVirtualMachineMapper;
import com.ecdata.cmp.iaas.service.IaasVirtualMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/25 10:54
 * @modified By：
 */
@Slf4j
@Service
public class IaasVirtualMachineServiceImpl extends ServiceImpl<IaasVirtualMachineMapper, IaasVirtualMachine>
        implements IaasVirtualMachineService {

    @Autowired
    private IaasClusterMapper iaasClusterMapper;

    @Autowired
    private IaasHostMapper iaasHostMapper;

    @Autowired
    private IaasHostDatastoreMapper iaasHostDatastoreMapper;

    @Autowired
    private IaasResourcePoolMapper iaasResourcePoolMapper;

    @Autowired
    private IaasProviderMapper iaasProviderMapper;

    @Override
    public IPage<IaasVirtualMachineVO> selectIaasVirtualMachineAll(Page<IaasVirtualMachineVO> page, Map<String, Object> map) {
        return baseMapper.selectIaasVirtualMachineAll(page, map);
    }

    @Override
    public IaasVirtualMachineVO qrtIaasVirtualMachineInfo(String id) {
        return baseMapper.qrtIaasVirtualMachineInfo(id);
    }

    @Override
    public List<String> qrMachineOptimize(Map<String, Object> map) {
        return baseMapper.qrMachineOptimize(map);
    }

    @Override
    public IPage<IaasVirtualMachineVO> selectByIds(Page<IaasVirtualMachineVO> page, Map<String, Object> map) {
        return baseMapper.selectByIds(page, map);
    }

    @Override
    public IaasVirtualMachineVO queryMachinesByProcessApplyVmId(Long processApplyVmId) {
        IaasVirtualMachineVO machineVO = baseMapper.queryMachinesByProcessApplyVmId(processApplyVmId);
        if (machineVO == null) {
            return machineVO;
        }

        //集群id
        Long clusterId = machineVO.getClusterId();
        if (clusterId != null) {
            IaasClusterVo clusterVo = new IaasClusterVo();
            clusterVo.setId(clusterId);
            machineVO.setIaasClusterVoList(iaasClusterMapper.getInfoByClusterVO(clusterVo));
        }

        //主机id
        Long hostId = machineVO.getHostId();
        if (hostId != null) {
            IaasHostVO hostVO = new IaasHostVO();
            hostVO.setId(hostId);
            machineVO.setIaasHostVOList(iaasHostMapper.getInfoByHostVO(hostVO));
        }

        //存储id
        Long datastoreId = machineVO.getDatastoreId();
        if (datastoreId != null) {
            IaasHostDatastoreVO datastoreVO = new IaasHostDatastoreVO();
            datastoreVO.setId(datastoreId);
            machineVO.setIaasHostDatastoreVOList(iaasHostDatastoreMapper.queryHostDatastoreVO(datastoreVO));
        }

        //资源池id
        Long poolId = machineVO.getPoolId();
        if (poolId != null) {
            IaasResourcePoolVO poolVO = new IaasResourcePoolVO();
            poolVO.setId(poolId);
            machineVO.setIaasResourcePoolVOList(iaasResourcePoolMapper.queryIaasResourcePoolVO(poolVO));
        }

        //业务组id
        Long businessGroupId = machineVO.getBusinessGroupId();
        if (businessGroupId != null) {

        }

        //供应商信息
        Long providerId = machineVO.getProviderId();
        if (providerId != null) {
            machineVO.setIaasProviderVO(iaasProviderMapper.queryIaasProviderVOById(providerId));
        }

        //云硬盘

        //网卡

        //安全组

        //弹性ip
        return machineVO;
    }

    @Override
    public IPage<IaasVirtualMachineVO> getVirtualMachineVOPage(Page<IaasVirtualMachineVO> page, Map map) {
        return baseMapper.getVirtualMachineVOPage(page, map);
    }

    @Override
    public List<IaasVirtualMachine> qryVMforTemplate() {
        return baseMapper.qryVMforTemplate();
    }

    @Override
    public List<IaasVirtualMachineVO> getVirtualMachineVOPage(Map map) {
        return baseMapper.getVirtualMachineVOPage(map);
    }

    @Override
    public boolean getVmByKey(List<String> list) {
        //查询到虚拟机id
        List<IaasVirtualMachineVO> vmIds = baseMapper.getIdBykey(list);
        if (vmIds.size()>0) {
            //删除虚拟机
            for (IaasVirtualMachineVO vo : vmIds) {
//                removeById(id.getId());
                baseMapper.removeVm(vo.getId());
                //删除网络
                baseMapper.removeNetwork(vo.getId());
                //删除磁盘
                baseMapper.removeDisk(vo.getId());
            }
            return true;
        }

        return false;
    }

    @Override
    public List<VMGroupVO> VmStatisticalList() {

        //虚拟机
        List<VMGroupVO> vmGroupVOS = baseMapper.VmStatisticalList();
        //裸金属
        List<BMGroupVO> bmGroupVOS = baseMapper.BmStatisticalList();

//        for(VMGroupVO vo : vmGroupVOS){
//            List<VMCountVO> children = vo.getChildren();
//            for (VMCountVO vo2:children) {
//                vo2.setUpdateTime("");
//            }
//        }
        if(bmGroupVOS.size()>0){
            for (BMGroupVO bmGroupVO:bmGroupVOS) {
                for (VMGroupVO vmGroupVO : vmGroupVOS) {
                    if (bmGroupVO.getId().equals(vmGroupVO.getId())){

                        List<VMCountVO> children = vmGroupVO.getChildren();
                        VMCountVO countVO = new VMCountVO();
                        countVO.setRemark(bmGroupVO.getAzoneInfo());
                        countVO.setCreateTime(bmGroupVO.getCreateTime());
                        countVO.setVmCount(bmGroupVO.getBmCount());
                        countVO.setSumDeleted(bmGroupVO.getSumDeleted());
                        children.add(countVO);
                        vmGroupVO.setChildren(children);
                    }

                    for(VMGroupVO vo : vmGroupVOS){
                        List<VMCountVO> children = vo.getChildren();
                        for (VMCountVO vo2:children) {
                            if(!"0".equals(vo2.getVmCount())){
                                vo2.setUpdateTime("");
                            }else {
                                vo2.setCreateTime("");
                            }
                        }
                    }

                }
            }
        }



        return vmGroupVOS;
    }
}
