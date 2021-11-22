package com.ecdata.cmp.iaas.entity.dto.response.catalog;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.catalog.IaasCatalogVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-18 15:35
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasCatalogPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<IaasCatalogVO> data;

    public IaasCatalogPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasCatalogPageResponse(PageVO<IaasCatalogVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasCatalogPageResponse(ResultEnum resultEnum, PageVO<IaasCatalogVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
