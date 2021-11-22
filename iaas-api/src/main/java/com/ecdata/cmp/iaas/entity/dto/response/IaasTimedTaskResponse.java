package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasTimedTaskVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: xuj
 * @description: 定时开关机
 * @Date: 2020/5/31
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasTimedTaskResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private IaasTimedTaskVO data;

    public IaasTimedTaskResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasTimedTaskResponse(IaasTimedTaskVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasTimedTaskResponse(ResultEnum resultEnum, IaasTimedTaskVO data) {
        super(resultEnum);
        this.data = data;
    }

}