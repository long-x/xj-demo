package com.ecdata.cmp.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xuxinsheng
 * @since 2019-07-02
 */
@Data
@Accessors(chain = true)
@TableName("ACT_RE_PROCDEF")
@ApiModel(value = "流程定义对象", description = "流程定义对象")
public class ActProcessDefinitionEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "ID_")
    private String id;

    /**
     * 版次
     */
    @ApiModelProperty(value = "版次")
    @TableField(value = "REV_")
    private Integer revision;

    /**
     * 流程命名空间
     */
    @ApiModelProperty(value = "流程命名空间（该编号就是流程文件targetNamespace的属性值）")
    @TableField(value = "CATEGORY_")
    private String category;

    /**
     * 流程名称
     */
    @ApiModelProperty(value = "流程名称（该编号就是流程文件process元素的name属性值）")
    @TableField(value = "NAME_")
    private String name;

    /**
     * 流程编号
     */
    @ApiModelProperty(value = "流程编号（该编号就是流程文件process元素的id属性值）")
    @TableField(value = "KEY_")
    private String key;

    /**
     * 流程版本号
     */
    @ApiModelProperty(value = "流程版本号（由程序控制，新增即为1，修改后依次加1来完成的）")
    @TableField(value = "VERSION_")
    private Integer version;

    /**
     * 部署编号
     */
    @ApiModelProperty(value = "部署编号")
    @TableField(value = "DEPLOYMENT_ID_")
    private String deploymentId;

    /**
     * 资源文件名称
     */
    @ApiModelProperty(value = "资源文件名称")
    @TableField(value = "RESOURCE_NAME_")
    private String resourceName;

    /**
     * 图片资源文件名称
     */
    @ApiModelProperty(value = "图片资源文件名称")
    @TableField(value = "DGRM_RESOURCE_NAME_")
    private String diagramResourceName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField(value = "DESCRIPTION_")
    private String description;

    /**
     * 是否有Start From Key
     */
    @ApiModelProperty(value = "是否有Start From Key")
    @TableField(value = "HAS_START_FORM_KEY_")
    private Boolean hasStartFormKey;

    /**
     * 是否有图形记号
     */
    @ApiModelProperty(value = "是否有图形记号")
    @TableField(value = "HAS_GRAPHICAL_NOTATION_")
    private Boolean hasGraphicalNotation;

    /**
     * 暂停状态
     */
    @ApiModelProperty(value = "暂停状态")
    @TableField(value = "SUSPENSION_STATE_")
    private Integer suspensionState;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    @TableField(value = "TENANT_ID_")
    private String tenantId;

}
