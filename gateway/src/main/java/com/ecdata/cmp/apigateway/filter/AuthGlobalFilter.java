package com.ecdata.cmp.apigateway.filter;

import com.alibaba.nacos.client.utils.StringUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecdata.cmp.apigateway.entity.LogValidity;
import com.ecdata.cmp.apigateway.service.ILogValidity;
import com.ecdata.cmp.apigateway.service.ISysLogService;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.common.crypto.Sign;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author honglei
 * @since 2019-08-23
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    /**
     * LOG LOG
     */
//    private static final ILogger LOG = SLoggerFactory.getLogger(AuthGlobalFilter.class);

    private ISysLogService sysLogService = null;

    @Autowired
    private ILogValidity logValidity;


    @Value("${switchs.zhuyun}")
    private boolean switchs;

    /**
     * 全局过滤器，网关做token 鉴权校验
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            //A.post或put方法走的过滤方法
            String method = exchange.getRequest().getMethodValue();
            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
                getBodyFilter(exchange, chain);
            }
            //B.其他方法
            return otherMethodFilter(exchange, chain);
        } catch (Exception e) {
            // 增加异常处理，防止日志插入失败造成接口整体出现问题
            log.error("filter error ", e);
            return chain.filter(exchange);
        }
    }

    private Mono<Void> otherMethodFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求
        ServerHttpRequest request = exchange.getRequest();
        //2.获取响应
        ServerHttpResponse response = exchange.getResponse();
        //3.判断用户是否访问的为登录路径，如果是登录路径、查询租户列表和Python调用的修改镜像构建状态则放行
        String path = request.getURI().getPath();
        //C.记录系统日志
        //insertSysLog(exchange, request);

        //4.0 不拦截对请求url地址
        if (notFilterUrl(path)) return chain.filter(exchange);

        //获取竹云token,后台验证活性
        HttpHeaders responseHeaders = request.getHeaders();
        String accesstoken = responseHeaders.getFirst("accessToken");
        String refreshToken = responseHeaders.getFirst("refreshToken");
        log.info("竹云accesstoken {} -",accesstoken);
        //4. 获取请求头中的令牌
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(AuthConstant.AUTHORIZATION_HEADER);
        //5. 如果请求头中的令牌为空, 则返回错误状态码
        if (StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //请求头竹云token为空,返回错误码
        if(switchs){
            if(StringUtils.isEmpty(accesstoken) && !(notFilterBanUrl(path))){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }

        //6. 解析请求头中的jwt令牌
        try {
            DecodedJWT jwt = Sign.verifyUserToken(token);
            if(switchs){
                if(!notFilterBanUrl(path)){
                    Long userId = jwt.getClaim(Sign.CLAIM_USER_ID).asLong();
                    LogValidity entity = new LogValidity();
                    entity.setUserId(userId);
                    entity.setAccessToken(accesstoken);
                    long count = logValidity.getTime(entity);
                    log.info("查询返回{}---",count);
                    if(count<1){
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return response.setComplete();
                    }
                }
            }

        } catch (Exception e) {
            //7. 如果解析出错, 说明令牌过期或者被篡改, 返回状态码
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //8. 如果解析正常则放行
        return chain.filter(exchange);
    }

    private Mono<Void> getBodyFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerRequest serverRequest = new DefaultServerRequest(exchange);
        Mono<String> bodyToMono = serverRequest.bodyToMono(String.class);
        return bodyToMono.flatMap(body -> {
            exchange.getAttributes().put("cachedRequestBody", body);
            ServerHttpRequest newRequest = new ServerHttpRequestDecorator(request) {
                @Override
                public HttpHeaders getHeaders() {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(super.getHeaders());
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                    return httpHeaders;
                }

                @Override
                public Flux<DataBuffer> getBody() {
                    NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                    DataBuffer bodyDataBuffer = nettyDataBufferFactory.wrap(body.getBytes());
                    return Flux.just(bodyDataBuffer);
                }
            };
            //B.其他方法
            otherMethodFilter(exchange, chain);

            return chain.filter(exchange.mutate().request(newRequest).build());
        });
    }

    private boolean notFilterUrl(String path) {
        return path.contains("/user-center/login")
                || path.contains("/user-center/tenant/list")
                || path.contains("/user-center/user/forget_password")
                || path.contains("/user-center/v1/sysNotification/add/batch_notification_bytpye")
                || path.contains("/user-center/v1/sysLogoStyle/getLastTimeSysLogoStyle")
                || path.contains("/user-center/v1/sysLicense/getLastTimeSysLicense")
                || path.contains("/iaas-service/v1/alert/add_batch")
                || path.contains("/iaas-service/oa/result")
                || path.contains("/iaas-service/oa/download")
                || path.contains("/huawei-service/v1/ManageOne/getToken")
                || path.contains("/huawei-service/v1/virtualMachine/get_virtual_machine_list")
                || path.contains("/huawei-service/v1/physicalhost/get_physical_host_list")
                || path.contains("/huawei-service/v1/AvailableArea/get_availablezone_resource")
                || path.contains("/chengtou-big-screen-web-service")
                || path.contains("/image-registry-service/v1/paasImageBuild/updateImageBuildStatus");
    }


    /**
     * 竹云调用接口
     * @param path
     * @return
     */
    private boolean notFilterBanUrl(String path) {
        return path.contains("/user-center/v1/bimIdentityManage");
    }



    @Override
    public int getOrder() {
        return 0;  // controller执行后过滤
    }

}
