package com.ecdata.cmp.common.config;

import com.ecdata.cmp.common.auth.AuthorizeInterceptor;
import com.ecdata.cmp.common.auth.FeignRequestHeaderInterceptor;
import com.ecdata.cmp.common.env.EnvConfig;
import com.github.structlog4j.StructLog4J;
import com.github.structlog4j.json.JsonFormatter;
import feign.RequestInterceptor;
import io.sentry.Sentry;
import io.sentry.SentryClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author honglei
 * @since 2019-08-16
 */

@Configuration
@EnableConfigurationProperties(OcpmgpProps.class)
public class OcpmpgConfig implements WebMvcConfigurer {
    /** activeProfile **/
    @Value("${spring.profiles.active:NA}")
    private String activeProfile;
    /** appName **/
    @Value("${spring.application.name:NA}")
    private String appName;
    /** ocpmgpProps **/
    @Autowired
    private OcpmgpProps ocpmgpProps;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public EnvConfig envConfig() {
        return EnvConfig.getEnvConfg(activeProfile);
    }

    @Bean
    public SentryClient sentryClient() {

        SentryClient sentryClient = Sentry.init(ocpmgpProps.getSentryDsn());
        sentryClient.setEnvironment(activeProfile);
        sentryClient.setRelease(ocpmgpProps.getDeployEnv());
        sentryClient.addTag("service", appName);

        return sentryClient;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorizeInterceptor());
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return new FeignRequestHeaderInterceptor();
    }

    /**
     * 初始化StructLog4J日志
     */
    @PostConstruct
    public void init() {
        // init structured logging
        StructLog4J.setFormatter(JsonFormatter.getInstance());

        // global log fields setting
        StructLog4J.setMandatoryContextSupplier(() -> new Object[]{
                "env", activeProfile,
                "service", appName});
    }

    @PreDestroy
    public void destroy() {
        sentryClient().closeConnection();
    }
}
