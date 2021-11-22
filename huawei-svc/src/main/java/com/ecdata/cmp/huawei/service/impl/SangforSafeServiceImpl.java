package com.ecdata.cmp.huawei.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecdata.cmp.common.utils.BaseOkHttpClientUtil;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.DeviceInfoVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk.*;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.weak.*;
import com.ecdata.cmp.huawei.enumeration.SangforSafeRiskType;
import com.ecdata.cmp.huawei.service.SangforSafeService;
import com.ecdata.cmp.huawei.utils.SangforUtils;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 深信服安全Service
 *
 * created by laj
 */
@Slf4j
@Service
public class SangforSafeServiceImpl implements SangforSafeService {

    /* ---------------------------------------------------------------------------- */

    @Autowired
    private SangforUtils sangforUtils;

    /* ---------------------------------------------------------------------------- */

    private static final String SANGFOR_COMMON = "/sangforinter/v1/data";

    private static final String QUESTION_MARK = "?";

    private static final String RISK_BUSINESS = "/riskbusiness";

    private static final String RISK_TERMINAL = "/riskterminal";

    private static final String RISK_EVENT = "/riskevent";

    private static final String WEAK_PASSWORD = "/weakpasswd";

    private static final String WEAK_PLAIN_TEXT_TRANSMISSION = "/plaintexttransmission";

    private static final String WEAK_HOLE = "/hole";

    /** 风险业务 */
    private static final String RISK_BUSINESS_PLUS = SANGFOR_COMMON + RISK_BUSINESS + QUESTION_MARK;

    /** 风险终端 */
    private static final String RISK_TERMINAL_PLUS = SANGFOR_COMMON + RISK_TERMINAL + QUESTION_MARK;

    /** 风险事件 */
    private static final String RISK_EVENT_PLUS = SANGFOR_COMMON + RISK_EVENT + QUESTION_MARK;

    /** 弱密码 */
    private static final String WEAK_PASSWORD_PLUS = SANGFOR_COMMON + WEAK_PASSWORD + QUESTION_MARK;

    /** 明文 */
    private static final String WEAK_PLAIN_TEXT_TRANSMISSION_PLUS = SANGFOR_COMMON + WEAK_PLAIN_TEXT_TRANSMISSION + QUESTION_MARK;

    /** 漏洞 */
    private static final String WEAK_HOLE_PLUS = SANGFOR_COMMON + WEAK_HOLE + QUESTION_MARK;

    private static final String TOKEN = "token";

    private static final String FROM_ACTION_TIME = "fromActionTime";

    private static final String TO_ACTION_TIME = "toActionTime";

    private static final String MAX_COUNT = "maxCount";

    private static final String DATA = "data";

    private static final String ITEMS = "items";

    private static final String DEVICE_INFO = "device_info";

    private static final String SANGFOR_DEAL_COMMON = "/sangforinter/v1/handle";

    private static final String IP = "ip";

    private static final String BRANCH_ID = "branchId";

    private static final String GROUP_ID = "groupId";

    private static final String TYPE = "type";

    private static final String RECORD_DATE = "recordDate";

    private static final String SEVERITY_LEVEL = "severityLevel";

    private static final String DEAL_STATUS = "dealStatus";

    private static final String RULE_ID = "ruleId";

    private static final String PRIORITY = "priority";

    private static final String RELIABILITY = "reliability";

    private static final String EVENT_KEY = "eventKey";

    private static final String MULTI_DEAL_STATUS = "multi_deal_status";

    private static final String IS_HANDLE_HOST = "is_handle_host";

    private static final String DEAL_COMMENT = "deal_comment";

    private static final String CODE = "code";

    private static final Integer ZERO = 0;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    /* ---------------------------------------------------------------------------- */

    /**
     * 拼接url查询参数
     */
    private String buildGetParameters(long startTime, long endTime, int maxCount) {
        String url = Joiner.on("&").withKeyValueSeparator("=").join(
                ImmutableMap.of(TOKEN, sangforUtils.getToken(), FROM_ACTION_TIME, String.valueOf(startTime),
                        TO_ACTION_TIME, String.valueOf(endTime), MAX_COUNT, String.valueOf(maxCount)));
        log.info("buildGetParameters url={}", url);
        return url;
    }

    /**
     * 获取http响应json的data属性
     */
    private Optional<JSONObject> buildData(String res) {
        return Optional.ofNullable(res)
                .filter(StringUtils::isNotBlank)
                .flatMap(e -> Optional.ofNullable(JSONObject.parseObject(res)))
                .flatMap(e -> Optional.ofNullable(e.getJSONObject(DATA)));
    }

    /**
     * 获取http响应json的items属性
     */
    private Optional<JSONArray> buildItems(String res) {
        return buildData(res).flatMap(e -> Optional.ofNullable(e.getJSONArray(ITEMS)).filter(CollectionUtils::isNotEmpty));
    }

    /**
     * 获取http响应json的device_info属性
     */
    private Optional<JSONObject> buildDeviceInfo(String res) {
        return buildData(res).flatMap(e -> Optional.ofNullable(e.getJSONObject(DEVICE_INFO)));
    }

    /**
     * 组装URL，获取http响应
     */
    private String getResponse(long startTime, long endTime, int maxCount, String address) throws IOException {
        String url = sangforUtils.getUrl() + address + buildGetParameters(startTime, endTime, maxCount);
        log.info("address={}, startTime={}, endTime={}, maxCount={}, url={}", address, startTime, endTime, maxCount, url);

        return BaseOkHttpClientUtil.get(url, ImmutableMap.of());
    }

    /**
     * 获取处置请求url
     * @param riskType 风险类型
     * @return 请求url
     */
    private String getDealUrl(int riskType) {
        return Optional.ofNullable(SangforSafeRiskType.getType(riskType)).map(e -> {
            String url = null;
            switch (e) {
                case RISK_BUSINESS: {
                    log.info("getDealUrl, switch=RISK_BUSINESS");
                    url = SANGFOR_DEAL_COMMON + RISK_BUSINESS;
                    break;
                }
                case RISK_TERMINAL: {
                    log.info("getDealUrl, switch=RISK_TERMINAL");
                    url = SANGFOR_DEAL_COMMON + RISK_TERMINAL;
                    break;
                }
                case RISK_EVENT: {
                    log.info("getDealUrl, switch=RISK_EVENT");
                    url = SANGFOR_DEAL_COMMON + RISK_EVENT;
                    break;
                }
                case WEAK_PASSWORD: {
                    log.info("getDealUrl, switch=WEAK_PASSWORD");
                    url = SANGFOR_DEAL_COMMON + WEAK_PASSWORD;
                    break;
                }
                case PLAIN_TEXT: {
                    log.info("getDealUrl, switch=PLAIN_TEXT");
                    url = SANGFOR_DEAL_COMMON + WEAK_PLAIN_TEXT_TRANSMISSION;
                    break;
                }
                case HOLE: {
                    log.info("getDealUrl, switch=HOLE");
                    url = SANGFOR_DEAL_COMMON + WEAK_HOLE;
                    break;
                }
            }

            log.info("sangfor build deal url={}", url);

            return url;
        }).orElse(null);
    }

    /**
     * 获取处置请求的json
     * @param vo SangforSecurityRiskVO
     * @return 处置请求的json
     */
    private JSONObject getDealJson(SangforSecurityRiskVO vo) {
        return Optional.ofNullable(vo).flatMap(e -> Optional.ofNullable(SangforSafeRiskType.getType(e.getRiskType())).map(t -> {
            JSONObject json = new JSONObject();

            Optional.ofNullable(e.getIp()).ifPresent(m -> json.put(IP, m));
            Optional.ofNullable(e.getBranchId()).ifPresent(m -> json.put(BRANCH_ID, m));
            Optional.ofNullable(e.getType()).ifPresent(m -> json.put(TYPE, m));
            e.setRecordDate(Optional.ofNullable(e.getRecordDate()).orElse(Long.valueOf(SDF.format(new Date()))));

            switch (t) {
                case RISK_BUSINESS:
                case RISK_TERMINAL: {
                    // 风险业务，风险终端
                    log.info("getDealJson, switch=RISK_BUSINESS, RISK_TERMINAL");
                    Optional.ofNullable(e.getGroupId()).ifPresent(m -> json.put(GROUP_ID, m));
                    Optional.ofNullable(e.getRecordDate()).ifPresent(m -> json.put(RECORD_DATE, m));
                    Optional.ofNullable(e.getSeverityLevel()).ifPresent(m -> json.put(SEVERITY_LEVEL, m));
                    Optional.ofNullable(e.getDealStatus()).ifPresent(m -> json.put(DEAL_STATUS, m));
                    json.put(MULTI_DEAL_STATUS, 30);
                    break;
                }
                case RISK_EVENT: {
                    // 风险事件
                    log.info("getDealJson, switch=RISK_EVENT");
                    Optional.ofNullable(e.getGroupId()).ifPresent(m -> json.put(GROUP_ID, m));
                    Optional.ofNullable(e.getRecordDate()).ifPresent(m -> json.put(RECORD_DATE, m));
                    Optional.ofNullable(e.getRuleId()).ifPresent(m -> json.put(RULE_ID, m));
                    Optional.ofNullable(e.getPriority()).ifPresent(m -> json.put(PRIORITY, m));
                    Optional.ofNullable(e.getReliability()).ifPresent(m -> json.put(RELIABILITY, m));
                    Optional.ofNullable(e.getEventKey()).ifPresent(m -> json.put(EVENT_KEY, m));
                    Optional.ofNullable(e.getDealStatus()).ifPresent(m -> json.put(DEAL_STATUS, m));
                    json.put(MULTI_DEAL_STATUS, 30);
                    json.put(IS_HANDLE_HOST, true);
                    break;
                }
                case WEAK_PASSWORD:
                case PLAIN_TEXT:
                case HOLE: {
                    // 弱密码，明文，漏洞
                    log.info("getDealJson, switch=WEAK_PASSWORD, PLAIN_TEXT, HOLE");
                    Optional.ofNullable(e.getEventKey()).ifPresent(m -> json.put(EVENT_KEY, m));
                    break;
                }
            }

            Optional.ofNullable(e.getDealComment()).ifPresent(m -> json.put(DEAL_COMMENT, m));
            json.put(TOKEN, sangforUtils.getToken());

            log.info("sanfor build deal, vo={}, json={}", vo, json);

            return json;
        })).orElse(null);
    }

    /**
     * 获取处置请求的响应
     * @param vo SangforSecurityRiskVO
     * @return 处置结果
     */
    private String getDealResponse(SangforSecurityRiskVO vo) {
        return Optional.ofNullable(vo).map(e -> {
            String url = sangforUtils.getUrl() + getDealUrl(e.getRiskType());
            JSONObject json = getDealJson(vo);
            try {
                String response = BaseOkHttpClientUtil.post(url, json, new HashMap<>());
                log.info("sanfor deal response={}", response);
                return response;
            } catch (IOException ioe) {
                log.error("sanfor deal getDealResponse post error=" + ioe);
            }
            return null;
        }).orElse(null);
    }

    /* ---------------------------------------------------------------------------- */

    /**
     * 获取风险业务
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return RiskBusinessVO
     * @throws IOException IO异常
     */
    @Override
    public RiskBusinessVO getRiskBusiness(long startTime, long endTime, int maxCount) throws IOException {
        log.info("sangfor getRiskBusiness, startTime={}, endTime={}, maxCount={}", startTime, endTime, maxCount);
        String res = getResponse(startTime, endTime, maxCount, RISK_BUSINESS_PLUS);

        RiskBusinessVO riskBusinessVO = new RiskBusinessVO();

        buildItems(res).ifPresent(e -> {
            // items属性存在
            log.info("getRiskBusiness get items");
            riskBusinessVO.setItems(e.toJavaList(RiskBusinessItemVO.class).stream().filter(Objects::nonNull).peek(
                    t -> Optional.ofNullable(t.getDealTime()).filter(h -> h <= 0).ifPresent(h -> t.setDealTime(null))
            ).collect(Collectors.toList()));
        });

        buildDeviceInfo(res).ifPresent(e -> {
            // device_info属性存在
            log.info("getRiskBusiness get device_info");
            riskBusinessVO.setDeviceInfo(e.toJavaObject(DeviceInfoVO.class));
        });

        // 回传请求参数
        riskBusinessVO.setStartTime(startTime).setEndTime(endTime).setMaxCount(maxCount);

        log.info("sanfor getRiskBusiness, result={}", riskBusinessVO);

        return riskBusinessVO;
    }

    /* ---------------------------------------------------------------------------- */

    /**
     * 获取终端业务
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return RiskTerminalVO
     * @throws IOException IO异常
     */
    @Override
    public RiskTerminalVO getRiskTerminal(long startTime, long endTime, int maxCount) throws IOException {
        log.info("sangfor getRiskTerminal, startTime={}, endTime={}, maxCount={}", startTime, endTime, maxCount);
        String res = getResponse(startTime, endTime, maxCount, RISK_TERMINAL_PLUS);

        RiskTerminalVO riskTerminalVO = new RiskTerminalVO();

        buildItems(res).ifPresent(e -> {
            // items属性存在
            log.info("getRiskBusiness get items");
            riskTerminalVO.setItems(e.toJavaList(RiskTerminalItemVO.class).stream().filter(Objects::nonNull).peek(
                    t -> Optional.ofNullable(t.getDealTime()).filter(h -> h <= 0).ifPresent(h -> t.setDealTime(null))
            ).collect(Collectors.toList()));
        });

        buildDeviceInfo(res).ifPresent(e -> {
            // device_info属性存在
            log.info("getRiskBusiness get device_info");
            riskTerminalVO.setDeviceInfo(e.toJavaObject(DeviceInfoVO.class));
        });

        // 回传请求参数
        riskTerminalVO.setStartTime(startTime).setEndTime(endTime).setMaxCount(maxCount);

        log.info("sanfor getRiskTerminal, result={}", riskTerminalVO);

        return riskTerminalVO;
    }

    /* ---------------------------------------------------------------------------- */

    /**
     * 获取弱密码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return WeakPasswordVO
     * @throws IOException IO异常
     */
    @Override
    public WeakPasswordVO getWeakPassword(long startTime, long endTime, int maxCount) throws Exception {
        log.info("sangfor getWeakPassword, startTime={}, endTime={}, maxCount={}", startTime, endTime, maxCount);
        String res = getResponse(startTime, endTime, maxCount, WEAK_PASSWORD_PLUS);

        WeakPasswordVO weakPasswordVO = new WeakPasswordVO();

        buildItems(res).ifPresent(e -> {
            // items属性存在
            log.info("getWeakPassword get items");
            weakPasswordVO.setItems(e.toJavaList(WeakPasswordItemVO.class).stream().filter(Objects::nonNull).peek(
                    t -> Optional.ofNullable(t.getDealTime()).filter(h -> h <= 0).ifPresent(h -> t.setDealTime(null))
            ).collect(Collectors.toList()));
        });

        buildDeviceInfo(res).ifPresent(e -> {
            // device_info属性存在
            log.info("getWeakPassword get device_info");
            weakPasswordVO.setDeviceInfo(e.toJavaObject(DeviceInfoVO.class));
        });

        // 回传请求参数
        weakPasswordVO.setStartTime(startTime).setEndTime(endTime).setMaxCount(maxCount);

        log.info("sanfor getWeakPassword, result={}", weakPasswordVO);

        return weakPasswordVO;
    }

    /* ---------------------------------------------------------------------------- */

    /**
     * 获取明文传输
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return PlainTextVO
     * @throws IOException IO异常
     */
    @Override
    public PlainTextVO getPlainText(long startTime, long endTime, int maxCount) throws Exception {
        log.info("sangfor getPlainText, startTime={}, endTime={}, maxCount={}", startTime, endTime, maxCount);
        String res = getResponse(startTime, endTime, maxCount, WEAK_PLAIN_TEXT_TRANSMISSION_PLUS);

        PlainTextVO plainTextVO = new PlainTextVO();

        buildItems(res).ifPresent(e -> {
            // items属性存在
            log.info("getPlainText get items");
            plainTextVO.setItems(e.toJavaList(PlainTextItemVO.class).stream().filter(Objects::nonNull).peek(
                    t -> Optional.ofNullable(t.getDealTime()).filter(h -> h <= 0).ifPresent(h -> t.setDealTime(null))
            ).collect(Collectors.toList()));
        });

        buildDeviceInfo(res).ifPresent(e -> {
            // device_info属性存在
            log.info("getPlainText get device_info");
            plainTextVO.setDeviceInfo(e.toJavaObject(DeviceInfoVO.class));
        });

        // 回传请求参数
        plainTextVO.setStartTime(startTime).setEndTime(endTime).setMaxCount(maxCount);

        log.info("sanfor getPlainText, result={}", plainTextVO);

        return plainTextVO;
    }

    /* ---------------------------------------------------------------------------- */

    /**
     * 获取漏洞
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return WeakHoleVO
     * @throws IOException IO异常
     */
    @Override
    public WeakHoleVO getHole(long startTime, long endTime, int maxCount) throws Exception {
        log.info("sangfor getHole, startTime={}, endTime={}, maxCount={}", startTime, endTime, maxCount);
        String res = getResponse(startTime, endTime, maxCount, WEAK_HOLE_PLUS);

        WeakHoleVO weakHoleVO = new WeakHoleVO();

        buildItems(res).ifPresent(e -> {
            // items属性存在
            log.info("getHole get items");
            weakHoleVO.setItems(e.toJavaList(WeakHoleItemVO.class).stream().filter(Objects::nonNull).peek(
                    t -> Optional.ofNullable(t.getDealTime()).filter(h -> h <= 0).ifPresent(h -> t.setDealTime(null))
            ).collect(Collectors.toList()));
        });

        buildDeviceInfo(res).ifPresent(e -> {
            // device_info属性存在
            log.info("getHole get device_info");
            weakHoleVO.setDeviceInfo(e.toJavaObject(DeviceInfoVO.class));
        });

        // 回传请求参数
        weakHoleVO.setStartTime(startTime).setEndTime(endTime).setMaxCount(maxCount);

        log.info("sanfor getHole, result={}", weakHoleVO);

        return weakHoleVO;
    }

    /* ---------------------------------------------------------------------------- */

    /**
     * 获取风险事件
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return RiskEventVO
     * @throws IOException IO异常
     */
    @Override
    public RiskEventVO getRiskEvent(long startTime, long endTime, int maxCount) throws Exception {
        log.info("sangfor getRiskEvent, startTime={}, endTime={}, maxCount={}", startTime, endTime, maxCount);
        String res = getResponse(startTime, endTime, maxCount, RISK_EVENT_PLUS);

        RiskEventVO riskEventVO = new RiskEventVO();

        buildItems(res).ifPresent(e -> {
            // items属性存在
            log.info("getRiskEvent get items");
            riskEventVO.setItems(e.toJavaList(RiskEventItemVO.class).stream().filter(Objects::nonNull).peek(
                    t -> Optional.ofNullable(t.getDealTime()).filter(h -> h <= 0).ifPresent(h -> t.setDealTime(null))
            ).collect(Collectors.toList()));
        });

        buildDeviceInfo(res).ifPresent(e -> {
            // device_info属性存在
            log.info("getRiskEvent get device_info");
            riskEventVO.setDeviceInfo(e.toJavaObject(DeviceInfoVO.class));
        });

        // 回传请求参数
        riskEventVO.setStartTime(startTime).setEndTime(endTime).setMaxCount(maxCount);

        log.info("sanfor getRiskEvent, result={}", riskEventVO);

        return riskEventVO;
    }

    /* ---------------------------------------------------------------------------- */

    /**
     * 处理深信服安全告警
     * @param risk 事件信息
     * @return boolean 成功/失败
     */
    @Override
    public boolean dealRisk(SangforSecurityRiskVO risk) {
        log.info("deal risk = {}", risk);
        String res = getDealResponse(risk);
        JSONObject json = JSONObject.parseObject(res);
        log.info("deal risk json = {}", json);
        return Optional.ofNullable(json).map(e -> e.getInteger(CODE)).filter(e -> e.equals(ZERO)).isPresent();
    }
}
