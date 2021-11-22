package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.PermissionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2020/1/13 17:10,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ListMapResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<Map<String,Object>> data;

    public ListMapResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ListMapResponse(List<Map<String,Object>> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ListMapResponse(ResultEnum resultEnum, List<Map<String,Object>> data) {
        super(resultEnum);
        this.data = data;
    }
}
