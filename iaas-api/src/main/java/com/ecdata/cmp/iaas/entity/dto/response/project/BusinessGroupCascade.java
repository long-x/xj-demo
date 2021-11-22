package com.ecdata.cmp.iaas.entity.dto.response.project;

import lombok.Data;

import java.util.List;

/**
 * @author ：xuj
 * @date ：Created in 2020/4/1 17:20
 * @modified By：
 */
@Data
public class BusinessGroupCascade {

    private Long id;
    private String name;
    private String isApp;
    private String pdKey;
    private String pdName;
    private List<VdcCascade> items;

}
