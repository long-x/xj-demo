package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolDatastoreVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: ResourcePoolPageResponse
 * @Author: shig
 * @description: 资源池存储 分页响应对象
 * @Date: 2019/11/18 11:25 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResourcePoolDatastorePageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<ResourcePoolDatastoreVO> data;

    public ResourcePoolDatastorePageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResourcePoolDatastorePageResponse(PageVO<ResourcePoolDatastoreVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResourcePoolDatastorePageResponse(ResultEnum resultEnum, PageVO<ResourcePoolDatastoreVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
