package com.ecdata.cmp.apigateway.entity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author honglei
 * @since 2019-09-12
 */
public class FilterEntity {

    //过滤器对应的Name
    private String name;

    //路由规则
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
