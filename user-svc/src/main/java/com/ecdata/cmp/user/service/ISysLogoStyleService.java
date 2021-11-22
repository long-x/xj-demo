package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.user.entity.SysLogoStyle;

public interface ISysLogoStyleService extends IService<SysLogoStyle> {
    SysLogoStyle getLastTimeSysLogoStyle();
}
