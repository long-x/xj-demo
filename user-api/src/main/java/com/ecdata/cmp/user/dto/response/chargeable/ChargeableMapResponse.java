package com.ecdata.cmp.user.dto.response.chargeable;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/11/29 20:07,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChargeableMapResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data;

    public ChargeableMapResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ChargeableMapResponse(Map<String,Object> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ChargeableMapResponse(ResultEnum resultEnum, Map<String,Object> data) {
        super(resultEnum);
        this.data = data;
    }
}
