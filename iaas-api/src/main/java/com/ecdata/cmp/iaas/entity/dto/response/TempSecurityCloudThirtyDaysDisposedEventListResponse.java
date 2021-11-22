package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempSecurityCloudThirtyDaysDisposedEventVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempSecurityCloudThirtyDaysDisposedEventListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<TempSecurityCloudThirtyDaysDisposedEventVO> data;

    public TempSecurityCloudThirtyDaysDisposedEventListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempSecurityCloudThirtyDaysDisposedEventListResponse(List<TempSecurityCloudThirtyDaysDisposedEventVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempSecurityCloudThirtyDaysDisposedEventListResponse(ResultEnum resultEnum, List<TempSecurityCloudThirtyDaysDisposedEventVO> data) {
        super(resultEnum);
        this.data = data;
    }
}