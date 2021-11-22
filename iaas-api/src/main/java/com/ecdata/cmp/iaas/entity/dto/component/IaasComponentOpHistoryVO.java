package com.ecdata.cmp.iaas.entity.dto.component;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhaoYX
 * @since 2019/11/13 13:20,
 */

@Data
@ToString
@Accessors(chain = true)
@TableName("iaas_component_operation_history")
@ApiModel(value = "组件操作历史", description = "组件操作历史")
public class IaasComponentOpHistoryVO implements Serializable {

    private static final long serialVersionUID = -7604826255048634448L;
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 组件操作id
     */
    @ApiModelProperty("组件操作id")
    private Long componentOperationId;

    /**
     * 组件历史id
     */
    @ApiModelProperty("组件历史id")
    private Long componentHistoryId;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;

    /**
     * 组件id
     */
    @ApiModelProperty("组件id")
    private Long componentId;

    /**
     * 操作名
     */
    @ApiModelProperty("操作名")
    private String operationName;


    /**
     * 脚本id
     */
    @ApiModelProperty("脚本id")
    private Long scriptId;

    /**
     * 操作(create/configure/start/stop/restart/delete/other)
     */
    @ApiModelProperty("操作(create/configure/start/stop/restart/delete/other)")
    private String operation;

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
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 插入历史表时间
     */
    @ApiModelProperty("插入历史表时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date historyTime;

    /**
     * 是否已删除(0表示未删除，1表示已正常)
     */
    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private Boolean isDeleted;


}
