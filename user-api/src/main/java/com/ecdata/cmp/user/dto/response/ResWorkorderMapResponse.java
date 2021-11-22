package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.ResWorkorderVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/11/22 16:05,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResWorkorderMapResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data;

    public ResWorkorderMapResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResWorkorderMapResponse(Map<String,Object> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResWorkorderMapResponse(ResultEnum resultEnum, Map<String,Object> data) {
        super(resultEnum);
        this.data = data;
    }
}
