package com.ecdata.cmp.user.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * @since 2020/1/13 11:59,
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@TableName("res_dictionary")
@ApiModel(value = "字典对象", description = "字典对象")
public class ResDictionary extends Model<ResDictionary> {
    private static final long serialVersionUID = 3853745571265568645L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("业务类型:1--工单")
    private Integer businessType;

    @ApiModelProperty("表字段:1--可用分区，2--当前规格，3--镜像，4--系统盘，5--创建数量，" +
            "6--虚拟私有云，7--网卡，8--安全组，9--弹性云服务器名称，10--描述")
    private Integer type;

    @ApiModelProperty("关联id：工单id")
    private Long resId;


    @ApiModelProperty("工单属性")
    private String mKey;

    @ApiModelProperty("属性值")
    private String mValue;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("修改时间")
    private String groupId;

    @TableLogic
    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private boolean isDeleted;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
