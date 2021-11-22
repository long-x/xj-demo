package com.ecdata.cmp.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author honglei
 * @since 2019-08-14
 */
@Aspect
@Slf4j
public class SentryClientAspect {
    /**
     * @param joinPoint joinPoint
     * @throws Throwable Throwable
     */
    @Around("execution(* io.sentry.SentryClient.send*(..))")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        // no sentry logging in debug mode 如果是debug模式，不启用sentry服务
//        if (true) {
//            log.debug("no sentry logging in debug mode");
//            return;
//        }
        joinPoint.proceed();
    }
}
