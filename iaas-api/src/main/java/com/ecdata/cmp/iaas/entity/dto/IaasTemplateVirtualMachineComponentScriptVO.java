package com.ecdata.cmp.iaas.entity.dto;

import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentOperationVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 描述:
 * 组件
 *
 * @author xxj
 * @create 2019-11-18 16:56
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "iaas模板虚拟机组件脚本表", description = "iaas模板虚拟机组件脚本表")
public class IaasTemplateVirtualMachineComponentScriptVO implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("脚本id")
    private Long scriptId;

    @ApiModelProperty("模板虚拟机组件id")
    private Long vmComponentId;

    @ApiModelProperty("关联iaas组件id")
    private Long componentId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private Boolean isDeleted;

    @ApiModelProperty("关联操作   非组件模块用，组件模块无视")
    private List<IaasComponentOperationVO> compOps;   //组件模块无视，其它模块使用

    @ApiModelProperty("前端对应id，与操作关联")//前端给的key  ops里面scriptid相等这关联，但是要生成雪花保存到id
    private String key;

}
