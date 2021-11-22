package com.ecdata.cmp.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.user.dto.UserVO;
import com.ecdata.cmp.user.dto.chargeable.SysChargeableVO;
import com.ecdata.cmp.user.entity.SysChargeable;
import com.ecdata.cmp.user.mapper.SysChargeableMapper;
import com.ecdata.cmp.user.service.ISysChargeableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/11/27 13:05,
 */
@Service
public class SysChargeableServiceImpl extends ServiceImpl<SysChargeableMapper,
        SysChargeable> implements ISysChargeableService {

    @Autowired
    SysChargeableMapper chargeableMapper;

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id,updateUser);
    }

    @Override
    public List<SysChargeableVO> getIaasSelection() {
        return chargeableMapper.getIaasSelection();
    }

    @Override
    public IPage<SysChargeableVO> qryChargeInfo(Page<SysChargeableVO> page,
                                                QueryWrapper<SysChargeable> queryWrapper) {
        IPage<SysChargeableVO> result = baseMapper.qryChargeInfo(page,queryWrapper);
        return result;
    }

}
