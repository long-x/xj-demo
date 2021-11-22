package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.transferCloud.ProjMigrationVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/19 10:44,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProjMigrationListResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private List<ProjMigrationVO> data;

    public ProjMigrationListResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ProjMigrationListResponse(List<ProjMigrationVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ProjMigrationListResponse(ResultEnum resultEnum, List<ProjMigrationVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
