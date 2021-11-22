package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempHostResourceUsedVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @title: TempHostResourceUsedListResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 2:59 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempHostResourceUsedListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<TempHostResourceUsedVO> data;

    public TempHostResourceUsedListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempHostResourceUsedListResponse(List<TempHostResourceUsedVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempHostResourceUsedListResponse(ResultEnum resultEnum, List<TempHostResourceUsedVO> data) {
        super(resultEnum);
        this.data = data;
    }

}