package com.ecdata.cmp.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xuxinsheng
 * @since 2019-05-08
*/
@Data
@Accessors(chain = true)
@TableName("sys_user_project")
@ApiModel(value = "用户项目对象", description = "用户项目对象")
public class UserProject extends Model<UserProject> {

    private static final long serialVersionUID = -5233488968050232464L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** 用户id */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /** 项目id */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public UserProject() {
    }

    public UserProject(Long id, Long userId, Long projectId, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.projectId = projectId;
        this.createTime = createTime;
    }

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
