package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempSecurityCloudSecurityEventOverviewVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @title: TempSecurityCloudSecurityEventOverviewListResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 5:24 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempSecurityCloudSecurityEventOverviewListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<TempSecurityCloudSecurityEventOverviewVO> data;

    public TempSecurityCloudSecurityEventOverviewListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempSecurityCloudSecurityEventOverviewListResponse(List<TempSecurityCloudSecurityEventOverviewVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempSecurityCloudSecurityEventOverviewListResponse(ResultEnum resultEnum, List<TempSecurityCloudSecurityEventOverviewVO> data) {
        super(resultEnum);
        this.data = data;
    }
}