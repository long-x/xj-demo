package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.component.IaasComponentScriptHistory;

public interface IComponentScriptHistoryService extends IService<IaasComponentScriptHistory> {

    void modifyUpdateRecord(Long id, Long userId);
}
