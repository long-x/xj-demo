package com.ecdata.cmp.iaas.entity.component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:58,
 */

@Data
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@TableName("iaas_component_history")
@ApiModel(value = "组件历史记录", description = "组件历史记录")
public class IaasComponentHistory extends Model<IaasComponentHistory> {

    private static final long serialVersionUID = 3791423279907432993L;
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.INPUT)
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

//    /**
//     * 父主键
//     */
//    @ApiModelProperty("父主键")
//    private Long parentId;

    /**
     * 所属操作系统(1:windows;2:linux;)
     */
    @ApiModelProperty("所属操作系统(1:windows;2:linux;)")
    private Integer osType;

    /**
     * 版本
     */
    @ApiModelProperty("版本")
    private Integer version;

    /**
     * 初始类型(0:非初始组件; 1:顶级租户初始组件; 2:子租户租户初始组件;)
     */
    @ApiModelProperty("初始类型(0:非初始组件; 1:顶级租户初始组件; 2:子租户租户初始组件;)")
    private Integer initType;

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
    @TableLogic
    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private Boolean isDeleted;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
