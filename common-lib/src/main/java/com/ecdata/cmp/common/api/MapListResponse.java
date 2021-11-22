package com.ecdata.cmp.common.api;

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
 * @since 2019-12-06
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MapListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<Map<String, Object>> data;

    public MapListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public MapListResponse(List<Map<String, Object>> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public MapListResponse(ResultEnum resultEnum, List<Map<String, Object>> data) {
        super(resultEnum);
        this.data = data;
    }

}
