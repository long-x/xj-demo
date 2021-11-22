package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.huawei.dto.alarm.Record;
import com.ecdata.cmp.iaas.entity.IaasAlert;
import com.ecdata.cmp.iaas.entity.dto.IaasAlertVO;
import com.ecdata.cmp.iaas.mapper.IaasAlertMapper;
import com.ecdata.cmp.iaas.service.IIaasAlertService;
import com.ecdata.cmp.user.client.SysNotificationClient;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import com.ecdata.cmp.user.dto.SysNotificationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhaoYX
 * @since 2019/12/6 13:01,
 */
@Slf4j
@Service
public class IIaasAlertServiceImpl extends ServiceImpl<IaasAlertMapper, IaasAlert>
        implements IIaasAlertService {
    @Autowired
    private SysNotificationClient sysNotificationClient;

    @Override
    public void modifyUpdateRecord(Long id, Long updateUser) {
        baseMapper.modifyUpdateRecord(id,updateUser);
    }

    @Override
    public boolean saveAlert(IaasAlert ia) {
        return baseMapper.saveAlert(ia);
    }

    @Override
    public boolean saveAlertBatch(List<IaasAlert> list) {
        return baseMapper.saveAlertBatch(list);
    }

    @Override
    public IPage<IaasAlertVO> alertPage(Page page, Wrapper<IaasAlert> wrapper) {
        return baseMapper.alertPage(page,wrapper);
    }

    @Override
    public void addBatchToNotify(List<IaasAlert> iaasAlertList){
        List<SysNotificationVO> sysNotificationVOList = new ArrayList<SysNotificationVO>();
        for (IaasAlert alert : iaasAlertList) {
            SysNotificationVO sysNotificationVO = new SysNotificationVO();
            sysNotificationVO.setCsn(alert.getCsn());
            sysNotificationVO.setBusinessType(alert.getMeName());
            sysNotificationVO.setLevel(alert.getSeverity() + "");
            sysNotificationVO.setMessage(alert.getAlarmName());
            sysNotificationVO.setDetail(alert.getAdditionalInformation());
            sysNotificationVO.setType(2);
            sysNotificationVO.setSourceType(2);//来源类型：告警
            sysNotificationVO.setAlertPlatform(1);
            sysNotificationVOList.add(sysNotificationVO);
        }
        sysNotificationClient.addBatchNotifToUserByType(sysNotificationVOList);
    }

    @Override
    public SysNotificationReceiverVO fetchReceiveIdByAlert(Long csn) {
        return baseMapper.fetchReceiveIdByAlert(csn);
    }

}
