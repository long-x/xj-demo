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
 * 网络策略信息表
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
@TableName("iaas_apply_network_policy")
@ApiModel(value = "网络策略信息表", description = "网络策略信息表")
public class IaasApplyNetworkPolicy extends Model<IaasApplyNetworkPolicy> {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    /**
     * 申请id
     */
    @ApiModelProperty(value = "申请id")
    private Long applyId;

    /**
     * 配置id
     */
    @ApiModelProperty(value = "配置id")
    private Long configId;

    /**
     * 所在区域
     */
    @ApiModelProperty(value = "所在区域")
    private Long areaId;
    /**
     * 操作类型：1.新增；2.变更;3.删除
     */
    @ApiModelProperty(value = "操作类型：1.新增；2.变更;3.删除")
    private String operationType;
    /**
     * 访问方式:1.内部访问 2.内部被访问 3.外网被访问
     */
    @ApiModelProperty(value = "访问方式:1.内部访问 2.内部被访问 3.外网被访问")
    private String accessMode;
    /**
     * 服务器的名称
     */
    @ApiModelProperty(value = "服务器的名称")
    private String serverName;
    /**
     * eip
     */
    @ApiModelProperty(value = "eip")
    private String eip;
    /**
     * 本机端口
     */
    @ApiModelProperty(value = "本机端口")
    private String localPort;
    /**
     * 映射服务器
     */
    @ApiModelProperty(value = "映射服务器")
    private String mappingServer;
    /**
     * 映射端口
     */
    @ApiModelProperty(value = "映射端口")
    private String mappingPort;
    /**
     * 其他要求
     */
    @ApiModelProperty(value = "其他要求")
    private String otherClaim;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
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
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;
    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
