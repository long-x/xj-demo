package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.statistics.StatisticsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/6/15 17:34
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StatisticsVOListResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private List<StatisticsVO> data;

    public StatisticsVOListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public StatisticsVOListResponse(List<StatisticsVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public StatisticsVOListResponse(ResultEnum resultEnum, List<StatisticsVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
