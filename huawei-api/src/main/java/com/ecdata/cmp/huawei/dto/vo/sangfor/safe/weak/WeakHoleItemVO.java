package com.ecdata.cmp.huawei.dto.vo.sangfor.safe.weak;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "深信服-脆弱性-漏洞信息-Item", description = "深信服-脆弱性-漏洞信息-Item")
public class WeakHoleItemVO {

    /**
     * 服务器 Ip 地址(目的 ip) "1.1.1.3"
     */
    private String ip;

    /**
     * 与脆弱性详情有关 用于确定漏洞所处协 议
     */
    private Integer appCrc;

    /**
     * 漏洞所处协议
     */
    private String protocol;

    /**
     * 所属分支
     */
    private String branchName;

    /**
     * 分支 ID Eg ： “1”
     */
    private String branchId;

    /**
     * Ip 属性
     * 0：全部 1：业务/已配置到某 业务2：没有归类（未配置 到业务的服务器） 3：终端组/属于某终 端组4：未配置到终端组的 终端5：互联网
     */
    private Integer type;

    /**
     * 对应 type 的 id
     * 若不是组,groupName 直接放 id
     */
    private String groupId;

    /**
     * 对应的组名
     */
    private String groupName;

    /**
     * 目的端口
     */
    private String dstPort;

    /**
     * 初次发现时间戳
     */
    private Long firstTime;

    /**
     * 仅漏洞有用（漏洞 id）
     */
    private Integer holeId;

    /**
     * 处理细节
     */
    private String dealDetail;

    /**
     * 处理状态
     * 0:未处理 1:已处理
     */
    private Integer dealStatus;

    /**
     * 分支类型
     * 0:根据用户 1:根据设 备
     */
    private Integer branchType;

    /**
     * 处理时间戳
     */
    private Long dealTime;

    /**
     * 脆弱性等级(好像只 在漏洞中看到了)
     * 1：低风险 2：高风险 3：危险
     */
    private Integer level;

    /**
     * 漏洞分类（简称）
     */
    private String sipHoleType;

    /**
     * 多次记录的日期
     * Eg.[ 20180801, 20180813 ]
     */
    private List<Long> recordDate;

    /**
     *  脆弱性规则 id
     *  504001:漏洞风险
     */
    private String ruleId;

    /**
     * 漏洞全名
     * /home/fantom/apps/s eclib/local/fw.unzippe d/4.7/pvs/patch0/rule s_des_ini_cn/ 下找对应 holeId 文件
     */
    private String holeName;

    /**
     * 漏洞风险描述
     */
    private String desc;

    /**
     * 可能产生的影响
     */
    private String impact;

    /**
     * 解决方案
     * [“解决方案 1”， “解决方案 2”， ... ]
     */
    private List<String> solution;

    /**
     * 漏洞信息举证
     */
    private String proof;

    /**
     * 最新发生时间时间戳
     */
    private Long lastTime;

    /**
     * 事件标识
     * Eg:”coengine|104008 ”
     */
    private String eventKey;

}
