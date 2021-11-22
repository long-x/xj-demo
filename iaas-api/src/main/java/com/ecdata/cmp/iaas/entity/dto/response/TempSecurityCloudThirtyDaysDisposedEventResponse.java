package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempSecurityCloudThirtyDaysDisposedEventVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @title: TempSecurityCloudThirtyDaysDisposedEventResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 5:56 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempSecurityCloudThirtyDaysDisposedEventResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private TempSecurityCloudThirtyDaysDisposedEventVO data;

    public TempSecurityCloudThirtyDaysDisposedEventResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempSecurityCloudThirtyDaysDisposedEventResponse(TempSecurityCloudThirtyDaysDisposedEventVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempSecurityCloudThirtyDaysDisposedEventResponse(ResultEnum resultEnum, TempSecurityCloudThirtyDaysDisposedEventVO data) {
        super(resultEnum);
        this.data = data;
    }
}