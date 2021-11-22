package com.ecdata.cmp.common.api;

import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-09-11
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LongListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<Long> data;

    public LongListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public LongListResponse(List<Long> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public LongListResponse(ResultEnum resultEnum, List<Long> data) {
        super(resultEnum);
        this.data = data;
    }

}
