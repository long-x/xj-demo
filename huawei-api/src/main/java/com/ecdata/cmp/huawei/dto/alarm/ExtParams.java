package com.ecdata.cmp.huawei.dto.alarm;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ExtParams", description = "ExtParams")
public class ExtParams implements Serializable {
    private static final long serialVersionUID = 5936764352267150447L;

    private String toggleStatus;

    private String toggleCount;

    private String clearArriveUtc;

    private String newlyVersion;

    private String aggrCount;
}
