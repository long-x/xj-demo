package com.ecdata.cmp.huawei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.huawei.dto.alarm.SysManageOne;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface SysManageOneMapper extends BaseMapper<SysManageOne> {
    void updateOccurTime(SysManageOne sysManageOne);

    void saveSysManageOne(SysManageOne entity);

    SysManageOne selectById(Long id);

    int selectCountSysManageOne();

}
