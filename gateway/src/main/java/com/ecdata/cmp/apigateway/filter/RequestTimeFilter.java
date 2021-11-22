package com.ecdata.cmp.apigateway.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 此过滤器功能为计算请求完成时间
 *
 * @author honglei
 * @since 2019-08-23
 */
public class RequestTimeFilter implements GatewayFilter, Ordered {
    /**
     * LOG LOG
     */
    private final Log log = LogFactory.getLog(GatewayFilter.class);
    /**
     * requestTimeBegin requestTimeBegin
     */
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    /**
     * filterI(exchange,chain)方法，在该方法中，先记录了请求的开始时间，并保存在ServerWebExchange中，此处是一个“pre”类型的过滤器
     * 然后再chain.filter的内部类中的run()方法中相当于"post"过滤器，
     * 在此处打印了请求所消耗的时间。然后将该过滤器注册到router中
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                    if (startTime != null) {
                        log.info("************************GatewayFilter 此过滤器功能为计算请求完成时间:filterfilterfilter:");
                        log.info(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
                    }
                })
        );

    }

    /**
     * Ordered的getOrder是给过滤器设定优先级别的，值越大则优先级越低
     *
     * @return 0
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
