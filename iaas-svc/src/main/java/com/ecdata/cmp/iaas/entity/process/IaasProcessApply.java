package com.ecdata.cmp.iaas.entity.process;

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
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-22 16:59
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iaas_process_apply")
@ApiModel(value = "iaas流程申请表", description = "iaas流程申请表")
public class IaasProcessApply extends Model<IaasProcessApply> {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("服务目录id")
    private Long catalogId;

    @ApiModelProperty("供应商id")
    private Long providerId;

    @ApiModelProperty("区域id")
    private Long areaId;

    @ApiModelProperty("集群id")
    private Long clusterId;

    @ApiModelProperty("主机id")
    private Long hostId;

    @ApiModelProperty("存储id")
    private Long datastoreId;

    @ApiModelProperty("资源池id")
    private Long poolId;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("流程申请名")
    private String processApplyName;

    @ApiModelProperty("流程状态(0:初始化;1:开始流程;2:流程处理中;3:流程结束;4:放弃流程;5:撤销流程)")
    private Integer state;

    @ApiModelProperty("租期")
    private Integer lease;

    @ApiModelProperty("周期(0:永久;1:小时;2:天;3:周;4:月;5:年;)")
    private Integer period;

    @ApiModelProperty("备注")
    private String remark;

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
    private Integer isDeleted;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
