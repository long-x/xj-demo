package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.SecurityGroupsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/4 17:13
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SecurityGroupsListResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<SecurityGroupsVO> data;

    public SecurityGroupsListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public SecurityGroupsListResponse(List<SecurityGroupsVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public SecurityGroupsListResponse(ResultEnum resultEnum, List<SecurityGroupsVO> data) {
        super(resultEnum);
        this.data = data;
    }

}
