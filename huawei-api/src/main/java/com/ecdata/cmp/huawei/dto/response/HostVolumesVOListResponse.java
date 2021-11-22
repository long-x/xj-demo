package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.HostVolumesVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 11:01
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HostVolumesVOListResponse  extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<HostVolumesVO> data;

    public HostVolumesVOListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public HostVolumesVOListResponse(List<HostVolumesVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public HostVolumesVOListResponse(ResultEnum resultEnum, List<HostVolumesVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
