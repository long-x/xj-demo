package com.ecdata.cmp.iaas.entity.dto.workbench;

import lombok.Data;

import java.util.List;

@Data
public class BusinessGroupUserVO {
    private int sum;

    private List<BusinessGroupUserDataVO> data;
}
