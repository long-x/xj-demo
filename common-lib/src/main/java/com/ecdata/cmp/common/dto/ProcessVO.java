package com.ecdata.cmp.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/8 17:57
 * @modified By：
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "告警", description = "告警")
public class ProcessVO {



    /** csns */
    @ApiModelProperty(value = "csns")
    private List<Long> csns;


    /** status */
    @ApiModelProperty(value = "status")
    private Integer status;

}
