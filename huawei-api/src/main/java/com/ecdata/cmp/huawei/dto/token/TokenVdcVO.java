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
 * @author ：xuj
 * @date ：Created in 2020/3/27 10:16
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "vdc信息", description = "vdc信息")
public class TokenVdcVO  implements Serializable{


    private static final long serialVersionUID = 3224026896517621657L;



    /**
     * 项目表id
     */
    @ApiModelProperty(value = "用户名")
    private String id;


    /**
     * 区域名
     */
    @ApiModelProperty(value = "区域名")
    private String domainName;









}
