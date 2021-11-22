package com.ecdata.cmp.common.env;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author honglei
 * @since 2019-08-14
 */
@Data
@Builder
public class EnvConfig {
    /**
     * name
     */
    private String name;
    /**
     * debug
     */
    private boolean debug;
    /**
     * externalApex
     */
    private String externalApex;
    /**
     * internalApex
     */
    private String internalApex;
    /**
     * scheme
     */
    private String scheme;
    /**
     * map
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static Map<String, EnvConfig> MAPCONFIG;

    static {
        MAPCONFIG = new HashMap<>();
        EnvConfig envConfig = EnvConfig.builder().name(EnvConstant.ENV_DEV)
                .debug(true)
                .externalApex("ocpmgp.local")
                .internalApex(EnvConstant.ENV_DEV)
                .scheme("http")
                .build();
        MAPCONFIG.put(EnvConstant.ENV_DEV, envConfig);

        envConfig = EnvConfig.builder().name(EnvConstant.ENV_PROD)
                .debug(false)
                .externalApex("ocpmgp.cci")
                .internalApex(EnvConstant.ENV_PROD)
                .scheme("https")
                .build();
        MAPCONFIG.put(EnvConstant.ENV_PROD, envConfig);
    }

    /**
     * @param env env
     * @return envConfig
     */
    public static EnvConfig getEnvConfg(String env) {
        EnvConfig envConfig = MAPCONFIG.get(env);
        if (envConfig == null) {
            envConfig = MAPCONFIG.get(EnvConstant.ENV_DEV);
        }
        return envConfig;
    }
}
