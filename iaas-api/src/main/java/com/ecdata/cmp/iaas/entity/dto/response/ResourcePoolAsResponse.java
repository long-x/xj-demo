package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: ResourcePoolAsResponse
 * @Author: shig
 * @description: 资源池 响应对象
 * @Date: 2019/11/18 11:17 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResourcePoolAsResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private ResourcePoolVO data;

    public ResourcePoolAsResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResourcePoolAsResponse(ResourcePoolVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResourcePoolAsResponse(ResultEnum resultEnum, ResourcePoolVO data) {
        super(resultEnum);
        this.data = data;
    }

}