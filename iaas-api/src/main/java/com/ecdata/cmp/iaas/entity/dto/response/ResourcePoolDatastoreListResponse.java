package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolDatastoreVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @title: ResourcePoolDatastoreListResponse
 * @Author: shig
 * @description: 资源池存储 响应对象
 * @Date: 2019/11/12 10:38 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResourcePoolDatastoreListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<ResourcePoolDatastoreVO> data;

    public ResourcePoolDatastoreListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResourcePoolDatastoreListResponse(List<ResourcePoolDatastoreVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResourcePoolDatastoreListResponse(ResultEnum resultEnum, List<ResourcePoolDatastoreVO> data) {
        super(resultEnum);
        this.data = data;
    }
}