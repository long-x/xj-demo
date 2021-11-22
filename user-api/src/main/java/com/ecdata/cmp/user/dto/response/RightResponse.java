package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.RightDTO;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxinsheng
 * @since 2020-01-08
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RightResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private RightDTO data;

    public RightResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public RightResponse(RightDTO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public RightResponse(ResultEnum resultEnum, RightDTO data) {
        super(resultEnum);
        this.data = data;
    }
}
