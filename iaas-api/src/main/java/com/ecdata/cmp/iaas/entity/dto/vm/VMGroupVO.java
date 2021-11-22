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
@ApiModel(value = "云上系统统计对象1", description = "云上系统统计对象1")
@ExcelTarget("VMGroupVO")
public class VMGroupVO implements Serializable{

    @ApiModelProperty(value = "业务组id")
    private String id;

    @Excel(name = "业务组", orderNum = "1", width = 25, needMerge = true)
    @ApiModelProperty(value = "业务组")
    private String businessGroupName;

    @Excel(name = "所属组织", orderNum = "1", width = 25, needMerge = true)
    @ApiModelProperty(value = "所属组织")
    private String projectName;

    @ExcelCollection(name = "具体计数", orderNum = "4")
    @ApiModelProperty(value = "具体计数")
    private List<VMCountVO> children;

}
