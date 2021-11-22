package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempGroupOrganizationResourceUsedVO;
import com.ecdata.cmp.iaas.entity.dto.TempGroupOrganizationResourceUsedVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @title: TempGroupOrganizationResourceUsedListResponse
 * @Author: shig
 * @description:
 * @Date: 2019/12/11 5:46 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempGroupOrganizationResourceUsedListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<TempGroupOrganizationResourceUsedVO> data;

    public TempGroupOrganizationResourceUsedListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempGroupOrganizationResourceUsedListResponse(List<TempGroupOrganizationResourceUsedVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempGroupOrganizationResourceUsedListResponse(ResultEnum resultEnum, List<TempGroupOrganizationResourceUsedVO> data) {
        super(resultEnum);
        this.data = data;
    }
}