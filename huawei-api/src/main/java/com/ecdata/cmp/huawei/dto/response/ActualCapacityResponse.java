package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.region.ActualCapacity;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ActualCapacityResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Map<String, ActualCapacity> data;

    public ActualCapacityResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ActualCapacityResponse(Map<String, ActualCapacity> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ActualCapacityResponse(ResultEnum resultEnum, Map<String, ActualCapacity> data) {
        super(resultEnum);
        this.data = data;
    }
}
