package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.alarm.Record;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AlarmListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<Record> data;

    public AlarmListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public AlarmListResponse(List<Record> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public AlarmListResponse(ResultEnum resultEnum, List<Record> data) {
        super(resultEnum);
        this.data = data;
    }
}
