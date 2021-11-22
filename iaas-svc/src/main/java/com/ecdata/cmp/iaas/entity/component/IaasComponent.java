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
 * @since 2019/11/13 10:02,
 */


@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@TableName("iaas_component")
@ApiModel(value = "组件对象", description = "组件对象")
public class IaasComponent extends Model<IaasComponent> {

    private static final long serialVersionUID = 2769640378954704712L;
    /** 主键 */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** 租户id */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /** 组件种类 */
    @ApiModelProperty(value = "组件种类")
    private String kind;

    /** 显示名称 */
    @ApiModelProperty(value = "显示名称")
    private String displayName;

//    @ApiModelProperty(value = "父主键")
//    private Long parentId;

    @ApiModelProperty(value = "所属操作系统(1:Windows;2:Linux;)")
    private Integer osType;

    @ApiModelProperty(value = "封面头像")
    private String cover;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "初始类型(0:非初始组件; 1:顶级租户初始组件; 2:子租户租户初始组件;)")
    private Integer initType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private Long updateUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 通过注解方式和XML形式自定义的SQL不会自动加上逻辑条件
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已正常)")
    private Integer isDeleted;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
