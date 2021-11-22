package com.ecdata.cmp.iaas.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-18 16:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "iaas模板虚机", description = "iaas模板虚机")
public class IaasTemplateVirtualMachineVO implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("模板id")
    private Long templateId;

    @ApiModelProperty("虚机名")
    private String vmName;

    @ApiModelProperty("cpu核数")
    private Integer vcpu;

    @ApiModelProperty("内存(mb)")
    private Integer memory;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("操作系统")
    private String osType;

    @ApiModelProperty("登入用户名")
    private String username;

    @ApiModelProperty("登入密码")
    private String password;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private Integer isDeleted;

    @ApiModelProperty("组件")
    private List<IaasTemplateVirtualMachineComponentVO> machineComponentVOS;

    @ApiModelProperty("模板虚机磁盘")
    private List<IaasTemplateVirtualMachineDiskVO> machineDiskVOS;

    /**
     * 获取组件ids
     *
     * @return
     */
    public List<Long> componentIds() {
        if (CollectionUtils.isEmpty(this.machineComponentVOS)) {
            return Collections.emptyList();
        }

        return this.machineComponentVOS.stream().map(IaasTemplateVirtualMachineComponentVO::getId).collect(Collectors.toList());
    }

    /**
     * 获取组件参数ids
     *
     * @return
     */
    public List<Long> componentParamIds() {
        if (CollectionUtils.isEmpty(this.machineComponentVOS)) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>();

        for (IaasTemplateVirtualMachineComponentVO componentVO : machineComponentVOS) {
            ids.addAll(componentVO.componentParamIds());
        }
        return ids;
    }

    /**
     * 虚机磁盘ids
     *
     * @return
     */
    public List<Long> machineDiskIds() {
        if (CollectionUtils.isEmpty(this.machineDiskVOS)) {
            return Collections.emptyList();
        }

        return this.machineDiskVOS.stream().map(IaasTemplateVirtualMachineDiskVO::getId).collect(Collectors.toList());
    }
}
