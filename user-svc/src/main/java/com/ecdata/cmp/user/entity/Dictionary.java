package com.ecdata.cmp.user.entity;

import com.ecdata.cmp.common.enums.Code;
import lombok.Getter;

@Getter
public enum Dictionary implements Code {

    partion(1,"可用分区"),

    currentSpecs(2,"当前规格"),

    mirror(3,"镜像"),

    systemDisk(4,"系统盘"),

    createNumber(5,"创建数量"),

    virtualPrivateCloud(6,"虚拟私有云"),

    networkCard(7,"网卡"),

    securityGroup(8,"安全组"),

    elasticEscName(9,"弹性云服务器名称"),

    description(10,"创建数量");

    /**
     * 码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;


    Dictionary(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
