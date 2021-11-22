package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempBusinessSystemResourceUsedVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @title: TempBusinessSystemResourceUsedListResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 3:10 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempBusinessSystemResourceUsedListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<TempBusinessSystemResourceUsedVO> data;

    public TempBusinessSystemResourceUsedListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempBusinessSystemResourceUsedListResponse(List<TempBusinessSystemResourceUsedVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempBusinessSystemResourceUsedListResponse(ResultEnum resultEnum, List<TempBusinessSystemResourceUsedVO> data) {
        super(resultEnum);
        this.data = data;
    }

}