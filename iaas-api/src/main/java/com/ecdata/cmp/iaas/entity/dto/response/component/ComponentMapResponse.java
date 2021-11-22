package com.ecdata.cmp.iaas.entity.dto.response.component;

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
 * @since 2019/11/28 10:36,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ComponentMapResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data;

    public ComponentMapResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ComponentMapResponse(Map<String,Object> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ComponentMapResponse(ResultEnum resultEnum, Map<String,Object> data) {
        super(resultEnum);
        this.data = data;
    }
}
