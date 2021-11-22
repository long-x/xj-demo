package com.ecdata.cmp.iaas.entity;

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
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("iaas_project")
@ApiModel(value = "iaas项目表", description = "iaas项目表")
public class IaasProject extends Model<IaasProject> {
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("虚拟数据中心id")
    private Long vdcId;

    @ApiModelProperty("项目名")
    private String projectName;

    @ApiModelProperty("关联唯一key")
    private String projectKey;

    @ApiModelProperty("授权令牌")
    private String token;

    @ApiModelProperty("令牌获取时间")
    private Date tokenTime;

    @ApiModelProperty("排序")
    private Integer score;

    @ApiModelProperty("备注")
    private String remark;

   @ApiModelProperty("备注")
    private String domainName;

   @ApiModelProperty("备注")
    private String username;

   @ApiModelProperty("备注")
    private String password;

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
    private boolean isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
