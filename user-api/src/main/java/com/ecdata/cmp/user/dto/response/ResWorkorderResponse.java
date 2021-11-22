package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.ResWorkorderVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoYX
 * @since 2019/11/20 10:59,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResWorkorderResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private ResWorkorderVO data;

    public ResWorkorderResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResWorkorderResponse(ResWorkorderVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResWorkorderResponse(ResultEnum resultEnum, ResWorkorderVO data) {
        super(resultEnum);
        this.data = data;
    }
}
