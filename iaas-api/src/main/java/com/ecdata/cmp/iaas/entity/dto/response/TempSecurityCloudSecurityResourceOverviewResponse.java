package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempSecurityCloudSecurityResourceOverviewVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @title: TempSecurityCloudSecurityResourceOverviewResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 5:10 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempSecurityCloudSecurityResourceOverviewResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private TempSecurityCloudSecurityResourceOverviewVO data;

    public TempSecurityCloudSecurityResourceOverviewResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempSecurityCloudSecurityResourceOverviewResponse(TempSecurityCloudSecurityResourceOverviewVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempSecurityCloudSecurityResourceOverviewResponse(ResultEnum resultEnum, TempSecurityCloudSecurityResourceOverviewVO data) {
        super(resultEnum);
        this.data = data;
    }
}