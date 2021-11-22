package com.ecdata.cmp.iaas.entity.dto.report;

import lombok.Data;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/5/11 11:26
 */
@Data
public class ApplyInfo {
    private Long applyId;
    private List<ConfigInfo> configInfoList;
}
