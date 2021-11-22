package com.ecdata.cmp.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xuxinsheng
 * @since 2019-05-07
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "租户对象", description = "租户对象")
public class TenantVO implements Serializable {

    private static final long serialVersionUID = 6131428739170800064L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 租户名称 */
    @ApiModelProperty(value = "租户名称")
    private String tenantName;

    /** 父主键 */
    @ApiModelProperty(value = "父主键")
    private Long parentId;

    /** 联系人 */
    @ApiModelProperty(value = "联系人")
    private String linkman;

    /** 联系电话 */
    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    /** 联系地址 */
    @ApiModelProperty(value = "联系地址")
    private String address;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

    /** 父租户名称 */
    @ApiModelProperty(value = "父租户名称")
    private String parentName;

}
