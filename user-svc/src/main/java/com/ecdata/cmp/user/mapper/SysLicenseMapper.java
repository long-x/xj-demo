package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.SysLicense;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysLicenseMapper extends BaseMapper<SysLicense> {
    void modifyUpdateRecord(Long id, Long updateUser);

    SysLicense getLastTimeSysLicense();

}