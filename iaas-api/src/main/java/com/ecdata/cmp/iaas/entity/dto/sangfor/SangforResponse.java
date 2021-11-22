package com.ecdata.cmp.iaas.entity.dto.sangfor;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author ZhaoYX
 * @since 2020/4/27 16:40,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SangforResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private SangforSecurityRiskVO data;

    public SangforResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SangforResponse(SangforSecurityRiskVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SangforResponse(ResultEnum resultEnum, SangforSecurityRiskVO data) {
        super(resultEnum);
        this.data = data;
    }
}
