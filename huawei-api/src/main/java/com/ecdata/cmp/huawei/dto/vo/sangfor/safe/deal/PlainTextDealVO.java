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
@ApiModel(value = "深信服-明文传输-处置", description = "深信服-明文传输-处置")
public class PlainTextDealVO {

    private String ip;

    private String branchId;

    private String eventKey;

    private String deal_comment;

    private String token;
}
