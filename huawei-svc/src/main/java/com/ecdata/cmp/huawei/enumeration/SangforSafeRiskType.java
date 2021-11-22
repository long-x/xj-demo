package com.ecdata.cmp.huawei.enumeration;

import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public enum SangforSafeRiskType {

    //1.风险业务  2.风险终端  3.风险事件  4.弱密码  5.明文传输  6.漏洞
    RISK_BUSINESS(1),
    RISK_TERMINAL(2),
    RISK_EVENT(3),
    WEAK_PASSWORD(4),
    PLAIN_TEXT(5),
    HOLE(6)
    ;

    private int value;

    SangforSafeRiskType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static SangforSafeRiskType getType(int value) {
        return Arrays.stream(SangforSafeRiskType.values()).filter(e -> e.getValue() == value).findFirst().orElse(null);
    }
}
