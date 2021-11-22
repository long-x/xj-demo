package com.ecdata.cmp.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecdata.cmp.user.entity.SysLogoStyle;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysLogoStyleMapper extends BaseMapper<SysLogoStyle> {
    SysLogoStyle getLastTimeSysLogoStyle();

}