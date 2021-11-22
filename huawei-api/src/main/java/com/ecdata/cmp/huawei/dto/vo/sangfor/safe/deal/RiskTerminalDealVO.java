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
@ApiModel(value = "深信服-风险终端-处置", description = "深信服-风险终端-处置")
public class RiskTerminalDealVO {

    private String ip;

    private Integer type;

    private String groupId;

    private String branchId;

    private Long recordDate;

    /**
     * 安全等级
     */
    private Integer severityLevel;

    /**
     * 处理状态
     * 0：未处理 1：已处理
     */
    private Integer dealStatus;

    /**
     * 处置备注，自填
     */
    private String deal_comment;

    private Integer multi_deal_status;

    private String token;
}
