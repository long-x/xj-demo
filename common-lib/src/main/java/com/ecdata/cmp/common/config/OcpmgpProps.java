package com.ecdata.cmp.common.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

/**
 * @author honglei
 * @since 2019-08-16
 */
@ConfigurationProperties(prefix = "ocpmgp.common")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcpmgpProps {
    /**
     * sentryDsn
     */
    @NotBlank
    private String sentryDsn;
    /**
     * deployEnv
     */
    @NotBlank
    // DeployEnvVar is set by Kubernetes during a new deployment so we can identify the code version
    private String deployEnv;
}
