package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @title: ResourcePoolListResponse
 * @Author: shig
 * @description: 资源池 响应对象
 * @Date: 2019/11/12 10:38 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResourcePoolListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<ResourcePoolVO> data;

    public ResourcePoolListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResourcePoolListResponse(List<ResourcePoolVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResourcePoolListResponse(ResultEnum resultEnum, List<ResourcePoolVO> data) {
        super(resultEnum);
        this.data = data;
    }
}