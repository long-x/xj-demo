package com.ecdata.cmp.apigateway.filter;

/**
 * @author honglei
 * @since 2019-08-29
 */

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.common.crypto.Sign;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 测试类，暂不引用此过滤器
 * <p>
 * 实现GatewayFilter过滤器，指定路由引用生效
 */
public class AuthorizeGatewayFilter implements GatewayFilter, Ordered {
    /**
     * 日志
     */
    private final ILogger log = SLoggerFactory.getLogger(AuthorizeGatewayFilter.class);

    /**
     * @param exchange ServerWebExchange
     * @param chain    GatewayFilterChain
     * @return {@code Mono<Void>} to indicate when request handling is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(AuthConstant.AUTHORIZATION_HEADER);
        String uid = headers.getFirst(AuthConstant.AUTHORIZATION_HEADER);
        log.info(request.getURI().toString());
        if (token == null) {
            token = request.getQueryParams().getFirst(AuthConstant.AUTHORIZATION_HEADER);
        }
        String name = "test_user";
        String email = "409178623@qq.com";
        String userId = UUID.randomUUID().toString();
        String authToken = Sign.generateEmailConfirmationToken(userId, email, "TEST_SECRET");
        try {
            DecodedJWT decodedJWT = Sign.verifySessionToken(authToken, "TEST_SECRET");
            String decodeUserId = decodedJWT.getClaim(Sign.CLAIM_USER_ID).asString();
            String decodeEmail = decodedJWT.getClaim(Sign.CLAIM_EMAIL).asString();
        } catch (Exception e) {
            log.error("fail to verify token", "token", token, e);
            return null;
        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }

}
