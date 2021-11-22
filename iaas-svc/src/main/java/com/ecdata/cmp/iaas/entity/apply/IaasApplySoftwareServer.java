package com.ecdata.cmp.iaas.entity.apply;

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

/**
 * 软件服务
 *
 * @author xxj
 * @date 2020-03-10 13:50:16
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iaas_apply_software_server")
@ApiModel(value = "软件服务", description = "软件服务")
public class IaasApplySoftwareServer extends Model<IaasApplySoftwareServer> {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 租户id
     */
    @ApiModelProperty(value = "创建时间")
    private Long tenantId;
    /**
     * 申请id
     */
    @ApiModelProperty(value = "创建时间")
    private Long applyId;

    /**
     * 配置id
     */
    @ApiModelProperty(value = "配置id")
    private Long configId;

    /**
     * 所在区域
     */
    @ApiModelProperty(value = "创建时间")
    private Long areaId;
    /**
     * 操作类型：1.新增；2.变更;3.删除
     */
    @ApiModelProperty(value = "创建时间")
    private String operationType;
    /**
     * 服务器的名称
     */
    @ApiModelProperty(value = "创建时间")
    private String serverName;
    /**
     * eip
     */
    @ApiModelProperty(value = "创建时间")
    private String eip;
    /**
     * 原软件要求
     */
    @ApiModelProperty(value = "创建时间")
    private String originalSoftwareClaim;
    /**
     * 现软件要求
     */
    @ApiModelProperty(value = "创建时间")
    private String currentSoftwareClaim;
    /**
     * 其他要求
     */
    @ApiModelProperty(value = "创建时间")
    private String otherClaim;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建时间")
    private Long createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "创建时间")
    private Integer isDeleted;
    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
