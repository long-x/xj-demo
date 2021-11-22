package com.ecdata.cmp.huawei.service;

import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk.RiskBusinessVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk.RiskEventVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk.RiskTerminalVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.weak.PlainTextVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.weak.WeakHoleVO;
import com.ecdata.cmp.huawei.dto.vo.sangfor.safe.weak.WeakPasswordVO;
import com.ecdata.cmp.iaas.entity.dto.sangfor.SangforSecurityRiskVO;


/**
 * 深信服安全Service
 */
public interface SangforSafeService {

    /**
     * 获取风险业务
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return RiskBusinessVO
     * @throws Exception 异常
     */
    RiskBusinessVO getRiskBusiness(long startTime, long endTime, int maxCount) throws Exception;

    /**
     * 获取风险终端
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return RiskTerminalVO
     * @throws Exception 异常
     */
    RiskTerminalVO getRiskTerminal(long startTime, long endTime, int maxCount) throws Exception;

    /**
     * 获取弱密码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return WeakPasswordVO
     * @throws Exception 异常
     */
    WeakPasswordVO getWeakPassword(long startTime, long endTime, int maxCount) throws Exception;

    /**
     * 获取明文传输
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return PlainTextVO
     * @throws Exception 异常
     */
    PlainTextVO getPlainText(long startTime, long endTime, int maxCount) throws Exception;

    /**
     * 获取漏洞
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return WeakHoleVO
     * @throws Exception 异常
     */
    WeakHoleVO getHole(long startTime, long endTime, int maxCount) throws Exception;

    /**
     * 获取风险事件
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param maxCount 总数
     * @return RiskEventVO
     * @throws Exception 异常
     */
    RiskEventVO getRiskEvent(long startTime, long endTime, int maxCount) throws Exception;

    /**
     * 处理深信服安全事件
     * @param risk 事件信息
     * @return boolean 成功/失败
     */
    boolean dealRisk(SangforSecurityRiskVO risk);
}
