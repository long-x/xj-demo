package com.ecdata.cmp.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author honglei
 * @since 2019-08-14
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    /**
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
