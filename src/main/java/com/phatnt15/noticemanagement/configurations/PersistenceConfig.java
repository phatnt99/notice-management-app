package com.phatnt15.noticemanagement.configurations;

import com.phatnt15.noticemanagement.services.impls.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * The type Persistence config.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceConfig {

    /**
     * Auditor provider auditor aware.
     *
     * @return the auditor aware
     */
    @Bean
     AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
