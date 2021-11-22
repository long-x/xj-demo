package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ImageServiceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: IaasImageServiceResponse response
 * @Author: shig
 * @description: 镜像服务 响应对象
 * @Date: 2019/11/12 11:17 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasImageServiceResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private ImageServiceVO data;

    public IaasImageServiceResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasImageServiceResponse(ImageServiceVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasImageServiceResponse(ResultEnum resultEnum, ImageServiceVO data) {
        super(resultEnum);
        this.data = data;
    }

}