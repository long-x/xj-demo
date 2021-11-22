package com.ecdata.cmp.common.api;

import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxinsheng
 * @since 2019-10-30
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IntegerResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private Integer data;

    public IntegerResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IntegerResponse(Integer data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IntegerResponse(ResultEnum resultEnum, Integer data) {
        super(resultEnum);
        this.data = data;
    }

}
