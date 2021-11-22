package com.ecdata.cmp.iaas.entity.dto.component;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/13 11:15,
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(value = "组件历史记录", description = "组件历史记录")
public class IaasComponentHistoryVO implements Serializable {

    private static final long serialVersionUID = -4759244456221992535L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 组件id
     */
    @ApiModelProperty("组件id")
    private Long componentId;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;

    /**
     * 组件类型
     */
    @ApiModelProperty("组件类型")
    private String kind;

    /**
     * 显示名称
     */
    @ApiModelProperty("显示名称")
    private String displayName;

    /**
     * 所属操作系统(1:windows;2:linux;)
     */
    @ApiModelProperty("所属操作系统(1:windows;2:linux;)")
    private Boolean osType;

    /**
     * 版本
     */
    @ApiModelProperty("版本")
    private Integer version;

    /**
     * 初始类型(0:非初始组件; 1:顶级租户初始组件; 2:子租户租户初始组件;)
     */
    @ApiModelProperty("初始类型(0:非初始组件; 1:顶级租户初始组件; 2:子租户租户初始组件;)")
    private Boolean initType;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private Long updateUser;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 插入历史表时间
     */
    @ApiModelProperty("插入历史表时间")
    private Date historyTime;

    /**
     * 是否已删除(0表示未删除，1表示已正常)
     */
    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private Boolean isDeleted;

    @ApiModelProperty(value = "用户名")
    private String uname;

    @ApiModelProperty(value = "组件历史参数")
    private List<IaasComponentParamHistoryVO> compHisParams;

    @ApiModelProperty(value = "组件历史脚本")
    private List<IaasComponentScriptHistoryVO> compHisScripts;

    @ApiModelProperty(value = "组件历史操作--生命周期")
    private List<IaasComponentOpHistoryVO> compHisOps;

}
