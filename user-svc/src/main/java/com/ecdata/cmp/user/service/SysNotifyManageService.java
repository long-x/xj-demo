package com.ecdata.cmp.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.common.api.BaseResponse;
import com.ecdata.cmp.user.dto.SysNotifyManageVO;
import com.ecdata.cmp.user.dto.request.SysNotificationRequest;
import com.ecdata.cmp.user.dto.request.SysNotifyManageRequest;
import com.ecdata.cmp.user.entity.SysNotifyManage;

import java.util.Map;

public interface SysNotifyManageService extends IService<SysNotifyManage> {

    IPage<SysNotifyManageVO> getNotifyManagePage(Page page, Map<String, Object> map);

    BaseResponse addSysNotifyManageByType (SysNotifyManageRequest notiRequest)throws Exception;
}
