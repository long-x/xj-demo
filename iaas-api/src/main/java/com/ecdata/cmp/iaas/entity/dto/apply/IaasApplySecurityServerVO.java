package com.ecdata.cmp.iaas.entity.dto.apply;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 安全服务信息表
 *
 * @author xxj
 * @email sunlightcs@gmail.com
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "安全服务信息表", description = "安全服务信息表")
public class IaasApplySecurityServerVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    /**
     * 申请id
     */
    @ApiModelProperty(value = "申请id")
    private Long applyId;

    /**
     * 配置id
     */
    @ApiModelProperty(value = "配置id")
    private Long configId;

    /**
     * 所在区域
     */
    @ApiModelProperty(value = "所在区域")
    private Long areaId;
    /**
     * 操作类型：1.新增；2.变更;3.删除
     */
    @ApiModelProperty(value = "操作类型：1.新增；2.变更;3.删除")
    private String operationType;
    /**
     * 操作类型：1.虚拟机；2.安全资源
     */
    @ApiModelProperty(value = "操作类型：1.虚拟机；2.安全资源")
    private String type;
    /**
     * 服务器的名称
     */
    @ApiModelProperty(value = "服务器的名称")
    private String serverName;
    /**
     * 存储资源类型：1.系统盘 2.存储盘
     */
    @ApiModelProperty(value = "存储资源类型：1.系统盘 2.存储盘")
    private String storeResourceType;
    /**
     * eip
     */
    @ApiModelProperty(value = "eip")
    private String eip;
    /**
     * 安全组件
     */
    @ApiModelProperty(value = "安全组件")
    private String securityAssembly;
    /**
     * 其他要求
     */
    @ApiModelProperty(value = "其他要求")
    private String otherClaim;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 是否已删除(0表示未删除,1表示已删除)
     */
    @ApiModelProperty(value = "是否已删除(0表示未删除,1表示已删除)")
    private Integer isDeleted;

}
