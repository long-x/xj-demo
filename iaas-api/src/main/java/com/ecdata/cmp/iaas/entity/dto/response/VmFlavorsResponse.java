package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.VmFlavors;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ty
 * @description:
 * @date 2020/1/13 16:56
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VmFlavorsResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<VmFlavors> data;

    public VmFlavorsResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public VmFlavorsResponse(List<VmFlavors> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public VmFlavorsResponse(ResultEnum resultEnum, List<VmFlavors> data) {
        super(resultEnum);
        this.data = data;
    }
}
