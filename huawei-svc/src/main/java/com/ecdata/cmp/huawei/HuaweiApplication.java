package com.ecdata.cmp.huawei;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.ecdata.cmp.huawei", "com.ecdata.cmp.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ecdata.cmp.user","com.ecdata.cmp.huawei","com.ecdata.cmp.iaas"})
@Slf4j
public class HuaweiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuaweiApplication.class, args);
    }

}
