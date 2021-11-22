package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ImageServiceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @title: ImageServiceVOPageResponse
 * @Author: shig
 * @description: 镜像服务分页对象
 * @Date: 2019/12/12 4:08 下午
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ImageServiceVOPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<ImageServiceVO> data;

    public ImageServiceVOPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ImageServiceVOPageResponse(PageVO<ImageServiceVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ImageServiceVOPageResponse(ResultEnum resultEnum, PageVO<ImageServiceVO> data) {
        super(resultEnum);
        this.data = data;
    }
}