package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.VdcsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/9 15:57
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VdcsListResponse extends BaseResponse {

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<VdcsVO> data;

    public VdcsListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public VdcsListResponse(List<VdcsVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public VdcsListResponse(ResultEnum resultEnum, List<VdcsVO> data) {
        super(resultEnum);
        this.data = data;
    }


}
