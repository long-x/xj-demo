package com.ecdata.cmp.common.api;

import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxinsheng
 * @since 2019-09-11
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BooleanResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Boolean data;

    public BooleanResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public BooleanResponse(Boolean data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public BooleanResponse(ResultEnum resultEnum) {
        super(resultEnum);
    }

    public BooleanResponse(ResultEnum resultEnum, Boolean data) {
        super(resultEnum);
        this.data = data;
    }

}
