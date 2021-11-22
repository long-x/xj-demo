package com.ecdata.cmp.iaas.entity.dto.response.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述:VSphere对接py返回市局
 *
 * @author xxj
 * @create 2019-11-14 13:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VSphereResponse {
    /**
     * 区域
     */
    List<AreaResponse> areaData;
}
