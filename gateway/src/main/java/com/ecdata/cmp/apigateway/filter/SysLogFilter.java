package com.ecdata.cmp.apigateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.utils.StringUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecdata.cmp.apigateway.entity.LogValidity;
import com.ecdata.cmp.apigateway.entity.SysLog;
import com.ecdata.cmp.apigateway.service.ILogValidity;
import com.ecdata.cmp.apigateway.service.ISysLogService;
import com.ecdata.cmp.common.auth.AuthConstant;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

@Component
@Slf4j
public class SysLogFilter implements GlobalFilter, Ordered {

    private ISysLogService sysLogService = null;

    private static Joiner joiner = Joiner.on("");

    @Autowired
    private ILogValidity logValidity;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (Objects.equals(getStatusCode(), HttpStatus.OK) && body instanceof Flux) {
                    // 获取ContentType，判断是否返回JSON格式数据
                    String originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    if(StringUtils.isNotBlank(originalResponseContentType) && originalResponseContentType.contains("application/json")) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {//解决返回体分段传输
                            List<String> list = Lists.newArrayList();
                            dataBuffers.forEach(dataBuffer -> {
                                try {
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    DataBufferUtils.release(dataBuffer);

                                    list.add(new String(content, StandardCharsets.UTF_8));
                                } catch (Exception e) {
                                    log.info("动态加载API加密规则失败，失败原因：{}", Throwables.getStackTraceAsString(e));
                                }
                            });
                            String httpResult = joiner.join(list);
                            insertSysLog(exchange, httpResult);
                            byte[] uppedContent = new String(httpResult.getBytes(), StandardCharsets.UTF_8).getBytes();
                            originalResponse.getHeaders().setContentLength(uppedContent.length);
                            return bufferFactory.wrap(uppedContent);
                        }));
                    }
                }
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };
        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    private void insertSysLog(ServerWebExchange exchange, String httpResult) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers1 = exchange.getResponse().getHeaders();
        try {
            String postBody = exchange.getAttributeOrDefault("cachedRequestBody", "");
            String apiDesc = URLDecoder.decode(Objects.requireNonNull(exchange.getResponse().getHeaders().get("apiOperation")).toString(), "utf-8");
            SysLog sysLog = SysLog.builder()
                    .type(2)//正常
                    .method(request.getMethod() != null ? request.getMethod().toString() : "")
                    .createTime(DateUtil.getNow())
                    .id(SnowFlakeIdGenerator.getInstance().nextId())
                    .uri(request.getURI().toString())
                    .ip(Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress())
                    .remark(apiDesc)
                    .httpResult(httpResult)
                    .build();
            if (StringUtils.isNotBlank(postBody)) {
                log.info("post body:" + postBody);
                sysLog.setParams(postBody);
            } else {
                log.info("post body is empty");
                sysLog.setParams("{}".equals(request.getQueryParams().toString()) ? "" : request.getQueryParams().toString());
            }
            HttpHeaders headers = request.getHeaders();
            String url = exchange.getRequest().getURI().toString();
            String token;
            if(url.contains("/user-center/login")){
                List<String> accessToken = headers1.get("accessToken");
                List<String> refreshToken = headers1.get("refreshToken");
                JSONObject object = JSON.parseObject(httpResult);
                token = object.getString("data");
                DecodedJWT jwt = Sign.verifyUserToken(token);
                Long userId = jwt.getClaim(Sign.CLAIM_USER_ID).asLong();
                LogValidity entity = new LogValidity();
                //是否存在记录
                assert accessToken != null;
                assert refreshToken != null;
                if(logValidity.isExist(userId) >= 1){
//                    entity.setUpdateTime(new Date());
//                    entity.setValidityTime(System.currentTimeMillis() / 1000);
//                    entity.setAccessToken(accessToken.get(0));
//                    entity.setRefreshToken(refreshToken.get(0));
//                    QueryWrapper<LogValidity> query = new QueryWrapper<>();
//                    query.eq("user_id", userId);
//                    logValidity.update(entity,query);
                    entity = LogValidity.builder().
                            validityTime(System.currentTimeMillis() / 1000).
                            userId(userId).
                            accessToken(accessToken.get(0)).
                            refreshToken(refreshToken.get(0)).build();
                    logValidity.updateLogValidity(entity);

                    log.info("insertSysLog 修改用户记录 {}-",entity);
                }else {
//                    entity.setId(SnowFlakeIdGenerator.getInstance().nextId());
//                    entity.setCreateTime(new Date());
//                    entity.setUpdateTime(new Date());
//                    entity.setValidityTime(System.currentTimeMillis() / 1000);
//                    entity.setUserId(userId);
//                    entity.setUserName(jwt.getClaim(Sign.CLAIM_USER_LOGIN_NAME).asString());
//                    entity.setAccessToken(accessToken.get(0));
//                    entity.setRefreshToken(refreshToken.get(0));
//                    logValidity.insert(entity);
                    entity = LogValidity.builder().id(SnowFlakeIdGenerator.getInstance().nextId()).
                            validityTime(System.currentTimeMillis() / 1000).
                            userId(userId).userName(jwt.getClaim(Sign.CLAIM_USER_LOGIN_NAME).asString()).
                            accessToken(accessToken.get(0)).refreshToken(refreshToken.get(0)).build();
                    logValidity.saveLogValidity(entity);
                    log.info("insertSysLog 新增用户记录 {}-",entity);
                }

            }else{
                token = headers.getFirst(AuthConstant.AUTHORIZATION_HEADER);
            }

            if (StringUtils.isNotBlank(token)) {
                DecodedJWT jwt = Sign.verifyUserToken(token);
                String username = jwt.getClaim(Sign.CLAIM_USER_LOGIN_NAME).asString();
                //登陆账号
                sysLog.setUsername(username);
                String displayName = jwt.getClaim(Sign.CLAIM_USER_DISPLAY_NAME).asString();
                //展示名
                sysLog.setDisplayName(displayName == null ? username : displayName);
                //创建者
                Long createUser = jwt.getClaim(Sign.CLAIM_USER_ID).asLong();
                sysLog.setCreateUser(createUser);
                log.info("日志信息：" + sysLog.toString());
                //insert sys log
                if (this.sysLogService == null) {
                    ApplicationContext context = exchange.getApplicationContext();
                    if (context != null) {
                        this.sysLogService = (ISysLogService) context.getBean("sysLogService");
                    }
                }
                if (this.sysLogService != null) {
                    this.sysLogService.insert(sysLog);
                }
            }

        } catch (Exception e) {
            log.error("系统日志插入异常！", e);
        }
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
