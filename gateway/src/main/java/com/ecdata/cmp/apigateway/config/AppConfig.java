package com.ecdata.cmp.apigateway.config;

import com.ecdata.cmp.apigateway.filter.AuthorizeGatewayFilter;
import com.ecdata.cmp.apigateway.filter.RequestTimeFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfig配置文件 ，结合实现GatewayFilter的过滤器使用
 *
 * @author honglei
 * @since 2019-08-23
 */
@Configuration
public class AppConfig {
    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        // @formatter:off
        return builder.routes()
                .route(r -> r.path("/config3/**")
                        .filters(f -> f.filter(new RequestTimeFilter()).stripPrefix(1))
                        .uri("lb://config")
                        .order(0)
                        .id("config-route-filter")
                )
                .build();
        // @formatter:on
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/config4/**")
                        .filters(f -> f.filter(new AuthorizeGatewayFilter()).stripPrefix(1))
                        .uri("lb://config")
                        .order(0)
                        .id("config-route-filter")
                )
                .build();
    }
}
