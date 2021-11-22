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
@ApiModel(value = "深信服-脆弱性-弱密码-Item", description = "深信服-脆弱性-弱密码-Item")
public class WeakPasswordItemVO {

    /**
     * 服务器 Ip 地址(目的 ip)
     * "1.1.1.3"
     */
    private String ip;

    /**
     * 与脆弱性详情有关
     */
    private Integer appCrc;

    /**
     * 分支 id
     */
    private String branchId;

    /**
     * 所属分支
     */
    private String branchName;

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
    private Integer dstPort;

    /**
     * 初次发现时间戳
     */
    private Long firstTime;

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
     */
    private Integer branchType;

    /**
     * 处理时间时间戳
     */
    private Long dealTime;

    /**
     * 脆弱性等级
     * 1：低风险 2：高风险  3：危险
     */
    private Integer level;

    /**
     * 多次记录的日期
     * Eg.[ 20180801, 20180813 ]
     */
    private List<Long> recordDate;

    /**
     * 脆弱性规则 id
     * 501001:FTP 登录弱密 码501002:LDAP 登录弱 密码501003:MYSQL 登 录 弱密码 501004:POP3 登录弱 密码501005:SMTP 登录弱 密码501006:TELNET 登 录 弱密码 501007:WEB 登 录 弱密码
     */
    private String ruleId;

    /**
     * 弱密码类型
     * Eg.FTP 登录弱密码
     */
    private String weakType;

    /**
     * 用户名
     */
    private String user;

    /**
     * 源 ip
     */
    private String srcIp;

    /**
     * 最新发生时间
     */
    private Long lastTime;

    /**
     * 事件标识
     * Eg:”coengine|104008 ”
     */
    private String eventKey;

}
