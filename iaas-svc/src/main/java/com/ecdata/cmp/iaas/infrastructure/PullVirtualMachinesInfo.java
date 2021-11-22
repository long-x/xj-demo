package com.ecdata.cmp.iaas.infrastructure;

import org.springframework.stereotype.Component;

/**
 * 描述:
 * 拉取mq中创建虚拟机成功的信息
 *
 * @author xxj
 * @create 2019-11-26 14:15
 */
@Component
public class PullVirtualMachinesInfo {
    public void pushVirtualMachines() {
        //拉取创建虚拟机成功后返回的数据（里面包含创建虚拟机的信息，及推送时的申请服务id，虚拟机id）
        //拿到推送时的虚拟机id，然后把此条虚拟机信息状态update
        //然后再查询其他没有创建的虚拟机发送去创建
    }
}
