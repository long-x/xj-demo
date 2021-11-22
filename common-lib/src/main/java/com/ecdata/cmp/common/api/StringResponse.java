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
public class StringResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private String data;

    public StringResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public StringResponse(String data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public StringResponse(ResultEnum resultEnum) {
        super(resultEnum);
    }

    public StringResponse(ResultEnum resultEnum, String data) {
        super(resultEnum);
        this.data = data;
    }

}
