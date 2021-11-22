package com.ecdata.cmp.huawei.dto.vo.sangfor.safe;

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
@ApiModel(value = "深信服-安全", description = "深信服-安全")
public class SangforSafeRequestVO {

    private long startTime;

    private long endTime;

    private int maxCount;

    private int pageNo;

    private int pageSize;
}
