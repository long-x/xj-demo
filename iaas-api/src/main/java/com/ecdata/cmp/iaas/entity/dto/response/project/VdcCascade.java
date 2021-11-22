package com.ecdata.cmp.iaas.entity.dto.response.project;

import lombok.Data;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/1 17:19
 * @modified By：
 */
@Data
public class VdcCascade {

    private Long id;
    private String name;
    private List<ProjectCascade> items;

}
