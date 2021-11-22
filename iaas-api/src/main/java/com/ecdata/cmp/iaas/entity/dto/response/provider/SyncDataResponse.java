package com.ecdata.cmp.iaas.entity.dto.response.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述:同步数据返回数据
 *
 * @author xxj
 * @create 2019-11-29 14:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncDataResponse {
    private String code;
    private String message;
    private VSphereResponse data;

}
