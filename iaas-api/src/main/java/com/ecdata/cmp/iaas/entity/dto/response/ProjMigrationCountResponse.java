package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.ProjectVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/11/20 10:18,
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProjMigrationCountResponse extends BaseResponse {
    /** 返回数据 */
    @ApiModelProperty(value = "返回统计数据")
    private List<Map<String,Object>> data;

    public ProjMigrationCountResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public ProjMigrationCountResponse(List<Map<String,Object>> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public ProjMigrationCountResponse(ResultEnum resultEnum, List<Map<String,Object>> data) {
        super(resultEnum);
        this.data = data;
    }

}
