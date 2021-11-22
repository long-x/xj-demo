package com.ecdata.cmp.common.api;

import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StringPageResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<String> data;

    public StringPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public StringPageResponse(PageVO<String> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public StringPageResponse(ResultEnum resultEnum, PageVO<String> data) {
        super(resultEnum);
        this.data = data;
    }

}
