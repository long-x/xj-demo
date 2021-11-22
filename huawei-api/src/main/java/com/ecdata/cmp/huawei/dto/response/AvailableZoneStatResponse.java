package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
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
public class AvailableZoneStatResponse extends BaseResponse {//AvailableZoneStatistics
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private AvailableZoneStatistics data;

    public AvailableZoneStatResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public AvailableZoneStatResponse(AvailableZoneStatistics data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public AvailableZoneStatResponse(ResultEnum resultEnum, AvailableZoneStatistics data) {
        super(resultEnum);
        this.data = data;
    }
}
