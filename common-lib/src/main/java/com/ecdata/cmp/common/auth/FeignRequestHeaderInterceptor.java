package com.ecdata.cmp.common.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecdata.cmp.common.crypto.Sign;
import com.ecdata.cmp.common.dto.FeignLog;
import com.ecdata.cmp.common.utils.DateUtil;
import com.ecdata.cmp.common.utils.SnowFlakeIdGenerator;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

/**
 * @author honglei
 * Feign interceptor，for passing auth info to backend
 * @since 2019-08-16
 */
@Slf4j
@Component
public class FeignRequestHeaderInterceptor implements RequestInterceptor {
    /**
     * 日志
     */
    private static final ILogger LOG = SLoggerFactory.getLogger(FeignRequestHeaderInterceptor.class);

    /**
     * @param requestTemplate Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = AuthContext.getAuthz();
        LOG.info("*************FeignRequestHeaderInterceptor:token:" + token);
        if (!StringUtils.isEmpty(token)) {
            requestTemplate.header(AuthConstant.AUTHORIZATION_HEADER, token);

            //系统日志
            try {
                log.info("feign系统日志！");
//                getSysLog(requestTemplate);
            } catch (Exception e) {
                log.error("系统日志插入异常！", e);
            }
        }
    }

    private void getSysLog(RequestTemplate requestTemplate) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        FeignLog sysLog = FeignLog.builder()
                .type(2)//正常
                .createTime(DateUtil.getNow())
                .id(SnowFlakeIdGenerator.getInstance().nextId())
                .uri(request.getRequestURI() == null ? "" : request.getRequestURI().toString())
                .ip(Objects.requireNonNull(request.getRequestURL()).toString())
                .build();
        if (request.getMethod() != null) {
            sysLog.setMethod(request.getMethod().toString());
            if ("POST".equalsIgnoreCase(request.getMethod().toString())) {
                sysLog.setParams(getPostData(request));
            } else {
                if(request.getQueryString()!=null){
//                    sysLog.setParams("{}".equals(request.getParameterNames().toString()) ? "" : request.getParameterNames().toString());
                }
            }
        }
        String token = AuthContext.getAuthz();
        if (token != null) {
            DecodedJWT jwt = Sign.verifyUserToken(token);
            String username = jwt.getClaim(Sign.CLAIM_USER_DISPLAY_NAME).asString();
            sysLog.setUsername(username); //请求者名字
            Long createUser = jwt.getClaim(Sign.CLAIM_USER_ID).asLong();//请求id
            sysLog.setCreateUser(createUser);
        }

        log.info("🔥日志信息：" + sysLog.toString());
        //insert sys log
    }

    private static String getPostData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
        } finally {
        }
        return data == null ? "" : data.toString();
    }

}
