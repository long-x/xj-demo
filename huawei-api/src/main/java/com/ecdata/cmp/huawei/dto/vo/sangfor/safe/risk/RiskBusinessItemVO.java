package com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "深信服-风险业务-Item", description = "深信服-风险业务-Item")
public class RiskBusinessItemVO {

    /**
     * 影响范围
     * Eg. [1,2,3] 1:只影响自身 2:影响内网 3:影响外网
     */
    private List<Integer> affectType;

    /**
     * 责任人
     */
    private String authUser;

    /**
     * 分支名
     */
    private String branchName;

    /**
     * 处理状态
     * 0：未处理 1：已处理
     */
    private Integer dealStatus;

    /**
     * 失陷等级
     * 0：正常 1：低可疑 2：高可疑 3：已失陷
     */
    private Integer fallLevel;

    /**
     * 业务名或用户组名称
     */
    private String groupName;

    /**
     * 业务或用户组 id
     */
    private String groupId;

    /**
     * 标签
     * Eg. [“标签 1”，”标签 2”...]
     */
    private List<String> tag;

    /**
     * 终端/服务器 ip
     * Eg."3.1.1.7"
     */
    private String ip;

    /**
     * 最新访问时间
     */
    private Long lastTime;

    /**
     * 安全等级
     */
    private Integer severityLevel;

    /**
     * IP 属性
     * 0：全部 1：业务/已配置到某 业务2：没有归类（未配置 到业务的服务器） 3：终端组/属于某终 端组4：未配置到终端组的 终端5：互联网
     */
    private Integer type;

    /**
     * 是否加入白名单
     * 0：未加入 1：加入
     */
    private Integer isWhite;

    /**
     * 处理时间（未处理为空字 符串）
     */
    private Long dealTime;

    /**
     * 分支 ID
     */
    private String branchId;

    private Long recordDate;

}
