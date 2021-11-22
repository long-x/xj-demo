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
@ApiModel(value = "用户部门对象", description = "用户部门对象")
public class UserDepartmentVO implements Serializable {

    private static final long serialVersionUID = -4818624157813817481L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    private Long id;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /** 部门id */
    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    public UserDepartmentVO() {
    }
    public UserDepartmentVO(Long userId, Long departmentId) {
        this.userId = userId;
        this.departmentId = departmentId;
    }
    public UserDepartmentVO(Long id, Long userId, Long departmentId) {
        this.id = id;
        this.userId = userId;
        this.departmentId = departmentId;
    }
}
