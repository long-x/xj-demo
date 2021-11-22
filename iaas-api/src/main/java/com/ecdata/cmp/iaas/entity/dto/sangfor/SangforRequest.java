package com.ecdata.cmp.iaas.entity.dto.sangfor;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ZhaoYX
 * @since 2020/4/27 14:49,
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "深信服告警请求", description = "深信服告警请求")
public class SangforRequest  implements Serializable {

    private static final long serialVersionUID = 4767128754496154275L;

    @ApiModelProperty(value = "接收id--receiveId")
    private Long receiveId;

    @ApiModelProperty(value = "风险vo")
    private SangforSecurityRiskVO vo;

}
