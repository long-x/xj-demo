package com.ecdata.cmp.iaas.entity.transferCloud;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhaoYX
 * @since 2019/11/19 15:25,
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@TableName("proj_migration")
@ApiModel(value = "项目上云", description = "项目上云")
public class ProjMigration extends Model<ProjMigration> {
    private static final long serialVersionUID = -8855410262983818271L;
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("主管单位  新增之后下拉框中科院查看所有")
    private String supervisorName;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("预算单位")
    private String budgetName;

    @ApiModelProperty("申请时间")
    private Date applyTime;

    @ApiModelProperty("基础资源/状态：0.未立项 1.已立项 2.分配资源 3.安全策略 4.应用迁移 5.完成   从2开始4个都是已经分配的")
    private Integer status;

    @ApiModelProperty("创建人")
    private Long createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
