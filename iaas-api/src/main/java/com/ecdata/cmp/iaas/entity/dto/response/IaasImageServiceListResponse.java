package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ImageServiceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/30 10:21
 * @modified By：
 */


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasImageServiceListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<ImageServiceVO> data;

    public IaasImageServiceListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasImageServiceListResponse(List<ImageServiceVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasImageServiceListResponse(ResultEnum resultEnum, List<ImageServiceVO> data) {
        super(resultEnum);
        this.data = data;
    }

}