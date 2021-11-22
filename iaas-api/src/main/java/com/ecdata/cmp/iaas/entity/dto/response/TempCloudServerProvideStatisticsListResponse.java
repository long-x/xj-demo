package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempCloudServerProvideStatisticsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @title: TempCloudServerProvideStatisticsListResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 3:04 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempCloudServerProvideStatisticsListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<TempCloudServerProvideStatisticsVO> data;

    public TempCloudServerProvideStatisticsListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempCloudServerProvideStatisticsListResponse(List<TempCloudServerProvideStatisticsVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempCloudServerProvideStatisticsListResponse(ResultEnum resultEnum, List<TempCloudServerProvideStatisticsVO> data) {
        super(resultEnum);
        this.data = data;
    }
}