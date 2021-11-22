package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.DistributionDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxinsheng
 * @since 2019-12-05
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DistributionResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private DistributionDTO data;

    public DistributionResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public DistributionResponse(DistributionDTO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public DistributionResponse(ResultEnum resultEnum, DistributionDTO data) {
        super(resultEnum);
        this.data = data;
    }

}
