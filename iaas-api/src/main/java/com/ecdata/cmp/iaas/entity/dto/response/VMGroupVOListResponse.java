package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.vm.VMGroupVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/21 17:11
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VMGroupVOListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<VMGroupVO> data;

    public VMGroupVOListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public VMGroupVOListResponse(List<VMGroupVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public VMGroupVOListResponse(ResultEnum resultEnum, List<VMGroupVO> data) {
        super(resultEnum);
        this.data = data;
    }
}