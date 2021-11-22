package com.ecdata.cmp.common.api;

import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: Gaspar
 * @Description:
 * @Date: 2019/10/31 17:25
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class JSONObjectListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<JSONObject> data;

    public JSONObjectListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public JSONObjectListResponse(List<JSONObject> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public JSONObjectListResponse(ResultEnum resultEnum) {
        super(resultEnum);
    }

    public JSONObjectListResponse(ResultEnum resultEnum, List<JSONObject> data) {
        super(resultEnum);
        this.data = data;
    }

}
