package com.ecdata.cmp.huawei.dto.vo.sangfor.safe.deal;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "深信服-风险事件-处置", description = "深信服-风险事件-处置")
public class RiskEventDealVO {

    private String ip;

    private Integer type;

    private String groupId;

    private String branchId;

    private Long recordDate;

    /**
     * 规则 id(用来找到对应 举证信息格式)
     */
    private String ruleId;

    /**
     * 失陷确定性
     * 1：低可疑 2：高可疑 3： 已失陷
     */
    private Integer priority;

    /**
     * 威胁性等级
     * 1：低威胁 2：中威胁 3： 高威胁
     */
    private Integer reliability;

    /**
     * 用于查找日志
     * Eg:”coengine|104008 ”
     */
    private String eventKey;

    private Integer dealStatus;

    /**
     * 处置备注，自填
     */
    private String deal_comment;

    private Integer multi_deal_status;

    private Boolean is_handle_host;

    private String token;

}
