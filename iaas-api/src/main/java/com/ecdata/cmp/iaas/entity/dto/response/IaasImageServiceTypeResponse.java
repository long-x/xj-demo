package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author ：xuj
 * @date ：Created in 2020/3/30 10:21
 * @modified By：
 */


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasImageServiceTypeResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private Map<String, List<String>> data;

    public IaasImageServiceTypeResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasImageServiceTypeResponse(Map<String, List<String>> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

}