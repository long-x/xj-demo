package com.ecdata.cmp.iaas.entity.catalog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-22 16:59
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iaas_catalog_virtual_machine_component")
@ApiModel(value = "iaas服务目录虚机组件表", description = "iaas服务目录虚机组件表")
public class IaasCatalogVirtualMachineComponent extends Model<IaasCatalogVirtualMachineComponent> {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("关联服务目录虚机id")
    private Long vmId;

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
    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
