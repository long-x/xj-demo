package com.ecdata.cmp.user.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.user.dto.UserDepartmentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author xuxinsheng
 * @since 2019-09-11
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserDepartmentListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<UserDepartmentVO> data;

    public UserDepartmentListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public UserDepartmentListResponse(List<UserDepartmentVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public UserDepartmentListResponse(ResultEnum resultEnum, List<UserDepartmentVO> data) {
        super(resultEnum);
        this.data = data;
    }

}
