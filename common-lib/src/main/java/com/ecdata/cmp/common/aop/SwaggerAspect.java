package com.ecdata.cmp.common.aop;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

@Aspect
@Slf4j
@Component
public class SwaggerAspect {

    @Before("execution(* com.ecdata..*Controller.*(..))")
    public void before(JoinPoint joinPoint) {
        ApiOperation apiOperation = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(ApiOperation.class);

        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        if (response != null) {
            response.setCharacterEncoding("utf-8");

            try {
                response.setHeader("apiOperation", URLEncoder.encode(apiOperation.value(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
