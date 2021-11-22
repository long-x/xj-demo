package com.ecdata.cmp.common.crypto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class License {

    private int prod = 1;  // 1. 测试版   2. 企业版
    private Date expire;
    private String version;

}
