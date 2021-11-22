package com.ecdata.cmp.iaas.entity.dto.workbench;

import com.ecdata.cmp.iaas.entity.dto.IaasVirtualMachineVO;
import lombok.Data;

import java.util.List;

/**
 * 业务组关联资源池
 */
@Data
public class ResourcePoolStatisticsVO {
    private Long id;
    private String poolName;
    private int cpuTotal;//CPU配额总数
    private int memoryTotal;//内存配额总量（GB）
    private int vmTotal;//虚拟机容量
    private int vmUsed;//虚拟机已分配容量
    private List<BusinessGroupStatisticsVO> businessGroupStatisticsVOS;
    private List<IaasVirtualMachineVO> iaasVirtualMachineVOList;
}
