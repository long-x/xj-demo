package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.TempCloudDataCenterVO;
import com.ecdata.cmp.iaas.entity.dto.TempHostResourceUsedVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @title: TempCloudDataCenter
 * @Author: shig
 * @description: 云数据中心临时表 list
 * @Date: 2019/12/11 12:47 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TempCloudDataCenterListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<TempCloudDataCenterVO> data;

    public TempCloudDataCenterListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public TempCloudDataCenterListResponse(List<TempCloudDataCenterVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public TempCloudDataCenterListResponse(ResultEnum resultEnum, List<TempCloudDataCenterVO> data) {
        super(resultEnum);
        this.data = data;
    }

}