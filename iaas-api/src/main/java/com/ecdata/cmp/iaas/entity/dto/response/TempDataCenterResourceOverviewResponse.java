package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempDataCenterResourceOverviewVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @title: iaasProvider response
 * @Author: shig
 * @description: 数据中心资源总览临时表 响应对象
 * @Date: 2019/11/12 11:17 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempDataCenterResourceOverviewResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private TempDataCenterResourceOverviewVO data;

    public TempDataCenterResourceOverviewResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempDataCenterResourceOverviewResponse(TempDataCenterResourceOverviewVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempDataCenterResourceOverviewResponse(ResultEnum resultEnum, TempDataCenterResourceOverviewVO data) {
        super(resultEnum);
        this.data = data;
    }

}