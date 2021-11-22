package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.component.IaasComponentParamHistory;

public interface IComponentParamHistoryService extends IService<IaasComponentParamHistory> {

    void modifyUpdateRecord(Long id, Long userId);

}
