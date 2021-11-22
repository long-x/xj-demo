package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.component.IaasComponentOpHistory;
import com.ecdata.cmp.iaas.mapper.component.ComponentOpHistoryMapper;
import com.ecdata.cmp.iaas.service.IComponentOpHistoryService;
import org.springframework.stereotype.Service;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:02,
 */
@Service
public class ComponentOpHistoryServiceImpl extends ServiceImpl<ComponentOpHistoryMapper, IaasComponentOpHistory>
        implements IComponentOpHistoryService {

    @Override
    public void modifyUpdateRecord(Long id, Long userId) {
        baseMapper.modifyUpdateRecord(id,userId);
    }
}
