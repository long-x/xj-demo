package com.ecdata.cmp.common.config;

import com.ecdata.cmp.common.aop.SentryClientAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author honglei
 * @since 2019-08-21
 */
@Configuration
@Import(value = {OcpmpgConfig.class, SentryClientAspect.class, })
public class OcpmgpWebConfig {
}
