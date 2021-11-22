package com.ecdata.cmp.iaas.entity.dto.response.provider.cascade;

import lombok.Data;

import java.util.List;

/**
 * @author xxj
 * @date 2020/2/14 15:54
 */
@Data
public class ClusterCascade {
    private Long id;
    private String name;
    private List<HostCascade> items;
}
