package com.ecdata.cmp.iaas.entity.dto.component;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/13 14:04,
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(value = "组件脚本历史", description = "组件脚本历史")
public class IaasComponentScriptHistoryVO implements Serializable {
    private static final long serialVersionUID = -8975693750536292655L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 组件脚本id
     */
    @ApiModelProperty("组件脚本id")
    private Long componentScriptId;

    /**
     * 组件历史id
     */
    @ApiModelProperty("组件历史id")
    private Long componentHistoryId;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private Long tenantId;

    /**
     * 组件id
     */
    @ApiModelProperty("组件id")
    private Long componentId;

    /**
     * 脚本名
     */
    @ApiModelProperty("脚本名")
    private String scriptName;

    /**
     * 脚本类型(sh/powershell/python/ruby/groovy/batchfile/text/xml)
     */
    @ApiModelProperty("脚本类型(sh/powershell/python/ruby/groovy/batchfile/text/xml)")
    private String scriptType;

    /**
     * 脚本内容
     */
    @ApiModelProperty("脚本内容")
    private String content;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 插入历史表时间
     */
    @ApiModelProperty("插入历史表时间")
    private Date historyTime;

    /**
     * 是否已删除(0表示未删除，1表示已正常)
     */
    @ApiModelProperty("是否已删除(0表示未删除，1表示已正常)")
    private Boolean isDeleted;

//    @ApiModelProperty("关联操作")
//    private List<IaasComponentOpHistoryVO> opsHis;

}
