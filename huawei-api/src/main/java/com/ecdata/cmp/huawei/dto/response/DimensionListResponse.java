package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.availablezone.DimensionInfo;
import com.ecdata.cmp.huawei.dto.availablezone.SysAzone;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/3 13:17
 * @modified By：
 */

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DimensionListResponse  extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<DimensionInfo> data;

    public DimensionListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public DimensionListResponse(List<DimensionInfo> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public DimensionListResponse(ResultEnum resultEnum, List<DimensionInfo> data) {
        super(resultEnum);
        this.data = data;
    }
}