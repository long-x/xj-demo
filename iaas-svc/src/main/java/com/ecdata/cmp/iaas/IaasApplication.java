package com.ecdata.cmp.iaas;

import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author honglei
 * @since 2019/11/5
 */
@SpringBootApplication(scanBasePackages = {"com.ecdata.cmp.iaas", "com.ecdata.cmp.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ecdata.cmp.user", "com.ecdata.cmp.huawei", "com.ecdata.cmp.activiti"})
@Slf4j
@EnableAsync
public class IaasApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(IaasApplication.class, args);
        String moduleId = ctx.getEnvironment().getProperty("module-id");
        SnowFlakeIdGenerator.setModuleId(moduleId);
        log.info("===================== IaasProvider Start Success =====================");
    }
}
