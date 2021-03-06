package com.ecdata.cmp.iaas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecdata.cmp.huawei.client.SangforSafeClient;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import com.ecdata.cmp.iaas.entity.dto.response.sangfor.MinTimeObject;
import com.ecdata.cmp.iaas.entity.sangfor.SangforSecurityRisk;
import com.ecdata.cmp.iaas.mapper.sangfor.SangforMapper;
import com.ecdata.cmp.iaas.service.IaasSangforRiskService;
import com.ecdata.cmp.user.client.SysNotificationClient;
import com.ecdata.cmp.user.dto.SysNotificationReceiverVO;
import com.ecdata.cmp.user.dto.SysNotificationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhaoYX
 * @since 2020/4/21 10:47,
 */
@Slf4j
@Service
public class IaasSangforRiskServiceImpl extends ServiceImpl<SangforMapper, SangforSecurityRisk> implements IaasSangforRiskService {

    @Autowired
    private SysNotificationClient sysNotificationClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchRisk(List<SangforSecurityRisk> risks) {
        return baseMapper.insertBatchRisk(risks);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MinTimeObject getMinTime() {
        MinTimeObject obj = new MinTimeObject();
        Date lastDate = baseMapper.getMinLastTime();
        Long recordDate = baseMapper.getMinRecordDate();
        Date date = null;
        if(recordDate !=null){
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMdd");
            String s= String.valueOf(recordDate);
            try {
                date = formatter.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Long last = null;
        Long record = null;
        if(null != lastDate)
            last= lastDate.getTime()/1000;
        if(null != recordDate)
            record= date.getTime()/1000;//.getTime()

        obj.setLastTime(last);
        obj.setRecordDate(record);
        return obj;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysNotificationVO addToNotify(SangforSecurityRisk risk){
        SysNotificationVO sysNotificationVO = new SysNotificationVO();
        String level = "";
        if(risk.getLevel()==null)
            level = "13";
        else{
            // '???????????? 1???????????? 2???????????? 3?????????',
            switch(risk.getLevel()){
                case 1:level = String.valueOf(13);break;
                case 2:level = String.valueOf(12);break;
                case 3:level = String.valueOf(11);break;
            }
        }
        sysNotificationVO.setLevel(level);
        //risk_type '1.????????????  2.????????????  3.????????????  4.?????????  5.????????????  6.??????',
        String riskType = "";
        switch (risk.getRiskType()){
            case 1:riskType="????????????";break;
            case 2:riskType="????????????";break;
            case 3:riskType="????????????";break;
            case 4:riskType="?????????";break;
            case 5:riskType="????????????";break;
            case 6:riskType="??????";break;
        }
        sysNotificationVO.setSangId(risk.getId());
        sysNotificationVO.setMessage(riskType);
        sysNotificationVO.setDetail(risk.getIp());
        sysNotificationVO.setType(2);
        sysNotificationVO.setSourceType(2);//?????????????????????
        sysNotificationVO.setAlertPlatform(2);
        return sysNotificationVO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBatchToNotify(List<SangforSecurityRisk> securityRisks){
        List<SysNotificationVO> sysNotificationVOList = new ArrayList<SysNotificationVO>();
        for (SangforSecurityRisk risk : securityRisks) {
            SysNotificationVO sysNotificationVO = this.addToNotify(risk);
            sysNotificationVOList.add(sysNotificationVO);
        }
        sysNotificationClient.addBatchNotifToUserByType(sysNotificationVOList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateToNotify(SangforSecurityRiskVO riskVO, Long receiverId) {
        //iaas??????????????????
        sysNotificationClient.dealSangforNotify(receiverId,riskVO.getDealComment());
        log.info("updatedToNotify "+receiverId);
//        sysNotificationClient.oneDealMessage(receiverId,riskVO.getDealComment());

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysNotificationReceiverVO> fetchReceiveIdByRisk(Long riskId) {
        return baseMapper.fetchReceiveIdByRisk(riskId);
    }
}
