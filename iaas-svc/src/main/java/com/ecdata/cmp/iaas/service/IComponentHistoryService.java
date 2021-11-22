package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.component.IaasComponentHistory;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentHistoryVO;
import com.ecdata.cmp.iaas.entity.dto.component.IaasComponentVO;

import java.util.List;

public interface IComponentHistoryService extends IService<IaasComponentHistory> {

    void modifyUpdateRecord(Long id, Long userId);

    List<IaasComponentHistoryVO> qryVersion(Long id);

    IaasComponentHistoryVO qryCompHisInfo(Long compHisId);

    IaasComponentHistoryVO qryUnionHistory(Long compHisId);

    void rollBack(IaasComponentHistoryVO compHisVo,IaasComponentVO icvo);


}
