package com.ecdata.cmp.iaas.schedule;

import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.huawei.client.OnOffMachineBaremetalClient;
import com.ecdata.cmp.iaas.client.IaasApplyResourceClient;
import com.ecdata.cmp.iaas.entity.dto.IaasTimedTaskVO;
import com.ecdata.cmp.iaas.entity.dto.apply.IaasApplyResourceVO;
import com.ecdata.cmp.iaas.mapper.apply.IaasApplyResourceMapper;
import com.ecdata.cmp.iaas.service.IApplyResourceService;
import com.ecdata.cmp.iaas.service.IaasAccountingRulesService;
import com.ecdata.cmp.iaas.service.IaasAccountingStatisticsService;
import com.ecdata.cmp.iaas.service.IaasTimedTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxiaojian
 * @date 2020/3/23 13:50
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class HandleApplyResource {
    @Autowired
    private IaasApplyResourceMapper applyResourceMapper;

    @Autowired
    private IApplyResourceService applyResourceService;

    @Autowired
    private IaasAccountingStatisticsService statisticsService;

    @Autowired
    private IaasTimedTaskService taskService;

    @Autowired
    private OnOffMachineBaremetalClient client;

    @Autowired
    private IaasAccountingRulesService accountingRulesService;

    /**
     * 处理申请资源租期到期的数据
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void handleLeaseExpireData() {
        List<IaasApplyResourceVO> iaasApplyResourceVOList = applyResourceService.recycleResourcesAuto();
        // 无申请资源到期
        if (CollectionUtils.isEmpty(iaasApplyResourceVOList)) {
            log.info("到期申请资源为空");
        }

        // 审批完成租期到期的资源重新copy一份存入数据库, 再发起一条默认审批流程，审批中添加取消纳管按钮
        for (IaasApplyResourceVO vo : iaasApplyResourceVOList) {
            vo.setState(1);
            vo.setBusinessActivitiId(152777148528160771L);
            vo.setBusinessActivitiName("城投新审核流程");
            applyResourceService.copyResourceAndStartRecoveryApply(vo);
        }
        log.info("自动回收资源成功");
    }

    /**
     * 每天跑一次计费统计
     */
    @Scheduled(cron="0 0 23 * * ?")
    public void accounting(){
        statisticsService.qrInfo();
        log.info("执行计划任务 accounting");
    }


    /**
     * 每天凌晨跑一次计费设置
     * 保证有一条最新的计费规则
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void updateTakeEffect(){
        //1.查询今天是否有计费规则
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer todayExits = accountingRulesService.isTodayExits(sdf.format(new Date()));
        if (todayExits<1){
            //2.没有规则,找到一条最新的规则,将状态改为生效
            accountingRulesService.updateTakeEffect(sdf.format(new Date()));
            log.info("更新和计费规则定时任务...");
        }
        return;
    }




}
