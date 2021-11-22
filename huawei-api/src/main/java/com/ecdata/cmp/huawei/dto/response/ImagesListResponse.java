package com.ecdata.cmp.huawei.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.huawei.dto.vo.ImagesVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2019/12/3 16:42
 * @modified By：
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ImagesListResponse extends BaseResponse {

    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<ImagesVO> data;

    public ImagesListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ImagesListResponse(List<ImagesVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ImagesListResponse(ResultEnum resultEnum, List<ImagesVO> data) {
        super(resultEnum);
        this.data = data;
    }

}
