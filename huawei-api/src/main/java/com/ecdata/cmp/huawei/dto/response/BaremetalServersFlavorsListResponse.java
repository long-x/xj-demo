package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.BaremetalServersFlavorsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/1 16:08
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaremetalServersFlavorsListResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<BaremetalServersFlavorsVO> data;

    public BaremetalServersFlavorsListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public BaremetalServersFlavorsListResponse(List<BaremetalServersFlavorsVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public BaremetalServersFlavorsListResponse(ResultEnum resultEnum, List<BaremetalServersFlavorsVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
