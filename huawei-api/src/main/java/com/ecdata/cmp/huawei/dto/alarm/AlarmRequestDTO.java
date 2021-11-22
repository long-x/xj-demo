package com.ecdata.cmp.huawei.dto.alarm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Alarm传入对象", description = "Alarm传入对象")
public class AlarmRequestDTO {
    @ApiModelProperty(value = "运营面Token")
    private String ocToken;

    @ApiModelProperty(value = "运维面Token")
    private String omToken;

    @ApiModelProperty(value = "待删除的告警id")
    private List<Long> awaitRemoveIds;
}
