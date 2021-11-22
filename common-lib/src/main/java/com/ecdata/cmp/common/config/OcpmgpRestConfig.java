package com.ecdata.cmp.common.config;

import com.ecdata.cmp.common.aop.SentryClientAspect;
import com.ecdata.cmp.common.error.GlobalExceptionTranslator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author honglei
 * @since 2019-08-16
 * Use this common config for Rest API
 */
@Configuration
@Import(value = {OcpmpgConfig.class, SentryClientAspect.class, GlobalExceptionTranslator.class})
public class OcpmgpRestConfig {
}
