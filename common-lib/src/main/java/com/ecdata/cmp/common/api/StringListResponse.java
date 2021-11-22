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
 * @since 2019-10-16
*/
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StringListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<String> data;

    public StringListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public StringListResponse(List<String> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public StringListResponse(ResultEnum resultEnum) {
        super(resultEnum);
    }

    public StringListResponse(ResultEnum resultEnum, List<String> data) {
        super(resultEnum);
        this.data = data;
    }

}
