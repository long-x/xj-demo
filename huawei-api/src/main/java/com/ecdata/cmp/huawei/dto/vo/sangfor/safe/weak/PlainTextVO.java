package com.ecdata.cmp.huawei.dto.vo.sangfor.safe.weak;


import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.DeviceInfoVO;
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
@ApiModel(value = "深信服-脆弱性-明文传输信息", description = "深信服-脆弱性-明文传输信息")
public class PlainTextVO {

    private List<PlainTextItemVO> items;

    private DeviceInfoVO deviceInfo;

    private long startTime;

    private long endTime;

    private int maxCount;
}
