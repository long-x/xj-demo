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

/**
 * iaas虚拟机表
 *
 * @author ：xuj
 * @date ：Created in 2019/11/23 15:24
 * @modified By：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("iaas_virtual_machine")
@ApiModel(value = "iaas虚拟机对象", description = "iaas虚拟机对象")
public class IaasVirtualMachine extends Model<IaasVirtualMachine> {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("项目id")
    private Long projectId;

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

    @ApiModelProperty("流程申请虚拟机id")
    private Long processApplyVmId;

    @ApiModelProperty("供应商id")
    private Long providerId;

    @ApiModelProperty("虚拟机镜像名称")
    private String imageName;

    @ApiModelProperty("虚机名")
    private String vmName;

    @ApiModelProperty(value = "0:未知;1:创建中;2:开机中;3:开机;4:挂起中;5:挂起;6:关机中;7:关机")
    private Integer status;

    @ApiModelProperty(value = "虚拟机状态：华为")
    private String state;

    @ApiModelProperty("操作系统全名")
    private String osName;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("操作系统类型(1:windows;2:linux;)")
    private Integer osType;

    @ApiModelProperty("总cpu核数")
    private Integer vcpuTotal;

    @ApiModelProperty("已用cpu核数")
    private Double vcpuUsed;

    @ApiModelProperty("内存(mb)")
    private Integer memoryTotal;

    @ApiModelProperty("内存(mb)")
    private Double memoryUsed;

    @ApiModelProperty("IP地址（华为专用）")
    private String ipAddress;

    @ApiModelProperty("登入用户名")
    private String username;

    @ApiModelProperty("登入密码")
    private String password;

    @ApiModelProperty("拥有者")
    private Long owner;

    @ApiModelProperty("关联唯一key")
    private String vmKey;

    @ApiModelProperty("虚拟机历史性能id")
    private String resId;

    @ApiModelProperty("虚机uuid")
    private String uuid;

    @ApiModelProperty("虚拟机使用率")
    private String diskUsage;

    @ApiModelProperty("权限类型(1:公开;2:依据虚机权限表)")
    private Integer rightType;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("上次启动时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastStartUp;

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

    @ApiModelProperty("1.虚拟机；2.裸金属；默认1")
    private String type;

    @ApiModelProperty("所在分区Id")
    private String azoneId;

    @ApiModelProperty("所在分区名称")
    private String azoneName;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
