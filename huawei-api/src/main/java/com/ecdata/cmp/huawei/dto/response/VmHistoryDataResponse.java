package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.VmHistoryDataVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/11 20:38
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VmHistoryDataResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private VmHistoryDataVO data;

    public VmHistoryDataResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public VmHistoryDataResponse(VmHistoryDataVO data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public VmHistoryDataResponse(ResultEnum resultEnum, VmHistoryDataVO data) {
        super(resultEnum);
        this.data = data;
    }

}
