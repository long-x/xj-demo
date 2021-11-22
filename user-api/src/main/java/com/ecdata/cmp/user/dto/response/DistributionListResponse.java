package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.DistributionDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-12-05
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DistributionListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<DistributionDTO> data;

    public DistributionListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public DistributionListResponse(List<DistributionDTO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public DistributionListResponse(ResultEnum resultEnum, List<DistributionDTO> data) {
        super(resultEnum);
        this.data = data;
    }

}
