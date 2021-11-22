package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.iaas.entity.transferCloud.ProjMigration;
import com.ecdata.cmp.iaas.mapper.ProjMigrationMapper;
import com.ecdata.cmp.iaas.service.ProjMigrationService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/11/19 15:31,
 */
@Service
public class ProjMigrationServiceImpl extends ServiceImpl<ProjMigrationMapper, ProjMigration>
        implements ProjMigrationService {

    @Override
    public List<Map<String,Object>> selectByMyWrapper(Wrapper<ProjMigration> wrapper) {
        return baseMapper.selectByMyWrapper(wrapper);
    }

    @Override
    public Map<String, Object> selectConditions() {
        List<String> status=baseMapper.selectStatus();
        List<String> names=baseMapper.selectSupervisorNames();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("status",status);
        map.put("supervisorNames",names);
        return map;
    }
}
