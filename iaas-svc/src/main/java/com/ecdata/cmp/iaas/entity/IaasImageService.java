package com.ecdata.cmp.iaas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("iaas_image_service")
@ApiModel(value = "镜像服务对象", description = "镜像服务对象")
public class IaasImageService extends Model<IaasImageService> {
    private static final long serialVersionUID = -2065543160384312558L;
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

    @ApiModelProperty(value = "vdcId")
    private Long vdcId;

    @ApiModelProperty(value = "是否被其他租户可见，取值为public或private")
    private String visibility;

    /**
     * 镜像状态。取值如下：
     * queued：表示镜像元数据已经创建成功，等待上传镜像文件。
     * saving：表示镜像正在上传文件到后端存储。
     * deleted：表示镜像已经删除。
     * killed：表示镜像上传错误。
     * active：表示镜像可以正常使用
     */
    @ApiModelProperty(value = "镜像状态。取值如下：queued：表示镜像元数据已经创建成功，等待上传镜像文件。saving：表示镜像正在上传文件到后端存储。deleted：表示镜像已经删除。killed：表示镜像上传错误。active：表示镜像可以正常使用")
    private String status;

    @ApiModelProperty(value = "镜像名称")
    private String name;

    @ApiModelProperty(value = "容器类型")
    private String containerFormat;

    @ApiModelProperty(value = "镜像格式，目前支持vhd，zvhd、raw，qcow2。默认值是vhd")
    private String diskFormat;

    @ApiModelProperty(value = "镜像运行需要的最小内存，单位为MB。参数取值依据弹性云服务器的规格限制，一般设置为0")
    private String minRam;

    @ApiModelProperty(value = "镜像运行需要的最小磁盘，单位为GB 。取值为40～1024GB。取值为1～1024GB。取值为40～255GB")
    private String minDisk;

    @ApiModelProperty(value = "操作系统位数，一般取值为32或者64")
    private String osBit;

    @ApiModelProperty(value = "镜像平台分类，取值为Windows，Ubuntu，RedHat，SUSE，CentOS，Debian，OpenSUSE, Oracle Linux，Fedora，Other，CoreOS和EuleOS")
    private String platform;

    @ApiModelProperty(value = "镜像系统类型，取值为Linux，Windows，Other")
    private String osType;

    @ApiModelProperty(value = "标签，用户为镜像增加自定义标签后可以通过该参数过滤查询")
    private String tag;

    @ApiModelProperty(value = "关联唯一key")
    private String imageKey;

    /**
     * 系统盘大小
     */
    @ApiModelProperty(value = "系统盘大小")
    private Double systemDiskSize;

    /**
     * 源
     */
    @ApiModelProperty(value = "源")
    private String source;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String imageDescribe;

    /**
     * 启动方式
     */
    @ApiModelProperty(value = "启动方式")
    private String startMode;

    /**
     * Cloud-Init,0:支持,1:不支持
     */
    @ApiModelProperty(value = "Cloud-Init,0:支持,1:不支持")
    private Long cloudInit;

    /**
     * CPU架构
     */
    @ApiModelProperty(value = "CPU架构")
    private String cpuFramework;

    /**
     * 磁盘设备类型
     */
    @ApiModelProperty(value = "磁盘设备类型")
    private String diskDeviceType;

    /**
     * 镜像大小
     */
    @ApiModelProperty(value = "镜像大小")
    private String imageSize;

    /**
     * 共享租户
     */
    @ApiModelProperty(value = "共享租户")
    private String shareTenet;

    /**
     * 当前版本
     */
    @ApiModelProperty(value = "当前版本")
    private String currentVersion;

    /**
     * 历史版本
     */
    @ApiModelProperty(value = "历史版本")
    private String historyVersion;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已正常)
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Boolean isDeleted;

    @ApiModelProperty(value = "操作系统版本")
    private String osVersion;

    @ApiModelProperty(value = "镜像创建时间")
    private String createdAt;

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}