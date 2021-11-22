package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.region.RegionEntity;
import com.ecdata.cmp.huawei.dto.vo.SubnetsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RegionEntityResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private RegionEntity data;

    public RegionEntityResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public RegionEntityResponse(RegionEntity data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public RegionEntityResponse(ResultEnum resultEnum, RegionEntity data) {
        super(resultEnum);
        this.data = data;
    }
}
