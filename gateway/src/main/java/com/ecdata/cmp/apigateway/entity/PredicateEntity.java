package com.ecdata.cmp.apigateway.entity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author honglei
 * @since 2019-09-12
 * <p>
 * 路由断言实体类
 */
public class PredicateEntity {

    //断言对应的Name
    private String name;

    //断言规则
    private Map<String, String> args = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}
