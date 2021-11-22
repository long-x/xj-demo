package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempCloudServerProvideStatisticsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @title: TempCloudServerProvideStatisticsResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 5:33 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempCloudServerProvideStatisticsResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private TempCloudServerProvideStatisticsVO data;

    public TempCloudServerProvideStatisticsResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempCloudServerProvideStatisticsResponse(TempCloudServerProvideStatisticsVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempCloudServerProvideStatisticsResponse(ResultEnum resultEnum, TempCloudServerProvideStatisticsVO data) {
        super(resultEnum);
        this.data = data;
    }
}