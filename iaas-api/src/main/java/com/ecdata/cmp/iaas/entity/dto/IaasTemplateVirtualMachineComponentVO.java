package com.ecdata.cmp.iaas.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述:
 * 组件
 *
 * @author xxj
 * @create 2019-11-18 16:56
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas模板虚机组件", description = "iaas模板虚机组件")
public class IaasTemplateVirtualMachineComponentVO implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("关联模板虚机id")
    private Long vmId;

    @ApiModelProperty("关联iaas组件id")
    private Long componentId;

    @ApiModelProperty("组件种类")
    private String kind;

    @ApiModelProperty("显示名称")
    private String displayName;

    @ApiModelProperty(value = "用户名")
    private String uname;

    @ApiModelProperty("父主键")
    private Long parentId;

    @ApiModelProperty("所属操作系统(1:windows;2:linux;)")
    private Integer osType;

    @ApiModelProperty("排序")
    private int sort;

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

    @ApiModelProperty("模板虚机组件")
    private List<IaasTemplateVirtualMachineComponentParamVO> componentParamVOList;

    @ApiModelProperty("模板虚拟机组件脚本")
    private List<IaasTemplateVirtualMachineComponentScriptVO> componentScriptVOList;

    @ApiModelProperty("模板虚拟机组件操作")
    private List<IaasTemplateVirtualMachineComponentOperationVO> componentOperationVOList;

    /**
     * 获取组件参数ids
     *
     * @return
     */
    public List<Long> componentParamIds() {
        if (CollectionUtils.isEmpty(this.componentParamVOList)) {
            return Collections.emptyList();
        }

        return this.componentParamVOList.stream().map(IaasTemplateVirtualMachineComponentParamVO::getId).collect(Collectors.toList());
    }
}
