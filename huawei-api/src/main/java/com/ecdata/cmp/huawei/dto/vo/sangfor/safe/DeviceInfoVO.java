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
@ApiModel(value = "深信服-风险业务-设备信息", description = "深信服-风险业务-设备信息")
public class DeviceInfoVO {
    /**
     * 数据来源
     * 默认为“SIP”
     */
    private String source;

    private String apikey;

    /**
     * 设备 id
     * 如为：“F99EAAF0”
     */
    private String deviceId;

    /**
     *  设备版本号
     *  如 为 ： "SIS3.0.12.20180919110523 Build20180919"
     */
    private String deviceVersion;
}
