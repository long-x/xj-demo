package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.component.IaasComponentScriptHistory;
import com.ecdata.cmp.iaas.mapper.component.ComponentScriptHistoryMapper;
import com.ecdata.cmp.iaas.service.IComponentScriptHistoryService;
import org.springframework.stereotype.Service;

/**
 * @author ZhaoYX
 * @since 2019/11/13 10:02,
 */
@Service
public class ComponentScriptHistoryServiceImpl extends ServiceImpl<ComponentScriptHistoryMapper, IaasComponentScriptHistory>
        implements IComponentScriptHistoryService {

    @Override
    public void modifyUpdateRecord(Long id, Long userId) {
        baseMapper.modifyUpdateRecord(id,userId);
    }
}
