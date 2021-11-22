package com.ecdata.cmp.iaas.entity.dto.response.provider.cascade;

import lombok.Data;

import java.util.List;

/**
 * @author xxj
 * @date 2020/2/14 15:54
 */
@Data
public class AreaCascade {
    private Long id;
    private String name;
    private List<ClusterCascade> items;
}
