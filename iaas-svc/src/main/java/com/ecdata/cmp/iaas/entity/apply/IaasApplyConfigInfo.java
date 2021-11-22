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
 * @author xuxiaojian
 * @date 2020/3/3 14:57
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("iaas_apply_config_info")
@ApiModel(value = "配置信息表", description = "配置信息表")
public class IaasApplyConfigInfo extends Model<IaasApplyConfigInfo> {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("申请id")
    private Long applyId;

    @ApiModelProperty("申请类型：1.虚拟机，2.裸金属，3.安全资源")
    private String applyType;

    @ApiModelProperty("操作类型：1.新增；2.变更;3.删除")
    private String operationType;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("服务器的名称信息")
    private String serverName;

    @ApiModelProperty("操作系统")
    private String operationSystem;

    @ApiModelProperty("所属区域id")
    private String areaId;

    @ApiModelProperty("cpu数量")
    private Integer cpu;

    @ApiModelProperty("内存数")
    private Integer memory;

    @ApiModelProperty("系统盘（g）")
    private Double systemDisk;

    @ApiModelProperty("申请服务器的数量")
    private Integer vmNum;

    @ApiModelProperty("租期，默认永久，可以选择具体到期日期")
    private String lease;

    @ApiModelProperty("端口映射要求:本机端口")
    private String nativePort;

    @ApiModelProperty("端口映射要求:对方端口")
    private String oppositePort;

    @ApiModelProperty("是否需要互联网访问：0，不选，1.必选，默认：0")
    private String portMapping;

    @ApiModelProperty("服务器密码")
    private String password;

    @ApiModelProperty("是否灾备服务器：0，不选，1.必选，默认：0")
    private String isDisasterServer;

    @ApiModelProperty("软件要求")
    private String softwareRequire;

    @ApiModelProperty("其他要求")
    private String otherRequire;

    @ApiModelProperty("前缀")
    private String prefixName;

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

    @ApiModelProperty("eip")
    private String eip;

    @ApiModelProperty("是否二级等保")
    private String isTwoCcrc;

    @ApiModelProperty("是否三级等保")
    private String isThreeCcrc;

    @ApiModelProperty("变更前申请id")
    private Long oldApplyId;

    @ApiModelProperty("变更前流程实例id")
    private String oldProcessInstanceId;

    @ApiModelProperty("是否自动发放资源")
    private String autoIssue;

    @ApiModelProperty("虚拟机ip")
    private String ipAddress;

    @ApiModelProperty("eipId")
    private String eipId;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
