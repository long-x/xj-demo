package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.RoleVO;
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
public class RoleResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private RoleVO data;

    public RoleResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public RoleResponse(RoleVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public RoleResponse(ResultEnum resultEnum, RoleVO data) {
        super(resultEnum);
        this.data = data;
    }
}
