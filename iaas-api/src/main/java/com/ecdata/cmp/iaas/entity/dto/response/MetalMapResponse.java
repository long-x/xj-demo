package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2020/4/29 13:44,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MetalMapResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data;

    public MetalMapResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public MetalMapResponse(Map<String,Object> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public MetalMapResponse(ResultEnum resultEnum, Map<String,Object> data) {
        super(resultEnum);
        this.data = data;
    }
}
