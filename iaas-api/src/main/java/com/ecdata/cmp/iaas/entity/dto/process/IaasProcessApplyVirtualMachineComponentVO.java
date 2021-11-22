package com.ecdata.cmp.iaas.entity.dto.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-22 16:59
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas流程申请虚机组件表", description = "iaas流程申请虚机组件表")
public class IaasProcessApplyVirtualMachineComponentVO {
    @ApiModelProperty(value = "配合前台树状")
    private Long key;

    @ApiModelProperty(value = "配合前台树状")
    private String title;

    @ApiModelProperty(value = "配合前台树状")
    private String templateId;

    @ApiModelProperty(value = "配合前台树状")
    private String name;

    @ApiModelProperty(value = "配合前台树状")
    private Long vmId;

    @ApiModelProperty(value = "用户名 配合前台树状")
    private String uname;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("关联流程申请虚机id")
    private Long processApplyVmId;

    @ApiModelProperty("关联iaas组件id")
    private Long componentId;

    @ApiModelProperty("组件种类")
    private String kind;

    @ApiModelProperty("显示名称")
    private String displayName;

    @ApiModelProperty("父主键")
    private Long parentId;

    @ApiModelProperty("所属操作系统(1:windows;2:linux;)")
    private Integer osType;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty(value = "封面头像")
    private String cover;

    @ApiModelProperty("关联的组件的版本")
    private Integer version;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新人")
    private Long updateUser;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private Integer isDeleted;

    @ApiModelProperty("组件参数")
    private List<IaasProcessApplyVirtualMachineComponentParamVO> compParams;

    @ApiModelProperty("组件脚本")
    private List<IaasProcessApplyVirtualMachineComponentScriptVO> compScripts;

    @ApiModelProperty("组件操作")
    private List<IaasProcessApplyVirtualMachineComponentOperationVO> compOps;

    @ApiModelProperty("子组件")
    private List<IaasProcessApplyVirtualMachineComponentVO> children;
}
