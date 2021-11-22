package com.ecdata.cmp.activiti.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xuxinsheng
 * @since 2020-01-14
 */
@Data
public class DisableRequest implements Serializable {

    private static final long serialVersionUID = 9176525610589707898L;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 是否已禁用(0:未禁用;1:已禁用)
     */
    @ApiModelProperty("是否已禁用(0:未禁用;1:已禁用)")
    private Integer isDisabled;

}
