package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.component.IaasComponentParamHistory;
import com.ecdata.cmp.iaas.mapper.component.ComponentParamHistoryMapper;
import com.ecdata.cmp.iaas.service.IComponentParamHistoryService;
import org.springframework.stereotype.Service;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:02,
 */
@Service
public class ComponentParamHistoryServiceImpl extends ServiceImpl<ComponentParamHistoryMapper, IaasComponentParamHistory>
        implements IComponentParamHistoryService {

    @Override
    public void modifyUpdateRecord(Long id, Long userId) {
        baseMapper.modifyUpdateRecord(id,userId);
    }
}
