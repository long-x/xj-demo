package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * @author ：xuj
 * @date ：Created in 2020/5/31 11:06
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasBMListResponse extends BaseResponse {

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<BareMetalVO> data;

    public IaasBMListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasBMListResponse(List<BareMetalVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasBMListResponse(ResultEnum resultEnum, List<BareMetalVO> data) {
        super(resultEnum);
        this.data = data;
    }

}
