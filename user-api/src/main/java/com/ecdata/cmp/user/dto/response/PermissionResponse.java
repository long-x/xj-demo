package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.PermissionVO;
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
public class PermissionResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PermissionVO data;

    public PermissionResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public PermissionResponse(PermissionVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public PermissionResponse(ResultEnum resultEnum, PermissionVO data) {
        super(resultEnum);
        this.data = data;
    }
}
