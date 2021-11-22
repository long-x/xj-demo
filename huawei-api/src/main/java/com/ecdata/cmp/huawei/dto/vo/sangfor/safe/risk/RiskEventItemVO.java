package com.ecdata.cmp.huawei.dto.vo.sangfor.safe.risk;


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
@ApiModel(value = "深信服-安全事件-Item", description = "深信服-安全事件-Item")
public class RiskEventItemVO {

    /**
     * 用于查找日志
     * Eg:”coengine|104008 ”
     */
    private String eventKey;

    /**
     * 分支id
     */
    private String branchId;

    /**
     * 原理
     */
    private String principle;

    /**
     * 标签
     */
    private List<String> tag;

    /**
     * Ip 属性
     * 0：全部 1：业务/已配置到某 业务2：没有归类（未配置 到业务的服务器）3：终端组/属于某终 端组4：未配置到终端组的 终端5：互联网
     */
    private Integer type;

    /**
     * 业务系统或用户组名称
     */
    private String groupName;

    /**
     * 失陷的主机 ip 地址
     */
    private String ip;

    /**
     * 事件产生的日期(非时间 戳)，此字段主要用于查 询，比如查询最近几天的 数据，用该字段作为过滤 条件
     * Eg. 20170701 具体到天
     */
    private Long recordDate;

    /**
     * 检测 引擎的名字
     */
    private String detectEngine;

    /**
     * 规则 id(用来找到对应 举证信息格式)
     */
    private String ruleId;

    /**
     * 安全事件产生的时间戳， 此字段表示该安全事件 当天第一次产生的具体 时间。
     * Eg. 1494582328 具体到秒
     */
    private Long firstTime;

    /**
     * 安全事件更新的时间戳， 此字段表示该安全事件 最近更新的时间，页面展 示可以使用该字段展示；
     * Eg. 1494582328 具体到秒
     */
    private Long lastTime;

    /**
     * 事件描述
     */
    private String eventDes;

    /**
     * 事件大类
     */
    private String infosecurity;

    /**
     * 事件子类(两个分类我没 有找到映射表)
     */
    private String infosecuritysub;

    /**
     * 失陷确定性
     * 1：低可疑 2：高可疑 3： 已失陷
     */
    private Integer priority;

    /**
     * 威胁性等级
     * 1：低威胁 2：中威胁 3： 高威胁
     */
    private Integer reliability;

    /**
     * 攻击阶段
     * -1：全部阶段, 1: 遭受攻击, 2: C&C 通信, 3: 黑产牟利 4: 内网探测 5：内网扩散攻击, 6：盗取数据
     */
    private Integer stage;

    /**
     * 风 险 主 机 (ip+groupName)
     * Eg. ‘1.1.1.1(业务1)’
     */
    private String hostRisk;

    /**
     * 分支名
     */
    private String branchName;

    /**
     * 处理状态
     * 0：未处理 1：已处理
     */
    private Integer dealStatus;

    /**
     * 攻击举证 proof
     * "proof": { "detail_list": [ { "count":1, "dst_type":"3", "hole_id": "10010236", "module_type": "30", "dst_ip": "192.201.8.17", "dst_group": "94" } ] } }
     */
    private String proof;

    /**
     *  处理建议
     */
    private String solution;

    /**
     * 用户名/主机名
     */
    private String hostName;

    /**
     * 是否为热点事件
     * 0：不是 1：是
     */
    private Integer eventType;

    /**
     * 用户用该字段去找对 应日志信息
     * { “log_type”: “param_code”: “param_origin”: }Log_type 对应日志类型 如下（暂只支持类型为安 全日志的对应查找，其他 类型不支持）： 安全日志: 1 操作日志: 2 DNS 日志: 3 第三方日志: 4 HTTPFLOW 日志:5 USERAUDIT 日志:6 ‘SAS_LOG':7'DASDB_LOG':8 具体查看接口 3.9
     */
    private String logParams;

    private String groupId;

    private Long dealTime;

//    /**
//     * 安全事件日志 top10
//     * 拉取时不会给出，需去请 求 3.9 接口
//     */
//    private List<String> top10;

}
