package com.ecdata.cmp.iaas.entity.dto.response;

import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.common.dto.PageVO;
import com.ecdata.cmp.common.enums.ResultEnum;
import com.ecdata.cmp.iaas.entity.dto.IaasClusterVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @title: iaasCluster page response
 * @Author: shig
 * @description: 集群 分页响应对象
 * @Date: 2019/11/25 11:25 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IaasClusterPageResponse extends BaseResponse {
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private PageVO<IaasClusterVo> data;

    public IaasClusterPageResponse() {
        super(ResultEnum.DEFAULT_SUCCESS);
    }

    public IaasClusterPageResponse(PageVO<IaasClusterVo> data) {
        super(ResultEnum.DEFAULT_SUCCESS);
        this.data = data;
    }

    public IaasClusterPageResponse(ResultEnum resultEnum, PageVO<IaasClusterVo> data) {
        super(resultEnum);
        this.data = data;
    }
}
