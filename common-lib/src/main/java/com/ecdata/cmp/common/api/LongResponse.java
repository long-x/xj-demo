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
public class LongResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Long data;

    public LongResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public LongResponse(Long data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public LongResponse(ResultEnum resultEnum, Long data) {
        super(resultEnum);
        this.data = data;
    }

}
