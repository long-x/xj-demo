package com.ecdata.cmp.iaas.entity.catalog;

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
@TableName("iaas_catalog_virtual_machine")
@ApiModel(value = "iaas服务目录虚机表", description = "iaas服务目录虚机表")
public class IaasCatalogVirtualMachin extends Model<IaasCatalogVirtualMachin> {
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

    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
