package com.ecdata.cmp.iaas.entity.dto.response.catalog;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
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
public class IaasCatalogResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private Long catalogId;

    public IaasCatalogResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasCatalogResponse(Long catalogId) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.catalogId = catalogId;
    }

    public IaasCatalogResponse(ResultEnum resultEnum, Long catalogId) {
        super(resultEnum);
        this.catalogId = catalogId;
    }
}
