package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2020/1/14 16:52,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MapResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data;

    public MapResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public MapResponse(Map<String,Object> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public MapResponse(ResultEnum resultEnum, Map<String,Object> data) {
        super(resultEnum);
        this.data = data;
    }
}
