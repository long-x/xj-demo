package com.ecdata.cmp.iaas.service;

import com.ecdata.cmp.huawei.dto.vo.VirtualMachineVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyConfigInfoVO;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xuxiaojian
 * @date 2020/3/16 14:18
 */
public class CloudTest {
    public static void main(String[] args) {
        Map<String, Object> name = new HashMap<>();
        name.put("value", "xxj");
        VirtualMachineVO machineVO = new VirtualMachineVO();
        machineVO.setName(name);
        List<VirtualMachineVO> vmData = new ArrayList<>();
        vmData.add(machineVO);

        IaasApplyConfigInfoVO configInfoVO=new IaasApplyConfigInfoVO();
        configInfoVO.setServerName("xxj");
        IaasApplyConfigInfoVO configInfoVO3=new IaasApplyConfigInfoVO();
        configInfoVO3.setServerName("3");
        List<IaasApplyConfigInfoVO> vmList = new ArrayList<>();
        vmList.add(configInfoVO);
        vmList.add(configInfoVO3);

        List<String> serveNameList = vmList.stream().map(IaasApplyConfigInfoVO::getServerName).collect(Collectors.toList());

        List<VirtualMachineVO> list = vmData.stream().filter(Objects::nonNull).filter(e ->
                serveNameList.contains(Optional.ofNullable(e.getName()).map(m -> m.get("value")).orElse(""))).collect(Collectors.toList());

//        List<VirtualMachineVO> list = vmData.stream().filter(item ->
//                vmList.stream().map(e -> e.getServerName())
//                        .collect(Collectors.toList()).contains((String)item.getName().get("value")))
//                .collect(Collectors.toList());

        System.out.println(list);
    }

}
