package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.RegionsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/9 15:08
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RegionsListResponse extends BaseResponse {


    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<RegionsVO> data;

    public RegionsListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public RegionsListResponse(List<RegionsVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public RegionsListResponse(ResultEnum resultEnum, List<RegionsVO> data) {
        super(resultEnum);
        this.data = data;
    }

}
