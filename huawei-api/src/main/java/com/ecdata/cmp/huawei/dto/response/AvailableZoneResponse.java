package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZone;
import com.ecdata.cmp.huawei.dto.availablezone.AvailableZoneResource;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ty
 * @description:
 * @date 2020/1/15 15:32
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AvailableZoneResponse  extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<AvailableZone> data;

    public AvailableZoneResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public AvailableZoneResponse(List<AvailableZone> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public AvailableZoneResponse(ResultEnum resultEnum, List<AvailableZone> data) {
        super(resultEnum);
        this.data = data;
    }
}
