package com.ecdata.cmp.iaas.entity.dto.response.template;

import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineComponentOperationVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineComponentParamVO;
import com.ecdata.cmp.iaas.entity.dto.IaasTemplateVirtualMachineComponentScriptVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-21 16:01
 */
@Data
public class TemplateVirtualMachineComponentTreeResponse {
    private Long id;
    private Long key;
    private String title;

    @ApiModelProperty("虚拟机id")
    private String vmId;

    @ApiModelProperty("关联iaas组件id")
    private Long componentId;

    @ApiModelProperty("组件种类")
    private String kind;

    @ApiModelProperty("显示名称")
    private String displayName;

    @ApiModelProperty("父主键")
    private Long parentId;

    @ApiModelProperty(value = "封面头像")
    private String cover;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty("所属操作系统(1:windows;2:linux;)")
    private Integer osType;

    @ApiModelProperty("排序")
    private int sort;

    @ApiModelProperty("组件参数")
    private List<IaasTemplateVirtualMachineComponentParamVO> compParams;

    @ApiModelProperty("组件操作")
    private List<IaasTemplateVirtualMachineComponentOperationVO> compOps;

    @ApiModelProperty("组件脚本")
    private List<IaasTemplateVirtualMachineComponentScriptVO> compScripts;

    private List<TemplateVirtualMachineComponentTreeResponse> children;
}
