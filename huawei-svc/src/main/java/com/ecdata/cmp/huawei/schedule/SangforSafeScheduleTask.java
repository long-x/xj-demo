package com.ecdata.cmp.huawei.schedule;

import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk.RiskBusinessVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk.RiskEventVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk.RiskTerminalVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.weak.PlainTextVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.weak.WeakHoleVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.weak.WeakPasswordVO;
import com.ecdata.cmp.huawei.enumeration.SangforSafeRiskType;
import com.ecdata.cmp.huawei.service.SangforSafeService;
import com.ecdata.cmp.iaas.client.IaasSangforClient;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@Slf4j
public class SangforSafeScheduleTask {

    @Autowired
    private SangforSafeService sangforSafeService;

    @Autowired
    private IaasSangforClient iaasSangforClient;

    private static final ZoneId LOCAL_ZONE_ID = ZoneId.systemDefault();

    private static final int YEAR_2020 = 2020;

    private static final int MONTH_DAY_1 = 1;

    private static final int ZERO = 0;

    private static final long INIT_DATE = LocalDateTime
            .of(YEAR_2020, MONTH_DAY_1, MONTH_DAY_1, ZERO, ZERO, ZERO)
            .atZone(LOCAL_ZONE_ID).toInstant().getEpochSecond();   // 精度：秒

    private static final Integer MAX_COUNT = 10000;

    private static final Integer INTEGER_NULL = null;

    private static final String STRING_NULL = null;

    @Value("${huawei.scheduled}")
    private boolean scheduled;

    /**
     * 取数据库中未处理记录的最早日期，和2020/1/1日比较，取较小的日期
     */
    private long getStartTime() {
        return Optional.ofNullable(iaasSangforClient.minUnhandled())
                .map(e -> Long.min(Optional.ofNullable(e.getLastTime()).orElse(INIT_DATE),
                        Optional.ofNullable(e.getRecordDate()).orElse(INIT_DATE))
                ).orElse(INIT_DATE);
    }

    /**
     * 组装VO对象
     */
    private SangforSecurityRiskVO buildVO(SangforSafeRiskType riskType, Integer type,
                                          String branchId, String branchName, String groupId, String groupName,
                                          String ip, Integer severityLevel, Long recordDate, Date lastTime, Date dealTime, Integer dealStatus,
                                          String ruleId, Integer priority, Integer reliability, String eventKey,
                                          Integer level, String metaData) {

        SangforSecurityRiskVO vo = new SangforSecurityRiskVO();

        vo.setRiskType(riskType.getValue())
                .setType(type)
                .setBranchId(branchId)
                .setBranchName(branchName)
                .setGroupId(groupId)
                .setGroupName(groupName)
                .setIp(ip)
                .setSeverityLevel(severityLevel)
                .setRecordDate(recordDate)
                .setLastTime(lastTime)
                .setDealStatus(dealStatus).setDealTime(dealTime)
                .setRuleId(ruleId)
                .setPriority(priority)
                .setReliability(reliability)
                .setEventKey(eventKey)
                .setLevel(level)
                .setMetaData(metaData);

        return vo;
    }

    @Scheduled(cron = "13 */6 * * * ?")
    public void configureTasks(){
        log.info("sangfor safe configureTasks called");
        if (scheduled) {
            log.info("sangfor safe configureTasks ready");
            try {
                long startTime = getStartTime();
                long endTime = LocalDateTime.now().atZone(LOCAL_ZONE_ID).toInstant().getEpochSecond();
                log.info("sangfor safe configureTasks startTime={}, endTime={}", startTime, endTime);

                List<SangforSecurityRiskVO> list = new ArrayList<>();

                // risk_type: 1.风险业务  2.风险终端  3.风险事件  4.弱密码  5.明文传输  6.漏洞

                // 1.风险业务
                log.info("sangfor safe configureTasks risk business ready");
                Optional.ofNullable(sangforSafeService.getRiskBusiness(startTime, endTime, MAX_COUNT))
                        .map(RiskBusinessVO::getItems).filter(CollectionUtils::isNotEmpty).ifPresent(
                                e -> list.addAll(e.stream().filter(Objects::nonNull).map(t -> {
                                    SangforSafeRiskType riskType = SangforSafeRiskType.RISK_BUSINESS;
                                    Integer type = t.getType();
                                    String branchId = t.getBranchId();
                                    String branchName = t.getBranchName();
                                    String groupId = t.getGroupId();
                                    String groupName = t.getGroupName();
                                    String ip = t.getIp();
                                    Integer severityLevel = t.getSeverityLevel();
                                    Long recordDate = t.getRecordDate();
                                    Date lastTime = Optional.ofNullable(t.getLastTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    Integer dealStatus = t.getDealStatus();
                                    Date dealTime = Optional.ofNullable(t.getDealTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    Integer level = Optional.ofNullable(t.getFallLevel()).map(h -> {
                                    if (h == 0) {
                                        h = 1;
                                    }
                                    return h;
                                    }).orElse(null);

                                    return buildVO(riskType, type, branchId, branchName, groupId, groupName, ip,
                                            severityLevel, recordDate, lastTime, dealTime, dealStatus,
                                            STRING_NULL, INTEGER_NULL, INTEGER_NULL, STRING_NULL, level, JSONObject.toJSONString(t));
                                }).collect(Collectors.toList()))
                );
                log.info("sangfor safe configureTasks risk business ok");

                // 2.风险终端
                log.info("sangfor safe configureTasks risk terminal ready");
                Optional.ofNullable(sangforSafeService.getRiskTerminal(startTime, endTime, MAX_COUNT))
                        .map(RiskTerminalVO::getItems).filter(CollectionUtils::isNotEmpty).ifPresent(
                                e -> list.addAll(e.stream().filter(Objects::nonNull).map(t -> {
                                    SangforSafeRiskType riskType = SangforSafeRiskType.RISK_TERMINAL;
                                    Integer type = t.getType();
                                    String branchId = t.getBranchId();
                                    String branchName = t.getBranchName();
                                    String groupId = t.getGroupId();
                                    String groupName = t.getGroupName();
                                    String ip = t.getIp();
                                    Integer severityLevel = t.getSeverityLevel();
                                    Long recordDate = t.getRecordDate();
                                    Date lastTime = Optional.ofNullable(t.getLastTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    Integer dealStatus = t.getDealStatus();
                                    Date dealTime = Optional.ofNullable(t.getDealTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    Integer level = Optional.ofNullable(t.getFallLevel()).map(h -> {
                                        if (h == 0) {
                                            h = 1;
                                        }
                                        return h;
                                    }).orElse(null);

                                    return buildVO(riskType, type, branchId, branchName, groupId, groupName, ip,
                                            severityLevel, recordDate, lastTime, dealTime, dealStatus,
                                            STRING_NULL, INTEGER_NULL, INTEGER_NULL, STRING_NULL, level, JSONObject.toJSONString(t));
                                }).collect(Collectors.toList()))
                );
                log.info("sangfor safe configureTasks risk terminal ok");

                // 3.风险事件
                log.info("sangfor safe configureTasks risk event ready");
                Optional.ofNullable(sangforSafeService.getRiskEvent(startTime, endTime, MAX_COUNT))
                        .map(RiskEventVO::getItems).filter(CollectionUtils::isNotEmpty).ifPresent(
                                e -> list.addAll(e.stream().filter(Objects::nonNull).map(t -> {
                                    SangforSafeRiskType riskType = SangforSafeRiskType.RISK_EVENT;
                                    Integer type = t.getType();
                                    String branchId = t.getBranchId();
                                    String branchName = t.getBranchName();
                                    String groupId = t.getGroupId();
                                    String groupName = t.getGroupName();
                                    String ip = t.getIp();
                                    Integer severityLevel = null;
                                    Long recordDate = t.getRecordDate();
                                    Date lastTime = Optional.ofNullable(t.getLastTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    Integer dealStatus = t.getDealStatus();
                                    Date dealTime = Optional.ofNullable(t.getDealTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    String ruleId = t.getRuleId();
                                    Integer priority = t.getPriority();
                                    Integer reliability = t.getReliability();
                                    String eventKey = t.getEventKey();
                                    Integer level = Optional.ofNullable(reliability).orElse(Optional.ofNullable(priority).orElse(null));

                                    return buildVO(riskType, type, branchId, branchName, groupId, groupName, ip,
                                            severityLevel, recordDate, lastTime, dealTime, dealStatus,
                                            ruleId, priority, reliability, eventKey, level, JSONObject.toJSONString(t));
                                }).collect(Collectors.toList()))
                );
                log.info("sangfor safe configureTasks risk event ok");

                // 4.弱密码
                log.info("sangfor safe configureTasks weak password ready");
                Optional.ofNullable(sangforSafeService.getWeakPassword(startTime, endTime, MAX_COUNT))
                        .map(WeakPasswordVO::getItems).filter(CollectionUtils::isNotEmpty).ifPresent(
                                e -> list.addAll(e.stream().filter(Objects::nonNull).map(t -> {
                                    SangforSafeRiskType riskType = SangforSafeRiskType.WEAK_PASSWORD;
                                    Integer type = t.getType();
                                    String branchId = t.getBranchId();
                                    String branchName = t.getBranchName();
                                    String groupId = t.getGroupId();
                                    String groupName = t.getGroupName();
                                    String ip = t.getIp();
                                    Integer severityLevel = null;
                                    Long recordDate = Optional.ofNullable(t.getRecordDate()).flatMap(m -> m.stream().findFirst()).orElse(null);
                                    Date lastTime = Optional.ofNullable(t.getLastTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    Integer dealStatus = t.getDealStatus();
                                    Date dealTime = Optional.ofNullable(t.getDealTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    String ruleId = t.getRuleId();
                                    String eventKey = t.getEventKey();
                                    Integer level = t.getLevel();

                                    return buildVO(riskType, type, branchId, branchName, groupId, groupName, ip,
                                            severityLevel, recordDate, lastTime, dealTime, dealStatus,
                                            ruleId, INTEGER_NULL, INTEGER_NULL, eventKey, level, JSONObject.toJSONString(t));
                                }).collect(Collectors.toList()))
                );
                log.info("sangfor safe configureTasks weak password ok");

                // 5.明文
                log.info("sangfor safe configureTasks plain text ready");
                Optional.ofNullable(sangforSafeService.getPlainText(startTime, endTime, MAX_COUNT))
                        .map(PlainTextVO::getItems).filter(CollectionUtils::isNotEmpty).ifPresent(
                                e -> list.addAll(e.stream().filter(Objects::nonNull).map(t -> {
                                    SangforSafeRiskType riskType = SangforSafeRiskType.PLAIN_TEXT;
                                    Integer type = t.getType();
                                    String branchId = t.getBranchId();
                                    String branchName = t.getBranchName();
                                    String groupId = t.getGroupId();
                                    String groupName = t.getGroupName();
                                    String ip = t.getIp();
                                    Integer severityLevel = null;
                                    Long recordDate = Optional.ofNullable(t.getRecordDate()).flatMap(m -> m.stream().findFirst()).orElse(null);
                                    Date lastTime = Optional.ofNullable(t.getLastTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    Integer dealStatus = t.getDealStatus();
                                    Date dealTime = Optional.ofNullable(t.getDealTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    String ruleId = t.getRuleId();
                                    String eventKey = t.getEventKey();
                                    Integer level = t.getLevel();

                                    return buildVO(riskType, type, branchId, branchName, groupId, groupName, ip,
                                            severityLevel, recordDate, lastTime, dealTime, dealStatus,
                                            ruleId, INTEGER_NULL, INTEGER_NULL, eventKey, level, JSONObject.toJSONString(t));
                                }).collect(Collectors.toList()))
                );
                log.info("sangfor safe configureTasks plain text ok");

                // 6.漏洞
                log.info("sangfor safe configureTasks hole ready");
                Optional.ofNullable(sangforSafeService.getHole(startTime, endTime, MAX_COUNT))
                        .map(WeakHoleVO::getItems).filter(CollectionUtils::isNotEmpty).ifPresent(
                                e -> list.addAll(e.stream().filter(Objects::nonNull).map(t -> {
                                    SangforSafeRiskType riskType = SangforSafeRiskType.HOLE;
                                    Integer type = t.getType();
                                    String branchId = t.getBranchId();
                                    String branchName = t.getBranchName();
                                    String groupId = t.getGroupId();
                                    String groupName = t.getGroupName();
                                    String ip = t.getIp();
                                    Integer severityLevel = null;
                                    Long recordDate = Optional.ofNullable(t.getRecordDate()).flatMap(m -> m.stream().findFirst()).orElse(null);
                                    Date lastTime = Optional.ofNullable(t.getLastTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    Integer dealStatus = t.getDealStatus();
                                    Date dealTime = Optional.ofNullable(t.getDealTime()).map(h -> h*1000).map(Date::new).orElse(null);
                                    String ruleId = t.getRuleId();
                                    String eventKey = t.getEventKey();
                                    Integer level = t.getLevel();

                                    return buildVO(riskType, type, branchId, branchName, groupId, groupName, ip, severityLevel, recordDate, lastTime, dealTime, dealStatus,
                                            ruleId, INTEGER_NULL, INTEGER_NULL, eventKey, level, JSONObject.toJSONString(t));
                                }).collect(Collectors.toList()))
                );
                log.info("sangfor safe configureTasks hole ok");

                // riskType ok

                log.info("sangfor safe configureTasks list={}", list);

                Optional.of(list).filter(CollectionUtils::isNotEmpty).ifPresent(e -> {
                    int begin = 0;
                    while(begin <= e.size()) {
                        int end = Integer.min(begin + 9, e.size());
                        List<SangforSecurityRiskVO> subVos;
                        if (CollectionUtils.isNotEmpty(subVos = e.subList(begin, end))) {
                            try {
                                log.info("sangfor safe configureTasks, add batch, begin={}, end={}, list={}", begin, end, subVos);
                                iaasSangforClient.addBatch(subVos);
                            } catch (Exception exp) {
                                log.error("sangfor safe configureTasks begin=" + begin + ", end=" + end + ", error=" + exp);
                            }
                        }
                        begin = end + 1;
                    }
                });
                log.info("sangfor safe configureTasks end");
            } catch (Exception e) {
                log.error("sangfor safe configureTasks error=", e);
            }
        }
    }

//    public static void main(String[] args) {
//        ImmutableList<Integer> list = ImmutableList.<Integer>builder()
//                .add(1).add(2).add(3).add(4).add(5).add(6).add(7).add(8).add(9).add(10)
//                .add(11).add(12).add(13).add(14).add(15).add(16).add(17).add(18)
//                .build();
//
//        pageList(list);
//    }
//
//    private static void pageList(ImmutableList<Integer> list) {
//        int begin = 0;
//        while(begin <= list.size()) {
//            int end = Integer.min(begin + 9, list.size());
//            List<Integer> l ;
//            if (CollectionUtils.isNotEmpty(l = list.subList(begin, end))) {
//                System.out.println(l);
//            }
//            begin = end + 1;
//        }
//    }

}
