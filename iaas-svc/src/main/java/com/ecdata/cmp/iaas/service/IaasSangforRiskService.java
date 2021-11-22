package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import com.ecdata.cmp.iaas.entity.dto.response.sangfor.MinTimeObject;
import com.ecdata.cmp.iaas.entity.sangfor.SangforSecurityRisk;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import com.ecdata.cmp.user.dto.SysNotificationVO;

import java.util.List;

public interface IaasSangforRiskService extends IService<SangforSecurityRisk> {
    boolean saveBatchRisk(List<SangforSecurityRisk> risks);

    MinTimeObject getMinTime();

    SysNotificationVO addToNotify(SangforSecurityRisk risk);

    void addBatchToNotify(List<SangforSecurityRisk> securityRisks);

    void updateToNotify(SangforSecurityRiskVO riskVO, Long userId);

    List<SysNotificationReceiverVO> fetchReceiveIdByRisk(Long riskId);
}
