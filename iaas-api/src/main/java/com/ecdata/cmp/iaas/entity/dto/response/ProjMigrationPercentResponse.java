package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoYX
 * @since 2019/11/20 14:11,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProjMigrationPercentResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private double data;

    public ProjMigrationPercentResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ProjMigrationPercentResponse(double data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ProjMigrationPercentResponse(ResultEnum resultEnum, double data) {
        super(resultEnum);
        this.data = data;
    }
}
