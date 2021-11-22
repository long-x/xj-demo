package com.ecdata.cmp.huawei.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/3 15:47
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "虚拟机镜像返回对象", description = "虚拟机镜像返回对象")
public class ImagesVO {

    @ApiModelProperty(value = "镜像ID")
    private String id;

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

    @ApiModelProperty(value = "创建时间")
    private String createdAt;

    @ApiModelProperty(value = "修改时间")
    private String updatedAt;

    @ApiModelProperty(value = "checksum")
    private String checksum;

    @ApiModelProperty(value = "__support_static_ip")
    private String supportStaticIp;

    @ApiModelProperty(value = "__isregistered")
    private String isregistered;

    @ApiModelProperty(value = "architecture")
    private String architecture;

    @ApiModelProperty(value = "归属")
    private String owner;

    @ApiModelProperty(value = "__virtual_size")
    private String virtualSize;

    @ApiModelProperty(value = "hw_firmware_type")
    private String hwFirmwareType;

    @ApiModelProperty(value = "镜像类型")
    private String imagetype;

    @ApiModelProperty(value = "cloudinit")
    private String cloudinit;

    @ApiModelProperty(value = "virtual_env_type")
    private String virtualEnvType;

    @ApiModelProperty(value = "__support_kvm_nvme_ssd")
    private String supportKvmNvmeSsd;

    @ApiModelProperty(value = "hw_disk_bus")
    private String hwDiskBus;

    @ApiModelProperty(value = "size")
    private String size;

    @ApiModelProperty(value = "版本")
    private String osVersion;

    @ApiModelProperty(value = "self")
    private String self;

}
