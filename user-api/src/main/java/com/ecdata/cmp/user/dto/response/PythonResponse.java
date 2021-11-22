package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.PythonVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxinsheng
 * @since 2019-11-22
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PythonResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PythonVO data;

    public PythonResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public PythonResponse(PythonVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public PythonResponse(ResultEnum resultEnum, PythonVO data) {
        super(resultEnum);
        this.data = data;
    }
}
