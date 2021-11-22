package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.dto.UserVO;
import com.ecdata.cmp.user.dto.chargeable.SysChargeableVO;
import com.ecdata.cmp.user.entity.SysChargeable;

import java.util.List;

public interface ISysChargeableService extends IService<SysChargeable> {
    void modifyUpdateRecord(Long id, Long updateUser);

    List<SysChargeableVO> getIaasSelection();

    IPage<SysChargeableVO> qryChargeInfo(Page<SysChargeableVO> page,
                                         QueryWrapper<SysChargeable> queryWrapper);

}
