package com.ecdata.cmp.iaas.entity.dto.vm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/21 15:52
 * @modified By：
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "云上系统统计对象裸金属", description = "云上系统统计对象裸金属")
public class BMGroupVO implements Serializable{

    @ApiModelProperty(value = "业务组id")
    private String id;

    @ApiModelProperty(value = "业务组")
    private String businessGroupName;

    @ApiModelProperty(value = "所属组织")
    private String projectName;

    @ApiModelProperty(value = "区域")
    private String azoneInfo;

    @ApiModelProperty(value = "数量")
    private String bmCount;

    @ApiModelProperty(value = "上云时间")
    private String createTime;

    @ApiModelProperty(value = "删除数")
    private String sumDeleted;

}
