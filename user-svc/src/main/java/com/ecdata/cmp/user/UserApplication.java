package com.ecdata.cmp.user;

import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author honglei
 * @since 2019-09-03
 */
@SpringBootApplication(scanBasePackages = {"com.ecdata.cmp.user", "com.ecdata.cmp.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ecdata.cmp.ocp", "com.ecdata.cmp.paas", "com.ecdata.cmp.iaas",
        "com.ecdata.cmp.huawei"})
@Slf4j
public class UserApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(UserApplication.class, args);
        String moduleId = ctx.getEnvironment().getProperty("module-id");
        SnowFlakeIdGenerator.setModuleId(moduleId);
        log.info("===================== User Start Success =====================");
    }
}
