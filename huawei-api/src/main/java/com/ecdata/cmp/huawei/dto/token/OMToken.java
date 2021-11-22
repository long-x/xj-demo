package com.ecdata.cmp.huawei.dto.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ty
 * @description
 * @date 2019/12/2 16:58
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@ApiModel(value = "运维面返回的Token对象", description = "运维面返回的Token对象")
@NoArgsConstructor
@AllArgsConstructor
public class OMToken implements Serializable {
    private static final long serialVersionUID = 1880251798173837957L;

    /**
     * token值
     */
    @ApiModelProperty(value = "token值")
    private String accessSession;

    /**
     * roaRand
     */
    @ApiModelProperty(value = "roaRand")
    private String roaRand;

    /**
     * 超时时间
     */
    @ApiModelProperty(value = "超时时间")
    private String expires;

    /**
     * roaRand
     */
    @ApiModelProperty(value = "additionalInfo")
    private Object additionalInfo;
}


