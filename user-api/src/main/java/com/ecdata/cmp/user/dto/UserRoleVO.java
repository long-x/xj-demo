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
 * @since 2019-04-19
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "用户角色对象", description = "用户角色对象")
public class UserRoleVO implements Serializable {

    private static final long serialVersionUID = -954815574607716565L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /** 角色id */
    @ApiModelProperty(value = "角色id")
    private Long roleId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public UserRoleVO() {
    }
    public UserRoleVO(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
    public UserRoleVO(Long id, Long userId, Long roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }
}
