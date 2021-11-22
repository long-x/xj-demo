package com.ecdata.cmp.iaas.entity.dto;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/21 16:36
 * @modified By：
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "裸金属统计结果", description = "裸金属统计结果")
public class BMVO extends Model<BMVO> {


    private static final long serialVersionUID = 508796474157324025L;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("型号名称")
    private String detailName;

    @ApiModelProperty("数量")
    private Long count;



}
