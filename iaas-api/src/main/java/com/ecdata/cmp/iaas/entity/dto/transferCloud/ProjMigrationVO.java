package com.ecdata.cmp.iaas.entity.dto.transferCloud;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2019/11/19 15:27,
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "项目上云", description = "项目上云")
public class ProjMigrationVO implements Serializable {
    private static final long serialVersionUID = -8753514526684940008L;
    @ApiModelProperty("主键")
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

    @ApiModelProperty("create_user")
    private Long createUser;

    @ApiModelProperty("create_time")
    private Date createTime;

    @ApiModelProperty("update_user")
    private Long updateUser;

    @ApiModelProperty("update_time")
    private Date updateTime;

}
