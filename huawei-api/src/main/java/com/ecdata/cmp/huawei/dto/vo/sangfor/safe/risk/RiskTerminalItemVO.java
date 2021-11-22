package com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "深信服-风险终端-Item", description = "深信服-风险终端-Item")
public class RiskTerminalItemVO extends RiskBusinessItemVO {

    /**
     * 主机名
     */
    private String hostName;

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }
}
