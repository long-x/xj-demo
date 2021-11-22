package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.CloudServersFlavorsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/2 11:13
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CloudServersFlavorsListResponce extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<CloudServersFlavorsVO> data;

    public CloudServersFlavorsListResponce() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public CloudServersFlavorsListResponce(List<CloudServersFlavorsVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public CloudServersFlavorsListResponce(ResultEnum resultEnum, List<CloudServersFlavorsVO> data) {
        super(resultEnum);
        this.data = data;
    }
}