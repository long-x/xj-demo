package com.ecdata.cmp.iaas.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecdata.cmp.iaas.entity.IaasAlert;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.iaas.entity.transferCloud.ProjMigration;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/12/6 13:00,
 */
public interface IIaasAlertService extends IService<IaasAlert> {

    void modifyUpdateRecord(Long id, Long updateUser);

    boolean saveAlert(IaasAlert ia);

    boolean saveAlertBatch(List<IaasAlert> list);

    IPage<IaasAlertVO> alertPage(Page page, @Param(Constants.WRAPPER) Wrapper<IaasAlert> wrapper);

    void addBatchToNotify(List<IaasAlert> iaasAlertList);

    SysNotificationReceiverVO fetchReceiveIdByAlert(Long csn);
}
