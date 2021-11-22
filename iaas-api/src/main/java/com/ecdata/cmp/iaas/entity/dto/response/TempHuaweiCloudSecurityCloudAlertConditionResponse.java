package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempHuaweiCloudSecurityCloudAlertConditionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @title: TempHuaweiCloudSecurityCloudAlertConditionResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 5:56 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempHuaweiCloudSecurityCloudAlertConditionResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private TempHuaweiCloudSecurityCloudAlertConditionVO data;

    public TempHuaweiCloudSecurityCloudAlertConditionResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempHuaweiCloudSecurityCloudAlertConditionResponse(TempHuaweiCloudSecurityCloudAlertConditionVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempHuaweiCloudSecurityCloudAlertConditionResponse(ResultEnum resultEnum, TempHuaweiCloudSecurityCloudAlertConditionVO data) {
        super(resultEnum);
        this.data = data;
    }
}