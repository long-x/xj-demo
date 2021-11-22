package com.ecdata.cmp.common.api;

import com.alibaba.fastjson.JSONObject;
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
public class JSONPageResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<JSONObject> data;

    public JSONPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public JSONPageResponse(PageVO<JSONObject> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public JSONPageResponse(ResultEnum resultEnum, PageVO<JSONObject> data) {
        super(resultEnum);
        this.data = data;
    }

}
