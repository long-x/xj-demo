package com.ecdata.cmp.iaas.entity.dto.response.apply;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xuxiaojian
 * @date 2020/3/9 10:40
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AuditResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private Long id;

    public AuditResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public AuditResponse(Long id) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.id = id;
    }

    public AuditResponse(ResultEnum resultEnum, Long id) {
        super(resultEnum);
        this.id = id;
    }
}
