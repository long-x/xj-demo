package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
//import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/11/25 11:06
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasBareMetalPageResponse extends BaseResponse {

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<BareMetalVO> data;

    public IaasBareMetalPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasBareMetalPageResponse(PageVO<BareMetalVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasBareMetalPageResponse(ResultEnum resultEnum, PageVO<BareMetalVO> data) {
        super(resultEnum);
        this.data = data;
    }

}
