package com.ecdata.cmp.iaas.entity.dto.response.sangfor;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoYX
 * @since 2020/4/21 14:35,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SangforRiskResponse extends BaseResponse {
    @ApiModelProperty(value = "返回数据")
    private SangforSecurityRiskVO data;

    public SangforRiskResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SangforRiskResponse(SangforSecurityRiskVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SangforRiskResponse(ResultEnum resultEnum, SangforSecurityRiskVO data) {
        super(resultEnum);
        this.data = data;
    }
    
}
