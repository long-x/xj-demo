package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.PhysicalHostVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 15:32
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PhysicalHostListResponse  extends BaseResponse {


    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<PhysicalHostVO> data;

    public PhysicalHostListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public PhysicalHostListResponse(List<PhysicalHostVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public PhysicalHostListResponse(ResultEnum resultEnum, List<PhysicalHostVO> data) {
        super(resultEnum);
        this.data = data;
    }

}
