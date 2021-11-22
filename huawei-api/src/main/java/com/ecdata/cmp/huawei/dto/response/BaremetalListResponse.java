package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.BareMetalVO;
//import com.ecdata.cmp.huawei.dto.vo.BareMetalVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * @author ：xuj
 * @date ：Created in 2020/4/24 13:31
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaremetalListResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<BareMetalVO> data;

    public BaremetalListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public BaremetalListResponse(List<BareMetalVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public BaremetalListResponse(ResultEnum resultEnum, List<BareMetalVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
