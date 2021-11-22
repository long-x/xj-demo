package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolDatastoreVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: ResourcePoolAsResponse
 * @Author: shig
 * @description: 资源池存储 响应对象
 * @Date: 2019/11/18 11:17 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResourcePoolDatastoreAsResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private ResourcePoolDatastoreVO data;

    public ResourcePoolDatastoreAsResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResourcePoolDatastoreAsResponse(ResourcePoolDatastoreVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResourcePoolDatastoreAsResponse(ResultEnum resultEnum, ResourcePoolDatastoreVO data) {
        super(resultEnum);
        this.data = data;
    }

}