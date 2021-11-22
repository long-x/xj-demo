package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vm.VmFlavors;
import com.ecdata.cmp.huawei.dto.vo.RequestVO;
import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;

import java.util.List;

public interface VDCVirtualMachineService {

    List<VirtualMachineVO> listVM(RequestVO requestVO) throws Exception;

    //获取单个虚拟机信息
    VirtualMachineVO VMInfo(RequestVO requestVO) throws Exception;

    //获取云服务器规格详情列表
    List<VmFlavors> VmFlavorsList(String token,String projectId) throws Exception;

}
