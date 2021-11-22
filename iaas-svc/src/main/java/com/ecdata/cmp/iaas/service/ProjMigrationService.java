package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.transferCloud.ProjMigration;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ProjMigrationService extends IService<ProjMigration> {

    List<Map<String,Object>> selectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<ProjMigration> wrapper);

    Map<String,Object> selectConditions();

}
