package com.ecdata.cmp.iaas.entity.dto.response.provider;

import lombok.Data;

/**
 * 描述:主机与网络关系
 *
 * @author xxj
 * @create 2019-11-14 13:47
 */
@Data
public class NetworkResponse {

    /**
     * 主机id
     */
    private String hostId;
    /**
     * 网络id
     */
    private String networkId;

}
