package com.ecdata.cmp.iaas.entity.dto.request;

import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-18 16:52
 */
@Data
public class IaasTemplateRequest implements Serializable {

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "状态(1:保存;2:发布;)")
    private Integer state;

    @ApiModelProperty(value = "虚拟机对象")
    private List<IaasTemplateVirtualMachineVO> iaasTemplateVirtualMachineVOS;

    /**
     * 获取虚拟机ids
     *
     * @return
     */
    public List<Long> machineIds() {
        if (CollectionUtils.isEmpty(this.iaasTemplateVirtualMachineVOS)) {
            return Collections.emptyList();
        }

        return this.iaasTemplateVirtualMachineVOS.stream().map(IaasTemplateVirtualMachineVO::getId).collect(Collectors.toList());
    }

    /**
     * 获取组件ids
     *
     * @return
     */
    public List<Long> componentIds() {
        if (CollectionUtils.isEmpty(this.iaasTemplateVirtualMachineVOS)) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>();

        for (IaasTemplateVirtualMachineVO machineVO : iaasTemplateVirtualMachineVOS) {
            ids.addAll(machineVO.componentIds());
        }
        return ids;
    }

    /**
     * 获取组件参数ids
     *
     * @return
     */
    public List<Long> componentParamIds() {
        if (CollectionUtils.isEmpty(this.iaasTemplateVirtualMachineVOS)) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>();

        for (IaasTemplateVirtualMachineVO machineVO : iaasTemplateVirtualMachineVOS) {
            ids.addAll(machineVO.componentParamIds());
        }
        return ids;
    }

    /**
     * 虚机磁盘ids
     *
     * @return
     */
    public List<Long> machineDiskIds() {
        if (CollectionUtils.isEmpty(this.iaasTemplateVirtualMachineVOS)) {
            return Collections.emptyList();
        }

        List<Long> ids = new ArrayList<>();

        for (IaasTemplateVirtualMachineVO machineVO : iaasTemplateVirtualMachineVOS) {
            ids.addAll(machineVO.machineDiskIds());
        }
        return ids;
    }
}
