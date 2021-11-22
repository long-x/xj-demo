package com.ecdata.cmp.iaas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description:
 *
 * @author hhj
 * @create created in 11:18 2020/5/26
 */

@Data
@Accessors(chain = true)
@TableName("iaas_area_sum")
@ApiModel(value = "资源总数", description = "资源总数")
public class IaasAreaSum extends Model<IaasAreaSum> {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id; // id

    /**
     * 资源名称
     */
    @ApiModelProperty(value = "资源名称")
    private String serverName;

    /**
     * 所在分区
     */
    @ApiModelProperty(value = "所在分区")
    private String areaName;

    /**
     * 所在分区总数
     */
    @ApiModelProperty(value = "所在分区总数")
    private int areaSum;
}
