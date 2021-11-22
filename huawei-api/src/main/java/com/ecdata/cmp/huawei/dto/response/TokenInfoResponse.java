package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.token.TokenInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ty
 * @description
 * @date 2019/11/20 14:42
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TokenInfoResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private TokenInfoVO data;

    public TokenInfoResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TokenInfoResponse(TokenInfoVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TokenInfoResponse(ResultEnum resultEnum, TokenInfoVO data) {
        super(resultEnum);
        this.data = data;
    }
}
