package com.ecdata.cmp.user.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/11/20 10:43,
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "工单对象", description = "工单对象")
public class ResWorkorderVO implements Serializable {

    private static final long serialVersionUID = 3031821060378712287L;
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("关联角色id")
    private Long roleId;

    @ApiModelProperty("工单名称")
    private String workorderName;

    @ApiModelProperty("工单类型(test--测试环境，ncProduce--非核心生产环境，produce--生产环境)")
    private String workorderType;

    @ApiModelProperty("重要性（urgert--紧急,normal--正常）")
    private String ponderance;

    @ApiModelProperty("优先级（emergent--紧急,high--高,medium--中,low--低）")
    private String priority;

    @ApiModelProperty("问题描述")
    private String problemDescription;

    @ApiModelProperty("审批意见")
    private String advice;

    @ApiModelProperty("状态  applying--申请中 closed--已关闭")
    private String status;

    @ApiModelProperty("机密信息")
    private String secret;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private Long createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("修改时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private boolean isDeleted;

    @ApiModelProperty("可用分区:测试区、互联网区、内网1区、内网2区")
    private String partion;

    @ApiModelProperty("可用分区名")
    private String partionName;

    @ApiModelProperty("弹性云服务器类型:通用型")
    private String floatServerType;

    @ApiModelProperty("当前规格")
    private String currentSpecs;

    @ApiModelProperty("规格列表")
    List<Map<String,Object>> flavors;

    @ApiModelProperty("镜像类型:公共镜像、私有镜像、共享镜像")
    private String mirrorType;

    @ApiModelProperty("对应镜像版本")
    private String mirrorVersion;

    @ApiModelProperty("镜像名")
    private String mirrorName;

    @ApiModelProperty("启动方式:bios、uefi")
    private String launchMode;

    @ApiModelProperty("必须同一存储:0--false,1--true")
    private boolean sameStorage;

    @ApiModelProperty("创建数量")
    private Integer createNumber;

    @ApiModelProperty("虚拟私有云")
    private String virtualPrivateCloud;

    @ApiModelProperty("子网")
    private String subnet;

    @ApiModelProperty("子网网段")
    private String subnetSegment;

    @ApiModelProperty("分配ip方式")
    private String assignIpMode;

    @ApiModelProperty("ipv4地址")
    private String ipv4Address;

    @ApiModelProperty("安全组")
    private String securityGroup;

    @ApiModelProperty("弹性ip:0--不使用、1--自动分配ip、2--使用已有")
    private Integer floatingIp;

    @ApiModelProperty("弹性云服务器名称")
    private String elasticEscName;

    @ApiModelProperty("弹性云服务器运行状态: 0--关闭、1--开启")
    private boolean elasticEscRunningStatus;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("高级配置")
    private String advancedConfiguration;
    /*
    高级配置下的
     */
    @ApiModelProperty("高级配置 -> 基础配置--区域")
    private String region;

    @ApiModelProperty("高级配置 -> 基础配置--镜像")
    private String mirror;

    @ApiModelProperty("高级配置 -> 基础配置--系统盘")
    private String systemDisk;

    @ApiModelProperty("高级配置 -> 基础配置--主网卡")
    private String mainNetworkCard;

    @ApiModelProperty("网卡")
    private String networkCard;

    @ApiModelProperty("申请时长:0--不限、1--1年、2--自定义")
    private Integer applicationTime;

    /*
    基础配置
     */
    @ApiModelProperty("基础配置 -> 所属区")
    private String respectiveArea;

    @ApiModelProperty("基础配置 -> 系统盘容量")
    private Integer systemDiskCapacity;

    @ApiModelProperty("基础配置 -> 数据盘")
    private String dataDisk;

    @ApiModelProperty("基础配置 -> 数据盘容量")
    private Integer dataDiskCapacity;

    @ApiModelProperty("基础配置 -> 是否scsi")
    private boolean isScsi;

    @ApiModelProperty("基础配置 -> 是否共享盘")
    private boolean isShareDisk;


    @ApiModelProperty(value = "部门列表")
    private List<DepartmentVO> departments;

    @ApiModelProperty(value = "业务组列表")
    private List<SysBusinessGroupVO> businessGroups;

    @ApiModelProperty(value = "用户名")
    private String uname;

    @ApiModelProperty("vdc名称--vdcName")
    private String vdcName;

    @ApiModelProperty("项目名--projectName")
    private String projectName;
}
