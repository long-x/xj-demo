package com.ecdata.cmp.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 19:09
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "业务组成员对象", description = "业务组成员对象")
public class SysBusinessMemberVO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("创建人")
    private Long createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;


}
