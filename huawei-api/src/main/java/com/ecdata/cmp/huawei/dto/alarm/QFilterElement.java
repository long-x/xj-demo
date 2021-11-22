package com.ecdata.cmp.huawei.dto.alarm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "告警查询过滤条件", description = "告警查询过滤条件")
public class QFilterElement implements Serializable {
    private static final long serialVersionUID = 2095340490642478865L;
    /**
     * 条件名称
     */
    @ApiModelProperty(value = "条件名称")
    private String name;

    /**
     * 条件对应的字段名称
     */
    @ApiModelProperty(value = "条件对应的字段名称")
    private String field;

    /**
     * 条件操作符
     */
    @ApiModelProperty(value = "条件操作符")
    private String operator;

    /**
     * 条件取值
     */
    @ApiModelProperty(value = "条件取值")
    private List<String> values;
}
