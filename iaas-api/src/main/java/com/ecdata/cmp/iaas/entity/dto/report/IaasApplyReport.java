package com.ecdata.cmp.iaas.entity.dto.report;

import lombok.Data;

import java.util.List;

/**
 * @author xuxiaojian
 * @date 2020/5/11 11:00
 */
@Data
public class IaasApplyReport {
    private Long businessId;
    private String businessGroupName;
    private Long parentId;
    private List<ApplyInfo> applyInfoList;

    //树状形式组装
    private List<IaasApplyReport> children;

    public int childrenNum() {
        if (this == null || this.children == null) {
            return 0;
        }
        return this.getChildren().size();
    }
}
