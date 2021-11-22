package com.ecdata.cmp.iaas.entity.dto.sangfor;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhaoYX
 * @since 2020/4/21 11:13,
 */
@Data
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "深信服安全风险", description = "深信服安全风险")
public class SangforSecurityRiskVO extends Model<SangforSecurityRiskVO> {

    private static final long serialVersionUID = -6249071314924544874L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty("服务器ip")
    private String ip;

    @ApiModelProperty("所属分支")
    private String branchName;

    @ApiModelProperty("所属分支id")
    private String branchId;

    @ApiModelProperty("业务名或用户组名称")
    private String groupName;

    @ApiModelProperty("业务或用户组 id")
    private String groupId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("最新发生时间时间戳")
    private Date lastTime;

    @ApiModelProperty("事件标识")
    private String eventKey;

    @ApiModelProperty("事件产生的日期")
    private Long recordDate;

    @ApiModelProperty("安全等级")
    private Integer severityLevel;

    @ApiModelProperty("ip 属性")
    private Integer type;

    @ApiModelProperty("安全事件:规则 id")
    private String ruleId;

    @ApiModelProperty("安全事件:失陷确定性")
    private Integer priority;

    @ApiModelProperty("安全事件:威胁性等级")
    private Integer reliability;

    @ApiModelProperty("原始json数据")
    private String metaData;

    @ApiModelProperty("1.风险业务 2.风险终端 3.风险事件 4.弱密码 5.明文传输 6.漏洞")
    private Integer riskType;

    @ApiModelProperty("处理状态 0:未处理 1:已处理")
    private Integer dealStatus;

    @ApiModelProperty("处理时间")
    private Date dealTime;

    @ApiModelProperty("处置人id")
    private Long dealUser;

    @ApiModelProperty("处置备注")
    private String dealComment;

    @ApiModelProperty("告警级别 1：低风险 2：高风险 3：危险")
    private Integer level;

    /** 指定主键 */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
