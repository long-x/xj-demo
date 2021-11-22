package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZoneResource;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZoneStatistics;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AvailableZoneResourceResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private AvailableZoneResource data;

    public AvailableZoneResourceResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public AvailableZoneResourceResponse(AvailableZoneResource data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public AvailableZoneResourceResponse(ResultEnum resultEnum, AvailableZoneResource data) {
        super(resultEnum);
        this.data = data;
    }
}
