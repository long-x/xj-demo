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
@ApiModel(value = "CsnsQueryResult", description = "CsnsQueryResult")
public class CsnsQueryResult implements Serializable {
    private static final long serialVersionUID = 1922679395701351504L;
    /**
     * 符合条件的记录数
     */
    @ApiModelProperty(value = "符合条件的记录数")
    private Integer count;
    /**
     * 查询记录数是否超出结果集上限
     */
    @ApiModelProperty(value = "查询记录数是否超出结果集上限")
    private Boolean sizeExceeded;

    /**
     * 符合条件记录的流水号集合
     */
    @ApiModelProperty(value = "符合条件记录的流水号集合")
    private List<Long> csns;
}
