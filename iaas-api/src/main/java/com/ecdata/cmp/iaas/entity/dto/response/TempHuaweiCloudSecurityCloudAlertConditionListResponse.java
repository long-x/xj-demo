package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempHuaweiCloudSecurityCloudAlertConditionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @title: TempHuaweiCloudSecurityCloudAlertConditionListResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 5:46 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempHuaweiCloudSecurityCloudAlertConditionListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<TempHuaweiCloudSecurityCloudAlertConditionVO> data;

    public TempHuaweiCloudSecurityCloudAlertConditionListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempHuaweiCloudSecurityCloudAlertConditionListResponse(List<TempHuaweiCloudSecurityCloudAlertConditionVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempHuaweiCloudSecurityCloudAlertConditionListResponse(ResultEnum resultEnum, List<TempHuaweiCloudSecurityCloudAlertConditionVO> data) {
        super(resultEnum);
        this.data = data;
    }
}