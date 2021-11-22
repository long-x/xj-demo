package com.ecdata.cmp.iaas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：xuj
 * @date ：Created in 2020/5/29 16:13
 * @modified By：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("iaas_timed_task")
@ApiModel(value = "定时开关机表", description = "定时开关机表")
public class IaasTimedTask extends Model<IaasTimedTask> {


    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("租户id")
    private Long tenantId;

    @ApiModelProperty("业务组id")
    private Long businessGroupId;

    @ApiModelProperty("业务组名称")
    private String businessGroupName;

    @ApiModelProperty("服务器类型：1.虚拟机；2.裸金属")
    private String serveType;

    @ApiModelProperty("服务器id")
    private String serveId;

    @ApiModelProperty("服务器名称")
    private String serveName;

    @ApiModelProperty("项目id")
    private Long projectId;

    @ApiModelProperty("关机时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date onTime;

    @ApiModelProperty("开机时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date offTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("是否已删除(0表示未删除，1表示已删除)")
    private boolean isDeleted;



    /**
     * 指定主键
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
