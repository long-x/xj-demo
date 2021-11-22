package com.ecdata.cmp.activiti;

import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author xuxinsheng
 * @since 2020-01-06
*/
@SpringBootApplication(scanBasePackages = {"com.ecdata.cmp.activiti", "com.ecdata.cmp.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ecdata.cmp.user"})
@Slf4j
//@EnableAutoConfiguration(exclude = {
//        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
//})
public class ActivitiApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ActivitiApplication.class, args);
        String moduleId = ctx.getEnvironment().getProperty("module-id");
        SnowFlakeIdGenerator.setModuleId(moduleId);
        log.info("===================== activiti Start Success =====================");
    }
}
