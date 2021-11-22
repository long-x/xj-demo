package com.ecdata.cmp.common.api;

import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import okhttp3.Response;

/**
 * @author xuxinsheng
 * @since 2019-11-19
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResponseResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private Response data;

    public ResponseResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResponseResponse(Response data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResponseResponse(ResultEnum resultEnum) {
        super(resultEnum);
    }

    public ResponseResponse(ResultEnum resultEnum, Response data) {
        super(resultEnum);
        this.data = data;
    }

}
