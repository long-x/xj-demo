package com.ecdata.cmp.activiti.dto.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xuxinsheng
 * @since 2020-01-07
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "流程定义对象", description = "流程定义对象")
public class ActProcessDefinitionVO implements Serializable {

    private static final long serialVersionUID = -6411027079364852536L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
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
    private String category;

    /**
     * 流程名称
     */
    @ApiModelProperty(value = "流程名称（该编号就是流程文件process元素的name属性值）")
    private String name;

    /**
     * 流程编号
     */
    @ApiModelProperty(value = "流程编号（该编号就是流程文件process元素的id属性值）")
    private String key;

    /**
     * 流程版本号
     */
    @ApiModelProperty(value = "流程版本号（由程序控制，新增即为1，修改后依次加1来完成的）")
    private Integer version;

    /**
     * 部署编号
     */
    @ApiModelProperty(value = "部署编号")
    private String deploymentId;

    /**
     * 资源文件名称
     */
    @ApiModelProperty(value = "资源文件名称")
    private String resourceName;

    /**
     * 图片资源文件名称
     */
    @ApiModelProperty(value = "图片资源文件名称")
    private String diagramResourceName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 是否有Start From Key
     */
    @ApiModelProperty(value = "是否有Start From Key")
    private Boolean hasStartFormKey;

    /**
     * 是否有图形记号
     */
    @ApiModelProperty(value = "是否有图形记号")
    private Boolean hasGraphicalNotation;

    /**
     * 暂停状态
     */
    @ApiModelProperty(value = "暂停状态")
    private Integer suspensionState;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;

}
