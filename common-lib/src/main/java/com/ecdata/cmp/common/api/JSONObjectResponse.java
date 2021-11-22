package com.ecdata.cmp.common.api;

import com.alibaba.fastjson.JSONObject;
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
public class JSONObjectResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private JSONObject data;

    public JSONObjectResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public JSONObjectResponse(JSONObject data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public JSONObjectResponse(ResultEnum resultEnum, JSONObject data) {
        super(resultEnum);
        this.data = data;
    }

}
