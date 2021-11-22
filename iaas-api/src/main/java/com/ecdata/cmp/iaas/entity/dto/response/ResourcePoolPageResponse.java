package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ResourcePoolVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: ResourcePoolPageResponse
 * @Author: shig
 * @description: 资源池 分页响应对象
 * @Date: 2019/11/18 11:25 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResourcePoolPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<ResourcePoolVO> data;

    public ResourcePoolPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ResourcePoolPageResponse(PageVO<ResourcePoolVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ResourcePoolPageResponse(ResultEnum resultEnum, PageVO<ResourcePoolVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
