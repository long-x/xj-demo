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
public class SuperiorsResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private Map<String, List<Long>> data;

    public SuperiorsResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SuperiorsResponse(Map<String, List<Long>> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SuperiorsResponse(ResultEnum resultEnum, Map<String, List<Long>> data) {
        super(resultEnum);
        this.data = data;
    }

}
