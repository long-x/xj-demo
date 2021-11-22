package com.ecdata.cmp.common.api;

import com.alibaba.fastjson.JSONArray;
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
public class JSONArrayResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private JSONArray data;

    public JSONArrayResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public JSONArrayResponse(JSONArray data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public JSONArrayResponse(ResultEnum resultEnum, JSONArray data) {
        super(resultEnum);
        this.data = data;
    }

}
