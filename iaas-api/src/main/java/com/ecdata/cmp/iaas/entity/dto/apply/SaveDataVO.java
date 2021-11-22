package com.ecdata.cmp.iaas.entity.dto.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/10/22 9:35
 * @modified By：
 */
@Data
public class SaveDataVO {

    @ApiModelProperty(value = "工单编号")
    private Long applyId;

    @ApiModelProperty(value = "申请 虚拟机/裸金属/安全资源的id")
    private List<ApplyAreaUpdateVO> areaVOList;

    @ApiModelProperty(value = "activiti回填id")
    private String  processId;

}
