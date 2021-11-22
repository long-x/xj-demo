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
 * @author xuxinsheng
 * @since 2019-09-11
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PermissionTreeAndIdsResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Map<String, List> data;

    public PermissionTreeAndIdsResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public PermissionTreeAndIdsResponse(Map<String, List> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public PermissionTreeAndIdsResponse(ResultEnum resultEnum, Map<String, List> data) {
        super(resultEnum);
        this.data = data;
    }
}
