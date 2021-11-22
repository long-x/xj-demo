package com.ecdata.cmp.iaas.infrastructure;

import com.ecdata.cmp.iaas.entity.dto.process.IaasProcessApplyVirtualMachineVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述:
 * 推送创建虚拟机和组件信息到mq
 *
 * @author xxj
 * @create 2019-11-26 14:15
 */
@Component
public class PushVirtualMachinesInfo {

    public void pushVirtualMachines(List<IaasProcessApplyVirtualMachineVO> virtualMachinesVOList) {
        //虚拟机排序然后进行发送mq,
        // 拿出没有创建的虚拟机，然后发送一条过去，等发送的创建成功后，再发送下一条虚拟机或组件

    }
}
