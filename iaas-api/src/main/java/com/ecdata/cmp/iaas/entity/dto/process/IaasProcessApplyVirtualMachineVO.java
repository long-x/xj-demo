package com.ecdata.cmp.iaas.entity.dto.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-22 16:59
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas流程申请虚机表", description = "iaas流程申请虚机表")
public class IaasProcessApplyVirtualMachineVO {
    @ApiModelProperty(value = "配合前台树状")
    private String templateId;

    @ApiModelProperty(value = "配合前台树状")
    private Long key;

    @ApiModelProperty(value = "配合前台树状")
    private String title;

    @ApiModelProperty(value = "配合前台树状")
    private Long parentId;

    @ApiModelProperty(value = "配合前台树状")
    private Long catalogId;

    @ApiModelProperty(value = "父类id（配合前台树状）")
    private Integer state;

    @ApiModelProperty("配合前台树状")
    private String name;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("流程申请id")
    private Long processApplyId;

    @ApiModelProperty("主机id")
    private Long hostId;

    @ApiModelProperty("主机存储id")
    private Long datastoreId;

    @ApiModelProperty("虚机名")
    private String vmName;

    @ApiModelProperty("cpu核数")
    private Integer vcpu;

    @ApiModelProperty("内存(mb)")
    private Integer memory;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("操作系统类型(1:windows;2:linux;)")
    private Integer osType;

    @ApiModelProperty("登入用户名")
    private String username;

    @ApiModelProperty("登入密码")
    private String password;

    @ApiModelProperty("排序")
    private Integer sort;

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

    @ApiModelProperty("虚拟机磁盘")
    private List<IaasProcessApplyVirtualMachineDiskVO> machineDiskVOList;

    @ApiModelProperty("虚拟机组件")
    private List<IaasProcessApplyVirtualMachineComponentVO> children;
}
