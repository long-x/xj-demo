package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.component.IaasComponentOpHistory;

public interface IComponentOpHistoryService extends IService<IaasComponentOpHistory> {

    void modifyUpdateRecord(Long id, Long userId);

}
