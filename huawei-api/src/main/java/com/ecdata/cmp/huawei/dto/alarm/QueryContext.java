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
public class QueryContext implements Serializable {
    private static final long serialVersionUID = -1747930013427620444L;
    /**
     * 条件名称
     */
    @ApiModelProperty(value = "条件名称")
    private List<QFilterElement> filters;

    /**
     * 由“and”、“or”、括号和QFilterElement的name组成的条件逻辑表达式
     */
    /*@ApiModelProperty(value = "由“and”、“or”、括号和QFilterElement的name组成的条件逻辑表达式")
    private String expression;*/
}
