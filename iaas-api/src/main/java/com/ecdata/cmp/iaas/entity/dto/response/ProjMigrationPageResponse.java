package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.transferCloud.ProjMigrationVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ZhaoYX
 * @since 2019/11/19 14:12,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProjMigrationPageResponse  extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回数据")
    private PageVO<ProjMigrationVO> data;

    public ProjMigrationPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ProjMigrationPageResponse(PageVO<ProjMigrationVO> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ProjMigrationPageResponse(ResultEnum resultEnum, PageVO<ProjMigrationVO> data) {
        super(resultEnum);
        this.data = data;
    }
}
