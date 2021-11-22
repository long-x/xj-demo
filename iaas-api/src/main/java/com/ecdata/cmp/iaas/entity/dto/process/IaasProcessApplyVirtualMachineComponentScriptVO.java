package com.ecdata.cmp.iaas.entity.dto.process;

import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentOperationVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author xxj
 * @create 2019-11-22 16:59
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas流程申请虚拟机组件脚本表", description = "iaas流程申请虚拟机组件脚本表")
public class IaasProcessApplyVirtualMachineComponentScriptVO {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("脚本id")
    private Long scriptId;

    @ApiModelProperty("流程申请虚拟机组件id")
    private Long vmComponentId;

    @ApiModelProperty("脚本名")
    private String scriptName;

    @ApiModelProperty("脚本类型(sh/powershell/python/ruby/groovy/batchfile/text/xml)")
    private String scriptType;

    @ApiModelProperty("脚本内容")
    private String content;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private Integer isDeleted;

    @ApiModelProperty("关联操作   非组件模块用，组件模块无视")
    private List<IaasComponentOperationVO> compOps;   //组件模块无视，其它模块使用

    @ApiModelProperty("前端对应id，与操作关联")//前端给的key  ops里面scriptid相等这关联，但是要生成雪花保存到id
    private String key;
}
