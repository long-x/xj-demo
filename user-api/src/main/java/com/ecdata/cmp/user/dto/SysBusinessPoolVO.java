package com.ecdata.cmp.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/21 10:28
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "业务组资源池关联对象", description = "业务组资源池关联对象")
public class SysBusinessPoolVO implements Serializable {

    private static final long serialVersionUID = -8639393911001824378L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("类型(1:iaas;2:paas;)")
    private boolean type;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("资源池id")
    private Long poolId;

    @ApiModelProperty("创建人")
    private Long createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /*其他属性*/
    @ApiModelProperty("页面名称")
    private String businessGroupName;

}
